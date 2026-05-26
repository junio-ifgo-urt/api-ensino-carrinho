package br.ifg.urt.carrinho_api.assembler;

import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;
import br.ifg.urt.carrinho_api.controller.CarrinhoController;
import br.ifg.urt.carrinho_api.controller.ProdutoController;
import br.ifg.urt.carrinho_api.dto.carrinho.CarrinhoResponseDTO;// Se existir
import br.ifg.urt.carrinho_api.dto.produto.ProdutoResponseDTO;

import org.springframework.hateoas.server.RepresentationModelAssembler;

@Component
public class ProdutoModelAssembler 
       implements RepresentationModelAssembler<ProdutoResponseDTO, EntityModel<ProdutoResponseDTO>> {

    public EntityModel<ProdutoResponseDTO> toModel(ProdutoResponseDTO dto) {

        EntityModel<ProdutoResponseDTO> model = EntityModel.of(dto);

        // Link Self: Busca o próprio produto
        model.add(linkTo(methodOn(ProdutoController.class)
                .buscarPorId(dto.id()))
                .withSelfRel());

        // Link para estoque do produto
        model.add(linkTo(methodOn(ProdutoController.class)
                .buscarEstoque(dto.id()))
                .withRel("estoque"));

        // Link para adicionar ao carrinho - só se tiver estoque disponível
        // REGRA: só adiciona link se tiver estoque
        if (dto.estoque() > 0) {
            model.add(linkTo(methodOn(CarrinhoController.class)
                    .adicionarItem(null))
                    .withRel("add-to-cart"));
        }

        return model;
    }
}


