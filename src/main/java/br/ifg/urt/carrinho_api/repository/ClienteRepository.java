package br.ifg.urt.carrinho_api.repository;

import br.ifg.urt.carrinho_api.exception.ResourceNotFoundException;
import br.ifg.urt.carrinho_api.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    // Método útil para validar se um e-mail já está cadastrado
    Optional<Cliente> findByEmail(String email);

    // Método auxiliar para lançar exceção caso não encontre
    default Cliente findByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com o ID: " + id));
    }
}
