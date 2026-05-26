package br.ifg.urt.carrinho_api.dto.carrinho;

import java.time.LocalDateTime;
import java.util.List;

public record CarrinhoResponseDTO(
    Long id,
    Long clienteId,
    String clienteNome, // Verifique se este campo está assim
    LocalDateTime dataCriacao,
    List<ItemCarrinhoResponseDTO> itens,
    Double total
) { }