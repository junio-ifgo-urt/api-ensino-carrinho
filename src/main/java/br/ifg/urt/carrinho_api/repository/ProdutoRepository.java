package br.ifg.urt.carrinho_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.ifg.urt.carrinho_api.model.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    // Apenas com essa linha, você já tem: save(), findById(), findAll(), delete()...
}
