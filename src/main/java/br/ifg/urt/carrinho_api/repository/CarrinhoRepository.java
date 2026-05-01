package br.ifg.urt.carrinho_api.repository;

import br.ifg.urt.carrinho_api.model.Carrinho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarrinhoRepository extends JpaRepository<Carrinho, Long> {

    /**
     * Busca o carrinho de um cliente específico.
     * Como um cliente geralmente tem apenas um carrinho "aberto" ou ativo,
     * este método é essencial para o fluxo de checkout.
     */
    Optional<Carrinho> findByClienteId(Long clienteId);

    /**
     * Busca um carrinho carregando seus itens (JOIN FETCH) para evitar 
     * o problema de N+1 consultas (Performance).
     */
    @Query("SELECT c FROM Carrinho c LEFT JOIN FETCH c.itens WHERE c.id = :id")
    Optional<Carrinho> findByIdWithItems(@Param("id") Long id);
}
