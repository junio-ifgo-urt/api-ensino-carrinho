package br.ifg.urt.carrinho_api.controller;

import br.ifg.urt.carrinho_api.assembler.CarrinhoModelAssembler;
import br.ifg.urt.carrinho_api.dto.carrinho.AtualizarQuantidadeItemDTO;
import br.ifg.urt.carrinho_api.dto.carrinho.CarrinhoResponseDTO;
import br.ifg.urt.carrinho_api.dto.carrinho.ItemCarrinhoRequestDTO;
import br.ifg.urt.carrinho_api.exception.ExceptionResponse;
import br.ifg.urt.carrinho_api.service.CarrinhoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carrinhos")
@Validated
@Tag(name = "Carrinho", description = "Endpoints para gerenciamento do carrinho de compras e itens")
public class CarrinhoController {

    private final CarrinhoService service;
    private final CarrinhoModelAssembler assembler;

    public CarrinhoController(CarrinhoService service,
                              CarrinhoModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    // Endpoint para adicionar um item ao carrinho, usando POST e recebendo um DTO com clienteId, produtoId e quantidade
    @PostMapping(value = "/adicionar", 
                 consumes = MediaType.APPLICATION_JSON_VALUE, 
                 produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Adicionar item ao carrinho",
        description = "Recebe um JSON com clienteId, produtoId e quantidade. Gerencia a criação ou atualização do carrinho.",
        responses = {
            // A linha ativa (sem o content) permite que o Swagger descubra o retorno 
            // sozinho através da assinatura do método Java
            @ApiResponse(description = "Item adicionado com sucesso", responseCode = "200"), 
            //@ApiResponse(description = "Item adicionado com sucesso", responseCode = "200", 
            //             content = @Content(schema = @Schema(implementation = CarrinhoResponseDTO.class))),
            @ApiResponse(description = "Produto ou Cliente não encontrado", responseCode = "404",
                         content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(description = "Dados inválidos", responseCode = "400",
                         content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
        }
    )
    public ResponseEntity<EntityModel<CarrinhoResponseDTO>> adicionarItem(
            @Valid @RequestBody ItemCarrinhoRequestDTO dto) {

        CarrinhoResponseDTO response = service.adicionarProduto(dto);
        // Pega o DTO puro e envolve com EntityModel + adiciona os _links
        return ResponseEntity.ok(assembler.toModel(response));
    }

    // Endpoint para atualizar a quantidade de um item específico no carrinho, usando PATCH e passando clienteId na URL
    @PatchMapping(value = "/cliente/{clienteId}/atualizar-quantidade", 
              consumes = MediaType.APPLICATION_JSON_VALUE, 
              produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Atualizar quantidade de um item",
        description = "Altera a quantidade de um produto específico que já está no carrinho do cliente.",
        responses = {
            // A linha ativa (sem o content) permite que o Swagger descubra o retorno 
            // sozinho através da assinatura do método Java
            @ApiResponse(description = "Quantidade atualizada", responseCode = "200"),
           // @ApiResponse(description = "Quantidade atualizada", responseCode = "200", 
           //             content = @Content(schema = @Schema(implementation = CarrinhoResponseDTO.class))),
            @ApiResponse(description = "Carrinho ou Produto não encontrado", responseCode = "404",
                        content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(description = "Dados inválidos", responseCode = "400",
                        content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
        }
    )
    public ResponseEntity<EntityModel<CarrinhoResponseDTO>> atualizarQuantidade(
            @PathVariable Long clienteId,
            @Valid @RequestBody AtualizarQuantidadeItemDTO dto) {

        CarrinhoResponseDTO response = service.atualizarQuantidade(clienteId, dto);

        return ResponseEntity.ok(assembler.toModel(response));
    }

    // Endpoint para remover um item do carrinho, usando DELETE e passando clienteId e produtoId na URL
    @DeleteMapping(value = "/cliente/{clienteId}/remover-produto/{produtoId}")
    @Operation(
        summary = "Remover produto do carrinho",
        description = "Remove completamente um produto da lista de itens do carrinho do cliente.",
        responses = {
            @ApiResponse(description = "Produto removido com sucesso", responseCode = "204"),
            @ApiResponse(description = "Carrinho ou Produto não encontrado", responseCode = "404",
                content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
        }
    )
    public ResponseEntity<Void> removerItem(
            @Parameter(description = "ID do cliente dono do carrinho", example = "1")
            @PathVariable Long clienteId, 
            
            @Parameter(description = "ID do produto a ser removido", example = "5")
            @PathVariable Long produtoId) {
    
        service.removerProduto(clienteId, produtoId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/cliente/{clienteId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Buscar carrinho por cliente",
        description = "Recupera o carrinho atual e todos os seus itens associados a um cliente específico.",
        responses = {
            // A linha ativa (sem o content) permite que o Swagger descubra o retorno 
            // sozinho através da assinatura do método Java
            @ApiResponse(description = "Sucesso", responseCode = "200"),
           // @ApiResponse(description = "Sucesso", responseCode = "200", 
           //              content = @Content(schema = @Schema(implementation = CarrinhoResponseDTO.class))),
            // Mapeando explicitamente o erro 404
            @ApiResponse(description = "Carrinho não encontrado", responseCode = "404",
                         content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
        }
    )
    public ResponseEntity<EntityModel<CarrinhoResponseDTO>> buscarPorCliente(
            @PathVariable Long clienteId) {

        CarrinhoResponseDTO response = service.buscarPorCliente(clienteId);

        return ResponseEntity.ok(assembler.toModel(response));
    }
}
