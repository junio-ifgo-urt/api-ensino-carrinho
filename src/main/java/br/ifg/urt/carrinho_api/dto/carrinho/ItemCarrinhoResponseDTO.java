package br.ifg.urt.carrinho_api.dto.carrinho;

import br.ifg.urt.carrinho_api.model.ItemCarrinho;

public record ItemCarrinhoResponseDTO(
    Long produtoId,
    String produtoNome,
    Integer quantidade,
    Double precoUnitario,
    Double subtotal
) { }
