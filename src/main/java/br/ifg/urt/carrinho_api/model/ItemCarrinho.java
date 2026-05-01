package br.ifg.urt.carrinho_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


@Entity
@Table(name = "itens_carrinho")
public class ItemCarrinho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer quantidade;

    // Mantemos Double aqui pois é um registro histórico do valor no ato da compra
    @Column(nullable = false)
    private Double precoUnitario;

    @ManyToOne(fetch = FetchType.LAZY) // Lazy para performance
    @JoinColumn(name = "carrinho_id")
    private Carrinho carrinho;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id")
    private Produto produto;

    public ItemCarrinho() {}

    public ItemCarrinho(Carrinho carrinho, Produto produto, Integer quantidade) {
        this.carrinho = carrinho;
        this.produto = produto;
        this.quantidade = quantidade;
        // O item "copia" o valor atual do produto
        this.precoUnitario = produto.getPreco().valor(); 
    }

    // Regra de negócio: Subtotal do item
    public Double getSubtotal() {
        return this.precoUnitario * this.quantidade;
    }

    // Método para adicionar quantidade (usado no POST para somar à quantidade existente) 
    public void adicionarQuantidade(Integer novaQuantidade) {
        if (novaQuantidade != null && novaQuantidade > 0) {
            this.quantidade += novaQuantidade;
        }
    }

    // Método para atualizar a quantidade para um valor fixo (PATCH)
    public void atualizarQuantidadeAbsoluta(Integer novaQuantidade) {
        if (novaQuantidade != null && novaQuantidade > 0) {
            this.quantidade = novaQuantidade;
        }
    }

    public Long getId() {
        return id;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public Double getPrecoUnitario() {
        return precoUnitario;
    }

    public Produto getProduto() {
        return produto;
    }

    
}
