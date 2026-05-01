package br.ifg.urt.carrinho_api.dto.produto;

public record ProdutoResponseDTO(
    Long id,
    String nome, 
    Double valor,      // Extraído do VO
    String moeda,      // Extraído do VO
    String precoFormatado // Novo campo útil vindo da lógica do VO
) {
    // Zero métodos estáticos aqui! 
    // O mapeamento é responsabilidade do Mapper, 
    // mas podemos ter um método auxiliar
}