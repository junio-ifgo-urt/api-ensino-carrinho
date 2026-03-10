package br.ifg.urt.carrinho_api.service;

import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ifg.urt.carrinho_api.model.Produto;
import br.ifg.urt.carrinho_api.repository.ProdutoRepository;

@Service
public class ProdutoService {

    private static final Logger logger = Logger.getLogger(ProdutoService.class.getName());

    // 1. Declarar como final
    private final ProdutoRepository repository;

    // 2. Injeção explícita via construtor (O Spring injeta automaticamente se houver apenas um construtor)
    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    /**
     * Utiliza o método default do Repository para simplificar a busca.
     * Note como o log de erro agora é centralizado pela exceção específica.
     */
    public Produto findById(Long id) {
        logger.info("Buscando produto no banco com ID: " + id);
        return repository.findByIdOrThrow(id);
    }

    public List<Produto> findAll() {
        logger.info("Buscando todos os produtos no banco.");
        return repository.findAll();
    }

    public Produto create(Produto produto) {
        logger.info("Salvando novo produto no banco: " + produto.getNome());
        // O ID agora é gerado pelo MySQL/JPA automaticamente
        return repository.save(produto);
    }

    @Transactional
    public Produto update(Produto produto) {
        logger.info("Atualizando produto ID: " + produto.getId());

        // Verificamos se existe antes de atualizar
        Produto existing = repository.findByIdOrThrow(produto.getId());
        
        existing.setNome(produto.getNome());
        existing.setPreco(produto.getPreco());
        existing.setDescricao(produto.getDescricao());
        existing.setEstoque(produto.getEstoque());
        
        // No JPA, o save() serve para criar ou atualizar
        return repository.save(existing);
    }

    public void delete(Long id) {
        logger.info("Removendo produto ID: " + id);
        // Garante a existência antes de tentar deletar
        Produto existing = repository.findByIdOrThrow(id);
        repository.delete(existing);
    }

    // @Transactional garante que, se algo der errado, o banco de dados voltará ao estado 
    // anterior (Rollback), evitando dados corrompidos
    @Transactional 
    public Produto baixarEstoque(Long id, Integer qtd) {
        // Fluxo limpo: Busca -> Processa Regra no Modelo -> Persiste
        Produto p = repository.findByIdOrThrow(id);
        
        logger.info("Baixa de estoque: " + p.getNome() + " | Qtd: " + qtd);
        // A lógica de negócio reside na Entidade (Modelo Rico)
        p.baixarEstoque(qtd); 
        // Salvamos o produto atualizado no banco de dados
        Produto produtoAtualizado = repository.save(p);
        // O log de sucesso é registrado após a confirmação da atualização no banco, garantindo que o estoque foi realmente atualizado
        logger.info("Estoque atualizado com sucesso no banco de dados. Novo estoque: " + produtoAtualizado.getEstoque());
        // retorna o produto atualizado para o controller, que por sua vez retorna o JSON atualizado para o cliente
        return produtoAtualizado;
    }


}
