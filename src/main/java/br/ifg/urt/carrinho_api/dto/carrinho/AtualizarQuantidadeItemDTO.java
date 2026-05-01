package br.ifg.urt.carrinho_api.dto.carrinho;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AtualizarQuantidadeItemDTO(
    @NotNull(message = "O ID do produto é obrigatório")
    Long produtoId,

    @NotNull(message = "A nova quantidade é obrigatória")
    @Min(value = 1, message = "A quantidade mínima permitida é 1")
    Integer quantidade
) {}
