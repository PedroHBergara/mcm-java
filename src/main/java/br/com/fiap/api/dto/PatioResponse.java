package br.com.fiap.api.dto;

public record PatioResponse(
        Long id,
        int qtdMotos,
        int numPatio,
        Long filialId
) {}


