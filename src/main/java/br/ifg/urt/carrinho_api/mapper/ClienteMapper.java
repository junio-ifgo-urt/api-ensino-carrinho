package br.ifg.urt.carrinho_api.mapper;

import org.mapstruct.Mapper;

import br.ifg.urt.carrinho_api.dto.cliente.ClienteRequestDTO;
import br.ifg.urt.carrinho_api.dto.cliente.ClienteResponseDTO;
import br.ifg.urt.carrinho_api.model.Cliente;


@Mapper(componentModel = "spring")
public interface ClienteMapper {
    Cliente toEntity(ClienteRequestDTO dto);
    ClienteResponseDTO toResponseDTO(Cliente cliente);
}
