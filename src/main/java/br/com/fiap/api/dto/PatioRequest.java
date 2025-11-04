package br.com.fiap.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PatioRequest(

        @NotNull(message = "Quantidade de motos não pode ser nula")
        @Min(value = 0, message = "Quantidade de motos não pode ser negativa")
        Integer qtdMotos,

        @NotNull(message = "Número do pátio não pode ser nulo")
        @Min(value = 1, message = "Número do pátio deve ser positivo")
        Integer numPatio,

        @NotNull(message = "ID da filial não pode ser nulo")
        Long filialId
) {}
