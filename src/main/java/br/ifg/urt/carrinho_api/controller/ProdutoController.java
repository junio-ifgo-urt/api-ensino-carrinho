package br.ifg.urt.carrinho_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.ifg.urt.carrinho_api.model.Produto;
import br.ifg.urt.carrinho_api.service.ProdutoService;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    // 200 OK - Padrão para listagens
    @GetMapping
    public ResponseEntity<List<Produto>> buscarTodos() {
        List<Produto> produtos = service.findAll();
        return ResponseEntity.ok(produtos);
    }

    // 200 OK - Padrão para busca individual bem-sucedida
    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        Produto produto = service.findById(id);
        return ResponseEntity.ok(produto);
    }

    // 201 Created - Padrão para criação de recursos
    @PostMapping
    public ResponseEntity<Produto> criar(@RequestBody Produto produto) {
        Produto novoProduto = service.create(produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoProduto);
    }

    // 200 OK - Recurso atualizado com sucesso
    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(@PathVariable Long id, @RequestBody Produto produto) {
        produto.setId(id);
        Produto produtoAtualizado = service.update(produto);
        return ResponseEntity.ok(produtoAtualizado);
    }

    // 204 No Content - Padrão para remoção (sucesso sem corpo de resposta)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint para realizar a baixa de estoque
    // Rota: PATCH /produtos/{id}/baixar-estoque?qtd=5
    @PatchMapping("/{id}/baixar-estoque")
    public ResponseEntity<Produto> baixarEstoque(
            @PathVariable Long id, 
            @RequestParam Integer quantidade) {
        
        Produto produtoAtualizado = service.baixarEstoque(id, quantidade);
        return ResponseEntity.ok(produtoAtualizado); // Retorna 200 OK com o JSON 
    }
}