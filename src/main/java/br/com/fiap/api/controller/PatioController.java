package br.com.fiap.api.controller;

import br.com.fiap.api.dto.PatioRequest;
import br.com.fiap.api.dto.PatioResponse;
import br.com.fiap.api.exception.RestExceptionHandler;
import br.com.fiap.api.service.PatioService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


import java.net.URI;

@RestController
@RequestMapping("/patios" )
@Tag(name = "Patios", description = "Endpoints para gerenciamento de Pátios")
public class PatioController {

    private final PatioService patioService;

    @Autowired
    public PatioController(PatioService patioService) {
        this.patioService = patioService;
    }

    @GetMapping
    @Operation(summary = "Lista todos os pátios", description = "Retorna uma lista paginada de pátios. Pode ser filtrada por ID da filial.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pátios retornada com sucesso")
    })
    public ResponseEntity<Page<PatioResponse>> listarPatios(
            @Parameter(description = "ID da Filial para filtrar os pátios", required = false, example = "1")
            @RequestParam(required = false) Long filialId,
            @Parameter(description = "Configuração de paginação e ordenação")
            @PageableDefault(size = 10, sort = {"numPatio"}) Pageable pageable) {

        Page<PatioResponse> patios;
        if (filialId != null) {
            patios = patioService.findByFilialId(filialId, pageable);
        } else {
            patios = patioService.findAll(pageable);
        }
        return ResponseEntity.ok(patios);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um pátio pelo ID", description = "Retorna os detalhes de um pátio específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pátio encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pátio não encontrado", content = @Content(schema = @Schema(implementation = RestExceptionHandler.ApiErrorResponse.class))) // Assuming ApiErrorResponse exists in your handler
    })
    public ResponseEntity<PatioResponse> buscarPatioPorId(
            @Parameter(description = "ID do pátio a ser buscado", required = true, example = "1")
            @PathVariable Long id) {
        PatioResponse patio = patioService.findById(id);
        return ResponseEntity.ok(patio);
    }

    @PostMapping
    @Operation(summary = "Cria um novo pátio", description = "Registra um novo pátio associado a uma filial existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pátio criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content(schema = @Schema(implementation = RestExceptionHandler.ValidationErrorResponse.class))), // Assuming ValidationErrorResponse exists
            @ApiResponse(responseCode = "404", description = "Filial associada não encontrada", content = @Content(schema = @Schema(implementation = RestExceptionHandler.ApiErrorResponse.class)))
    })
    public ResponseEntity<PatioResponse> criarPatio(
            @Parameter(description = "Dados do pátio a ser criado", required = true)
            @Valid @RequestBody PatioRequest patioRequestDTO,
            UriComponentsBuilder uriBuilder) {
        PatioResponse createdPatio = patioService.create(patioRequestDTO);
        URI location = uriBuilder.path("/patios/{id}").buildAndExpand(createdPatio.id()).toUri();
        return ResponseEntity.created(location).body(createdPatio);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um pátio existente", description = "Modifica os dados de um pátio existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pátio atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content(schema = @Schema(implementation = RestExceptionHandler.ValidationErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Pátio ou Filial associada não encontrada", content = @Content(schema = @Schema(implementation = RestExceptionHandler.ApiErrorResponse.class)))
    })
    public ResponseEntity<PatioResponse> atualizarPatio(
            @Parameter(description = "ID do pátio a ser atualizado", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Novos dados para o pátio", required = true)
            @Valid @RequestBody PatioRequest patioRequestDTO) {
        PatioResponse updatedPatio = patioService.update(id, patioRequestDTO);
        return ResponseEntity.ok(updatedPatio);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deleta um pátio", description = "Remove um pátio existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pátio deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pátio não encontrado", content = @Content(schema = @Schema(implementation = RestExceptionHandler.ApiErrorResponse.class)))
    })
    public void deletarPatio(
            @Parameter(description = "ID do pátio a ser deletado", required = true, example = "1")
            @PathVariable Long id) {
        patioService.delete(id);
    }

}