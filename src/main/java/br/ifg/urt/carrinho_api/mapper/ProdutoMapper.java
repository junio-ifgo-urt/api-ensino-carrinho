package br.ifg.urt.carrinho_api.mapper;

import br.ifg.urt.carrinho_api.dto.produto.ProdutoRequestDTO;
import br.ifg.urt.carrinho_api.dto.produto.ProdutoEstoqueResponseDTO;
import br.ifg.urt.carrinho_api.dto.produto.ProdutoInventarioDTO;
import br.ifg.urt.carrinho_api.dto.produto.ProdutoResponseDTO;
import br.ifg.urt.carrinho_api.model.Produto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {

    // O MapStruct liga automaticamente: 
    // Entidade.getValorTotalEstoque() -> DTO.valorTotalEstoque()
    // Converte Entidade para DTO de inventário (com campo calculado)
    @Mapping(source = "preco.valor", target = "preco")
    ProdutoInventarioDTO toInventarioDTO(Produto produto);

    // Mapeamento de Entidade (com VO) para Response DTO
    // Conecta Produto.preco.valor -> ProdutoResponseDTO.valor
    // Conecta Produto.preco.moeda -> ProdutoResponseDTO.moeda
    // Usa o método do VO para preencher o precoFormatado
    @Mapping(source = "preco.valor", target = "valor")
    @Mapping(source = "preco.moeda", target = "moeda")
    @Mapping(target = "precoFormatado", expression = "java(produto.getPreco().getFormatado())")
    ProdutoResponseDTO toResponseDTO(Produto produto);

    // Mapeamento de Request DTO para Entidade (com VO)
    // O MapStruct precisa saber como construir o record Preco a partir do Double do DTO
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "preco", target = "preco.valor")
    @Mapping(target = "preco.moeda", constant = "BRL") // Define moeda padrão na criação
    Produto toEntity(ProdutoRequestDTO dto);

    // Útil para o método findAll do Service
    List<ProdutoResponseDTO> toResponseDTOList(List<Produto> produtos);

    // Converte Entidade para DTO específico de estoque (apenas nome e estoque)
    ProdutoEstoqueResponseDTO toEstoqueDTO(Produto produto);
}