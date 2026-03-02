package br.ifg.urt.carrinho_api.model;

import java.io.Serializable;

import br.ifg.urt.carrinho_api.exception.EstoqueInsuficienteException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity // 1. Indica que esta classe é uma tabela no banco de dados
@Table(name = "produtos") // 2. Nome da tabela (opcional, mas boa prática)
public class Produto implements Serializable { // 3. Adicionado Serializable (boa prática para JPA)

    private static final long serialVersionUID = 1L;

    @Id // 4. Define a chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 5. Auto-incremento (1, 2, 3...)
    private Long id;

    @Column(nullable = false, length = 100) // 6. Campo obrigatório e com limite de caracteres
    private String nome;

    @Column(length = 255)
    private String descricao;

    @Column(nullable = false)
    private Double preco;

    @Column(nullable = false)
    private Integer estoque;

    // O construtor padrão é OBRIGATÓRIO para o JPA
    public Produto() {
    }
    
    public Produto(Long id, String nome, String descricao, Double preco, Integer estoque) {
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

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Integer getEstoque() {
        return estoque;
    }

    public void setEstoque(Integer estoque) {
        this.estoque = estoque;
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

}
