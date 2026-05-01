package br.ifg.urt.carrinho_api.dto.produto;

public record ProdutoEstoqueResponseDTO(
    Long id,
    String nome,
    Integer estoque
) {
}
