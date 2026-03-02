package br.ifg.urt.carrinho_api.model;

import br.ifg.urt.carrinho_api.exception.EstoqueInsuficienteException;

public class Produto {

    private Long id;
    private String nome;
    private String descricao;
    private Double preco;
    private Integer estoque;

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
