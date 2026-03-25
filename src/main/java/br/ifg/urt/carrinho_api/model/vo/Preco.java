package br.ifg.urt.carrinho_api.model.vo;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Column;

/**
 * Exemplo de uso de Value Object (VO) para encapsular lógica de negócio
 * dentro de uma Entidade, mantendo DTOs para transferência.
 */

// 1. O VALUE OBJECT (O "Coração" do Domínio)
// Representa um conceito (Preço) e não apenas um número.
@Embeddable // Permite que o JPA grave os campos deste VO na mesma tabela do Produto
public record Preco(
    @Column(name = "preco_valor", nullable = false)
    Double valor,
    
    @Column(name = "preco_moeda", length = 3)
    String moeda
) {
    // O VO se autovalida na criação
    public Preco {
        if (valor == null || valor < 0) {
            throw new IllegalArgumentException("O valor do preço não pode ser negativo");
        }
        if (moeda == null || moeda.isBlank()) {
            moeda = "BRL"; // Default
        }
    }

    // O VO pode ter lógica própria (ex: formatar para exibição)
    public String getFormatado() {
        return String.format("%s %.2f", moeda, valor);
    }
    
    // O VO pode ter lógica de conversão (ex: se fosse multimoedas)
    public Preco aplicarDesconto(Double percentual) {
        return new Preco(this.valor * (1 - percentual/100), this.moeda);
    }
}