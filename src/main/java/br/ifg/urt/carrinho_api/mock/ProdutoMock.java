package br.ifg.urt.carrinho_api.mock;

import java.util.ArrayList;
import java.util.List;

import br.ifg.urt.carrinho_api.model.Produto;

public class ProdutoMock {

    public static List<Produto> createList() {
        List<Produto> produtos = new ArrayList<>();
        produtos.add(create(1L, "Teclado", "Teclado Mecânico", 250.0, 10));
        produtos.add(create(2L, "Mouse", "Mouse Gamer", 150.0, 5));
        produtos.add(create(3L, "Cadeira", "Cadeira Gamer", 800.0, 3));
        return produtos;
    }

    private static Produto create(Long id, String nome, String descricao, Double preco, Integer estoque) {
        Produto p = new Produto();
        p.setId(id);
        p.setNome(nome);
        p.setDescricao(descricao);
        p.setPreco(preco);
        p.setEstoque(estoque);
        return p;
    }
}
