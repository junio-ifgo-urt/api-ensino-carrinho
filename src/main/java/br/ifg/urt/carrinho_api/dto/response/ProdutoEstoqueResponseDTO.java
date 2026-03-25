package br.ifg.urt.carrinho_api.dto.response;

public record ProdutoEstoqueResponseDTO(
    Long id,
    String nome,
    Integer estoque
) {
}
