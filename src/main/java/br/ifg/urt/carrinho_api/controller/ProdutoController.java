package br.ifg.urt.carrinho_api.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import br.ifg.urt.carrinho_api.dto.request.ProdutoRequestDTO;
import br.ifg.urt.carrinho_api.dto.response.ProdutoEstoqueResponseDTO;
import br.ifg.urt.carrinho_api.dto.response.ProdutoInventarioDTO;
import br.ifg.urt.carrinho_api.dto.response.ProdutoResponseDTO;
import br.ifg.urt.carrinho_api.exception.ExceptionResponse; // Ajuste o package conforme o seu projeto
import br.ifg.urt.carrinho_api.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
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

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Listar todos os produtos", description = "Retorna uma lista simplificada de produtos.",
        responses = {
            @ApiResponse(description = "Sucesso", responseCode = "200", 
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProdutoResponseDTO.class)))),
            @ApiResponse(description = "Erro Interno", responseCode = "500", 
                content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
        }
    )
    public ResponseEntity<List<ProdutoResponseDTO>> buscarTodos() {
        return ResponseEntity.ok(service.findAll());
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