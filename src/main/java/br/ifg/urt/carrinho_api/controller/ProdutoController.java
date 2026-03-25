package br.ifg.urt.carrinho_api.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import br.ifg.urt.carrinho_api.dto.request.ProdutoRequestDTO;
import br.ifg.urt.carrinho_api.dto.response.ProdutoEstoqueResponseDTO;
import br.ifg.urt.carrinho_api.dto.response.ProdutoInventarioDTO;
import br.ifg.urt.carrinho_api.dto.response.ProdutoResponseDTO;
import br.ifg.urt.carrinho_api.service.ProdutoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/produtos")
@Validated // para validar @RequestParam e @PathVariable
public class ProdutoController {

    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    // 200 OK - Agora retorna uma lista de DTOs (apenas nome e preço)
    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> buscarTodos() {
        return ResponseEntity.ok(service.findAll());
    }

    // 200 OK - Busca individual retornando DTO
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    // 200 OK - Busca individual retornando DTO específico de estoque (apenas nome e estoque)
    // Rota: GET /produtos/{id}/estoque
    @GetMapping("/{id}/estoque")
    public ResponseEntity<ProdutoEstoqueResponseDTO> buscarEstoque(@PathVariable Long id) {
        return ResponseEntity.ok(service.getEstoque(id));
    }

    // 200 OK - Busca individual retornando DTO de inventário (com campo calculado)
    // Rota: GET /produtos/{id}/inventario
    @GetMapping("/{id}/inventario")
    public ResponseEntity<ProdutoInventarioDTO> buscarInventario(@PathVariable Long id) {
        // Chamamos o service, que usa o mapper, que por sua vez chama o cálculo da entidade
        ProdutoInventarioDTO inventario = service.getRelatorioInventario(id);
        return ResponseEntity.ok(inventario);
    }

    // 201 Created - Recebe ProdutoRequestDTO (corpo da requisição)
    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> criar(@Valid @RequestBody ProdutoRequestDTO dto) {
        ProdutoResponseDTO novo = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novo);
    }

    // 200 OK - Atualização passando DTO
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> atualizar(
            @PathVariable Long id, 
            @Valid @RequestBody ProdutoRequestDTO dto) {
        
        // Note que não setamos mais o ID aqui, o Service fará isso
        return ResponseEntity.ok(service.update(id, dto));
    }

    // 204 No Content - Permanece igual (não retorna corpo)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // PATCH - Realiza a baixa de estoque e retorna o DTO atualizado
    @PatchMapping("/{id}/baixar-estoque")
    public ResponseEntity<ProdutoEstoqueResponseDTO> baixarEstoque(
            @PathVariable Long id, 
            @NotNull(message = "A quantidade é obrigatória") 
            @Min(value = 1, message = "A quantidade mínima para baixa é 1 unidade")
            Integer quantidade) {
        
        return ResponseEntity.ok(service.baixarEstoque(id, quantidade));
    }

    // PATCH - Aplica desconto global no preço do produto
    // path: /api/v1/produtos/{id}/desconto?percentual=20
    @PatchMapping("/{id}/desconto")
    public ResponseEntity<Void> aplicarDesconto(
            @PathVariable Long id, 
            @RequestParam Double percentual) {
        
        service.aplicarDescontoGlobal(id, percentual);
        
        // Retornamos 204 No Content pois a operação foi um sucesso, 
        // mas não há corpo na resposta.
        return ResponseEntity.noContent().build();
    }
    
}