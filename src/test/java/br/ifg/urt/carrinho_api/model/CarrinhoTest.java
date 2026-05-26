package br.ifg.urt.carrinho_api.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.ifg.urt.carrinho_api.mother.ProdutoMother;

public class CarrinhoTest {

    // Variável de instância para o carrinho, inicializada antes de cada teste
    private Carrinho carrinho;

    @BeforeEach
    void setup() {
        carrinho = new Carrinho();
    }

    @Test
    @DisplayName("Deve adicionar um novo item ao carrinho")
    void deveAdicionarNovoItemAoCarrinho() {

        // Arrange
        Produto produto = ProdutoMother.mouseGamer();

        // Act
        carrinho.adicionarItem(produto, 2);

        // Assert
        // Verifica se o item foi adicionado corretamente
        assertEquals(1, carrinho.getItens().size());

        // Verifica se a quantidade do item é a esperada
        assertEquals(2, carrinho.getItens().get(0).getQuantidade());

        // Verifica se o total do carrinho é o esperado (150 * 2)
        assertEquals(300.0, carrinho.getTotal());
    }

    @Test
    @DisplayName("Deve somar a quantidade quando o produto já existir no carrinho")
    void deveSomarQuantidadeQuandoProdutoJaExiste() {

        // Arrange
        Produto produto = ProdutoMother.tecladoMecanico();

        // Act
        carrinho.adicionarItem(produto, 2);
        carrinho.adicionarItem(produto, 3);

        // Assert
        // Verifica se o item foi adicionado apenas uma vez
        assertEquals(1, carrinho.getItens().size());

        // Verifica se a quantidade do item foi somada corretamente (2 + 3)
        assertEquals(5, carrinho.getItens().get(0).getQuantidade());
    }

    @Test
    @DisplayName("Deve atualizar a quantidade do item no carrinho")
    void deveAtualizarQuantidadeDoItem() {

        // Arrange
        Produto produto = ProdutoMother.monitor();

        carrinho.adicionarItem(produto, 2);

        // Act
        carrinho.atualizarQuantidadeItem(produto, 10);

        // Assert
        // Verifica se a quantidade do item foi atualizada corretamente para 10
        assertEquals(10, carrinho.getItens().get(0).getQuantidade());
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar quantidade inválida")
    void deveLancarExcecaoQuandoQuantidadeForInvalida() {

        // Arrange
        Produto produto = ProdutoMother.monitor();

        carrinho.adicionarItem(produto, 1);

        // Act + Assert
        // Verifica se a exceção é lançada para quantidades inválidas
        assertThrows(
                IllegalArgumentException.class,
                () -> carrinho.atualizarQuantidadeItem(produto, 0)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> carrinho.atualizarQuantidadeItem(produto, -1)
        );
    }

    @Test
    @DisplayName("Deve remover item do carrinho")
    void deveRemoverItemDoCarrinho() {

        // Arrange
        Produto produto = ProdutoMother.notebookGamer();

        carrinho.adicionarItem(produto, 2);

        // Act
        carrinho.removerItem(produto);

        // Assert
        // Verifica se o item foi removido corretamente
        assertEquals(0, carrinho.getItens().size());
    }

    @Test
    @DisplayName("Deve calcular o total do carrinho")
    void deveCalcularTotalDoCarrinho() {

        // Arrange
        Produto produto1 = ProdutoMother.mouseGamer();
        Produto produto2 = ProdutoMother.tecladoMecanico();

        carrinho.adicionarItem(produto1, 2); // 150 * 2 = 300
        carrinho.adicionarItem(produto2, 1); // 250 * 1 = 250

        // Act
        Double total = carrinho.getTotal();

        // Assert
        assertEquals(550.0, total);
    }
}

