package br.ifg.urt.carrinho_api.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import br.ifg.urt.carrinho_api.exception.ResourceNotFoundException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "carrinhos")
public class Carrinho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    // Permite que um cliente tenha apenas um carrinho ativo, mas um carrinho pertence a um cliente
    // FetchType.LAZY para evitar carregar o cliente desnecessariamente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    // Um carrinho pode ter vários itens, mas um item pertence a um carrinho
    // orphanRemoval garante que se remover do Java, remove do Banco
    @OneToMany(mappedBy = "carrinho", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemCarrinho> itens = new ArrayList<>();

    public Carrinho() {}

    // Método para ADICIONAR (Soma à quantidade existente)
    // Ele cria um novo item se o produto ainda não estiver no carrinho, ou
    // soma à quantidade existente se já estiver
    public void adicionarItem(Produto produto, Integer quantidade) {
        // busca o item correspondente ao produto, se existir
        encontrarItemPorProduto(produto)
                .ifPresentOrElse(
                    item -> item.adicionarQuantidade(quantidade), // Soma a quantidade existente
                    () -> this.itens.add(new ItemCarrinho(this, produto, quantidade)) // Cria novo item
                );
    }

    // Método para ATUALIZAR a quantidade de um item específico (PATCH)
    public void atualizarQuantidadeItem(Produto produto, Integer novaQuantidade) {
        if (novaQuantidade == null || novaQuantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }

        encontrarItemPorProduto(produto)
                .ifPresentOrElse(
                    item -> item.atualizarQuantidadeAbsoluta(novaQuantidade), // Sobrescreve
                    () -> { 
                        throw new ResourceNotFoundException("Produto ID " + produto.getId() + " não está no carrinho."); 
                    }
                );
    }

    // Método auxiliar para encontrar um item específico por produto, usado internamente
    private Optional<ItemCarrinho> encontrarItemPorProduto(Produto produto) {
        return itens.stream()
                .filter(item -> item.getProduto().getId().equals(produto.getId()))
                .findFirst();
    }

    // Remove um produto do carrinho
    public void removerItem(Produto produto) {
        this.itens.removeIf(item -> item.getProduto().getId().equals(produto.getId()));
    }

    // Calcula o total do carrinho somando os subtotais de cada item
    public Double getTotal() {
        return itens.stream()
                .mapToDouble(ItemCarrinho::getSubtotal)
                .sum();
    }

    // Removido o setItens para proteger a integridade da lista
    public List<ItemCarrinho> getItens() {
        return Collections.unmodifiableList(itens); // Boa prática: retorna lista imutável
    }

    // Um carrinho precisa de um dono para ser processado
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public Cliente getCliente() {
        return cliente;
    }
    
}
