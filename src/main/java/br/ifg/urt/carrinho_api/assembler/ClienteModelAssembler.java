package br.ifg.urt.carrinho_api.assembler;

import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;
import br.ifg.urt.carrinho_api.controller.ClienteController;
import br.ifg.urt.carrinho_api.dto.cliente.ClienteResponseDTO;
import org.springframework.hateoas.server.RepresentationModelAssembler;

@Component
public class ClienteModelAssembler implements RepresentationModelAssembler<ClienteResponseDTO, EntityModel<ClienteResponseDTO>> {

    @Override
    public EntityModel<ClienteResponseDTO> toModel(ClienteResponseDTO dto) {
        // Cria o envelope (EntityModel) com os dados do teu record DTO
        EntityModel<ClienteResponseDTO> model = EntityModel.of(dto);
        // Adiciona links HATEOAS relevantes
        model.add(linkTo(methodOn(ClienteController.class).cadastrar(null)).withSelfRel());
        
        return model;
    }
}