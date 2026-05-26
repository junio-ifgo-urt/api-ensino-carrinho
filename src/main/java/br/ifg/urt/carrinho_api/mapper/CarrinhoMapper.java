package br.ifg.urt.carrinho_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.ifg.urt.carrinho_api.dto.carrinho.CarrinhoResponseDTO;
import br.ifg.urt.carrinho_api.dto.carrinho.ItemCarrinhoResponseDTO;
import br.ifg.urt.carrinho_api.model.Carrinho;
import br.ifg.urt.carrinho_api.model.ItemCarrinho;

@Mapper(componentModel = "spring")
public interface CarrinhoMapper {

    @Mapping(source = "cliente.id", target = "clienteId") 
    @Mapping(source = "cliente.nome", target = "clienteNome")
    CarrinhoResponseDTO toResponseDTO(Carrinho carrinho);

    // O MapStruct usará este método automaticamente ao mapear a lista de itens no Carrinho
    @Mapping(source = "produto.id", target = "produtoId")
    @Mapping(source = "produto.nome", target = "produtoNome")
    ItemCarrinhoResponseDTO toItemResponseDTO(ItemCarrinho item);
}
