package br.ifg.urt.carrinho_api.assembler;

import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.stereotype.Component;

import br.ifg.urt.carrinho_api.controller.CarrinhoController;
import br.ifg.urt.carrinho_api.dto.carrinho.CarrinhoResponseDTO;// Se existir
import org.springframework.hateoas.server.RepresentationModelAssembler;

@Component
public class CarrinhoModelAssembler implements RepresentationModelAssembler<CarrinhoResponseDTO, EntityModel<CarrinhoResponseDTO>> {

    @Override
    public EntityModel<CarrinhoResponseDTO> toModel(CarrinhoResponseDTO dto) {
        
        EntityModel<CarrinhoResponseDTO> model = EntityModel.of(dto);

        // Link Self: Busca o próprio carrinho
        model.add(linkTo(methodOn(CarrinhoController.class)
                .buscarPorCliente(dto.clienteId()))
                .withSelfRel());

        // Link para Atualizar Quantidade do Item (exemplo, pode ser ajustado conforme a implementação do controller)
        model.add(linkTo(methodOn(CarrinhoController.class)
                .atualizarQuantidade(dto.clienteId(), null))
                .withRel("atualizar-item"));
                
        // Link para Adicionar Item
        model.add(linkTo(methodOn(CarrinhoController.class)
                .adicionarItem(null))
                .withRel("adicionar-item"));

        return model;
    }
}