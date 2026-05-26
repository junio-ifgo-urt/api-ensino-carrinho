package br.ifg.urt.carrinho_api.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import br.ifg.urt.carrinho_api.dto.produto.ProdutoRequestDTO;
import br.ifg.urt.carrinho_api.assembler.ProdutoModelAssembler;
import br.ifg.urt.carrinho_api.dto.produto.ProdutoEstoqueResponseDTO;
import br.ifg.urt.carrinho_api.dto.produto.ProdutoInventarioDTO;
import br.ifg.urt.carrinho_api.dto.produto.ProdutoResponseDTO;
import br.ifg.urt.carrinho_api.exception.ExceptionResponse; // Ajuste o package conforme o seu projeto
import br.ifg.urt.carrinho_api.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/produtos")
@Validated
@Tag(name = "Produtos", description = "Endpoints para gerenciamento do catálogo e estoque de produtos")
public class ProdutoController {

    private final ProdutoService service;
    private final ProdutoModelAssembler assembler;
    private final PagedResourcesAssembler<ProdutoResponseDTO> pagedAssembler;

    public ProdutoController(ProdutoService service,
                             ProdutoModelAssembler assembler,
                             PagedResourcesAssembler<ProdutoResponseDTO> pagedAssembler) {
        this.service = service;
        this.assembler = assembler;
        this.pagedAssembler = pagedAssembler;
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Listar produtos com paginação e filtro",
        description = "Retorna uma página de produtos. Permite filtrar por nome e utilizar paginação e ordenação com os parâmetros 'page', 'size' e 'sort'.",
        responses = {
            @ApiResponse(
                description = "Sucesso", responseCode = "200", content = @Content(schema = @Schema(implementation = Page.class))
            ),
            @ApiResponse(
                description = "Erro Interno", responseCode = "500", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            )
        }
    )
    public ResponseEntity<PagedModel<EntityModel<ProdutoResponseDTO>>> buscarTodosPorNome(
            @RequestParam(required = false) String nome,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        // Busca dados
        Page<ProdutoResponseDTO> page = service.findAll(nome, pageable);
        // Converte para HATEOAS
        PagedModel<EntityModel<ProdutoResponseDTO>> model =
                pagedAssembler.toModel(page, assembler::toModel);

        return ResponseEntity.ok(model);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Buscar produto por ID", 
        responses = {
            @ApiResponse(description = "Sucesso", responseCode = "200", content = @Content(schema = @Schema(implementation = ProdutoResponseDTO.class))),
            @ApiResponse(description = "Produto não encontrado", responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(description = "ID inválido", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
        }
    )
    public ResponseEntity<ProdutoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping(value = "/{id}/estoque", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Consultar estoque", 
        responses = {
            @ApiResponse(description = "Sucesso", responseCode = "200", content = @Content(schema = @Schema(implementation = ProdutoEstoqueResponseDTO.class))),
            @ApiResponse(description = "Produto não encontrado", responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
        }
    )
    public ResponseEntity<ProdutoEstoqueResponseDTO> buscarEstoque(@PathVariable Long id) {
        return ResponseEntity.ok(service.getEstoque(id));
    }

    @GetMapping(value = "/{id}/inventario", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Relatório de inventário", 
        responses = {
            @ApiResponse(description = "Sucesso", responseCode = "200", content = @Content(schema = @Schema(implementation = ProdutoInventarioDTO.class))),
            @ApiResponse(description = "Produto não encontrado", responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
        }
    )
    public ResponseEntity<ProdutoInventarioDTO> buscarInventario(@PathVariable Long id) {
        return ResponseEntity.ok(service.getRelatorioInventario(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Criar novo produto", 
        responses = {
            @ApiResponse(description = "Criado com sucesso", responseCode = "201", content = @Content(schema = @Schema(implementation = ProdutoResponseDTO.class))),
            @ApiResponse(description = "Erro de validação nos dados enviados", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
        }
    )
    public ResponseEntity<ProdutoResponseDTO> criar(@Valid @RequestBody ProdutoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Atualizar produto", 
        responses = {
            @ApiResponse(description = "Atualizado com sucesso", responseCode = "200", content = @Content(schema = @Schema(implementation = ProdutoResponseDTO.class))),
            @ApiResponse(description = "Produto não encontrado", responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(description = "Dados de atualização inválidos", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
        }
    )
    public ResponseEntity<ProdutoResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody ProdutoRequestDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir produto", 
        responses = {
            @ApiResponse(description = "Excluído com sucesso", responseCode = "204"),
            @ApiResponse(description = "Produto não encontrado", responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
        }
    )
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/{id}/baixar-estoque", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Dar baixa no estoque", 
        responses = {
            @ApiResponse(description = "Baixa realizada", responseCode = "200", content = @Content(schema = @Schema(implementation = ProdutoEstoqueResponseDTO.class))),
            @ApiResponse(description = "Estoque insuficiente ou ID inexistente", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(description = "Produto não encontrado", responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
        }
    )
    public ResponseEntity<ProdutoEstoqueResponseDTO> baixarEstoque(
            @PathVariable Long id, 
            @NotNull @Min(1) Integer quantidade) {
        return ResponseEntity.ok(service.baixarEstoque(id, quantidade));
    }

    @PatchMapping("/{id}/desconto")
    @Operation(summary = "Aplicar desconto", 
        responses = {
            @ApiResponse(description = "Desconto aplicado com sucesso", responseCode = "204"),
            @ApiResponse(description = "Produto não encontrado", responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(description = "Percentual inválido", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
        }
    )
    public ResponseEntity<Void> aplicarDesconto(@PathVariable Long id, @RequestParam Double percentual) {
        service.aplicarDescontoGlobal(id, percentual);
        return ResponseEntity.noContent().build();
    }
}