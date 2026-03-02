package br.ifg.urt.carrinho_api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ifg.urt.carrinho_api.model.Produto;
import br.ifg.urt.carrinho_api.service.ProdutoService;

@RestController
@RequestMapping("/produtos") // Rota base para produtos
public class ProdutoController {

    private final ProdutoService service; // Injeção do serviço de produtos

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    // Endpoint para buscar todos os produtos
    @GetMapping
    public ResponseEntity<List<Produto>> buscarTodos() {
        return ResponseEntity.ok(service.findAll());
    }

    // Endpoint para buscar um produto por ID
    @GetMapping("/{id}/detalhes")
    public ResponseEntity<Produto> buscarPorId1(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/{id}")
    public Produto buscarPorId(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public Produto criar(@RequestBody Produto produto) {
        return service.create(produto);
    }

    @PutMapping("/{id}")
    public Produto atualizar(@PathVariable Long id, @RequestBody Produto produto) {
        // TODO: validar se id da URL corresponde ao id do body
        produto.setId(id);
        return service.update(produto);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        service.delete(id);
    }
}
