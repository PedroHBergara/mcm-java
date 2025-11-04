package br.com.fiap.api.controller;

import br.com.fiap.api.dto.FilialRequest;
import br.com.fiap.api.dto.FilialResponse;
import br.com.fiap.api.service.FilialService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/filiais")
public class FilialController {

    private final FilialService filialService;

    @Autowired
    public FilialController(FilialService filialService) {
        this.filialService = filialService;
    }

    @GetMapping
    public ResponseEntity<Page<FilialResponse>> listarFiliais(
            @RequestParam(required = false) String nome,
            @PageableDefault(size = 10, sort = {"nome"}) Pageable pageable) {

        Page<FilialResponse> filiais;
        if (nome != null && !nome.trim().isEmpty()) {
            filiais = filialService.findByNome(nome, pageable);
        } else {
            filiais = filialService.findAll(pageable);
        }
        return ResponseEntity.ok(filiais);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FilialResponse> buscarFilialPorId(@PathVariable Long id) {
        FilialResponse filial = filialService.findById(id);
        return ResponseEntity.ok(filial);
    }

    @PostMapping
    public ResponseEntity<FilialResponse> criarFilial(
            @Valid @RequestBody FilialRequest filialRequestDTO,
            UriComponentsBuilder uriBuilder) {
        FilialResponse createdFilial = filialService.create(filialRequestDTO);

        URI location = uriBuilder.path("/filiais/{id}").buildAndExpand(createdFilial.id()).toUri();
        return ResponseEntity.created(location).body(createdFilial);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FilialResponse> atualizarFilial(
            @PathVariable Long id,
            @Valid @RequestBody FilialRequest filialRequestDTO) {
        FilialResponse updatedFilial = filialService.update(id, filialRequestDTO);
        return ResponseEntity.ok(updatedFilial);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarFilial(@PathVariable Long id) {
        filialService.delete(id);
    }
}
