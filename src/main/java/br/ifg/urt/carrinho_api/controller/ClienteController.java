package br.ifg.urt.carrinho_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.ifg.urt.carrinho_api.assembler.ClienteModelAssembler;
import br.ifg.urt.carrinho_api.dto.cliente.ClienteRequestDTO;
import br.ifg.urt.carrinho_api.dto.cliente.ClienteResponseDTO;
import br.ifg.urt.carrinho_api.exception.ExceptionResponse;
import br.ifg.urt.carrinho_api.service.ClienteService;

@RestController
@RequestMapping("/clientes")
@Tag(name = "Cliente", description = "Endpoints para cadastro de clientes")
public class ClienteController {

    private final ClienteService service;
    private final ClienteModelAssembler assembler; // 1. Injetar o assembler

    public ClienteController(ClienteService service, ClienteModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    // Endpoint para cadastrar um novo cliente
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Cadastrar novo cliente",
        responses = {
            @ApiResponse(description = "Cliente criado com sucesso", responseCode = "201"),
            @ApiResponse(description = "Dados inválidos", responseCode = "400", 
                         content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
        }
    )
    public ResponseEntity<EntityModel<ClienteResponseDTO>> cadastrar(@Valid @RequestBody ClienteRequestDTO dto) {
        // O service retorna o record simples
        ClienteResponseDTO responseDto = service.salvar(dto);

        // O assembler transforma o record DTO em um EntityModel com links HATEOAS
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(responseDto));
    }
}
