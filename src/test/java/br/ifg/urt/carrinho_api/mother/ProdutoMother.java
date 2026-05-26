package br.ifg.urt.carrinho_api.mother;

import br.ifg.urt.carrinho_api.model.Produto;
import br.ifg.urt.carrinho_api.model.vo.Preco;

public class ProdutoMother {

    public static Produto mouseGamer() {
        return new Produto(
                1L,
                "Mouse Gamer",
                new Preco(150.0, "BRL")
        );
    }

    public static Produto tecladoMecanico() {
        return new Produto(
                2L,
                "Teclado Mecânico",
                new Preco(250.0, "BRL")
        );
    }

    public static Produto monitor() {
        return new Produto(
                3L,
                "Monitor",
                new Preco(1200.0, "BRL")
        );
    }

    public static Produto notebookGamer() {
        return new Produto(
                4L,
                "Notebook Gamer",
                new Preco(5850.0, "BRL")
        );
    }
}
