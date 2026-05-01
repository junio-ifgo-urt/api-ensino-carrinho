package br.ifg.urt.carrinho_api.dto.produto;

public record ProdutoInventarioDTO(
    String nome,
    Integer estoque,
    Double preco,
    Double valorTotalEstoque // Campo calculado!
) {}
