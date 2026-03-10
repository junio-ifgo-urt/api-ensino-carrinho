package br.ifg.urt.carrinho_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.ifg.urt.carrinho_api.exception.ProdutoNotFoundException;
import br.ifg.urt.carrinho_api.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    /**
     * Busca um estudante por ID ou lança uma exceção customizada.
    * @param id Identificador do estudante.
     * @return O objeto Student encontrado.
     * @throws ResourceNotFoundException Caso o ID não exista no banco.
     */
    default Produto findByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new ProdutoNotFoundException("Produto não encontrado com o ID: " + id));
    }

    // 1. Busca exata por nome
    // SELECT * FROM produtos WHERE nome = ?
    Optional<Produto> findByNome(String nome);

    // 2. Busca por parte do nome (ignorando maiúsculas/minúsculas)
    // SELECT * FROM produtos WHERE UPPER(nome) LIKE UPPER('%termo%')
    List<Produto> findByNomeContainingIgnoreCase(String nome);

    // 3. Busca produtos com preço menor que um valor
    // SELECT * FROM produtos WHERE preco < ?
    List<Produto> findByPrecoLessThan(Double preco);

    // 4. Busca produtos em estoque e com preço entre dois valores
    // SELECT * FROM produtos WHERE estoque > 0 AND preco BETWEEN ? AND ?
    List<Produto> findByEstoqueGreaterThanAndPrecoBetween(Integer minEstoque, Double precoMin, Double precoMax);

    // 5. Ordenar resultados por nome
    // SELECT * FROM produtos ORDER BY nome ASC
    List<Produto> findAllByOrderByNomeAsc();

    // 6. Consulta JPQL (usa o nome da classe e atributos, não a tabela)
    @Query("SELECT p FROM Produto p WHERE p.preco < :preco")
    List<Produto> buscarPorPreceMenorQue(@Param("preco") Double preco);

    // 7. Consulta SQL nativo (usa o nome real da tabela)
    @Query(value = "SELECT * FROM produtos WHERE estoque = 0", nativeQuery = true)
    List<Produto> buscarSemEstoque();
}