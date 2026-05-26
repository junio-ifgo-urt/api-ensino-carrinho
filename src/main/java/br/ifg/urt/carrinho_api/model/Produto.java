package br.ifg.urt.carrinho_api.model;

import java.io.Serializable;

import br.ifg.urt.carrinho_api.exception.EstoqueInsuficienteException;
import br.ifg.urt.carrinho_api.model.vo.Preco;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity // Indica que esta classe é uma tabela no banco de dados
@Table(name = "produtos") // Nome da tabela (opcional, mas boa prática)
public class Produto implements Serializable { // Adicionado Serializable (boa prática para JPA)

    private static final long serialVersionUID = 1L;

    @Id // Define a chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremento 
    private Long id;

    @Column(nullable = false, length = 100) // Campo obrigatório e com limite de caracteres
    private String nome;

    @Column(length = 255)
    private String descricao;

    @Embedded // O banco terá as colunas 'preco_valor' e 'preco_moeda'
    private Preco preco;

    @Column(nullable = false)
    private Integer estoque;

    // O construtor padrão é OBRIGATÓRIO para o JPA
    public Produto() {
    }
    
    // Construtores adicionais para facilitar a criação de objetos em testes ou na aplicação
    public Produto(Long id, String nome, Preco preco) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
    }

    public Produto(Long id, String nome, String descricao, Preco preco, Integer estoque) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.estoque = estoque;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Preco getPreco() {
        return preco;
    }

    public void setPreco(Preco preco) {
        this.preco = preco;
    }

    public Integer getEstoque() {
        return estoque;
    }

    public void setEstoque(Integer estoque) {
        this.estoque = estoque;
    }

    // Método de negócio usando o VO
    public void atualizarPrecoComPromocao(Double percentual) {
        this.preco = this.preco.aplicarDesconto(percentual);
    }

    public void baixarEstoque(Integer quantidade) {
        // 1. Validação de parâmetro (Erro de argumento inválido)
        if (quantidade == null || quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade para baixar deve ser maior que zero.");
        }

        // 2. Validação de Regra de Negócio (exceção personalizada)
        if (this.estoque < quantidade) {
            throw new EstoqueInsuficienteException(
                "Estoque insuficiente para o produto: " + this.nome + 
                ". Disponível: " + this.estoque + ", Solicitado: " + quantidade
            );
        }

        // 3. Atualização do estado
        this.estoque -= quantidade;
    }

    // Método auxiliar para calcular o valor total do estoque (preço * quantidade)
    public Double getValorTotalEstoque() {
        if (this.preco == null || this.estoque == null) return 0.0;
        return this.preco.valor() * this.estoque;
    }

}
