package br.com.fiap.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FilialRequest(
        @NotBlank(message = "Nome da filial não pode ser vazio")
        @Size(min = 2, max = 100, message = "Nome da filial deve ter entre 2 e 100 caracteres")
        String nome,

        @NotBlank(message = "País não pode ser vazio")
        @Size(min = 2, max = 50, message = "País deve ter entre 2 e 50 caracteres")
        String pais,

        @NotBlank(message = "Logradouro não pode ser vazio")
        @Size(min = 5, max = 255, message = "Logradouro deve ter entre 5 e 255 caracteres")
        String logradouro
) {}
