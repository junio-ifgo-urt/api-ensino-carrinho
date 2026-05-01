package br.ifg.urt.carrinho_api.dto.cliente;

import br.ifg.urt.carrinho_api.model.Cliente;

public record ClienteResponseDTO(
    Long id,
    String nome,
    String email
) {
    public static ClienteResponseDTO fromEntity(Cliente cliente) {
        return new ClienteResponseDTO(cliente.getId(), cliente.getNome(), cliente.getEmail());
    }
}
