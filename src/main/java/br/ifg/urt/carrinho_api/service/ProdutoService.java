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

    @Autowired
    private ProdutoRepository repository; // Agora usamos o Repository real

    public Produto findById(Long id) {
        logger.info("Buscando produto no banco com ID: " + id);

        return repository.findById(id)
            .orElseThrow(() -> {
                logger.warning("Produto ID " + id + " não encontrado no banco.");
                return new RuntimeException("Produto não encontrado");
            });
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
        Produto existing = findById(produto.getId());
        
        existing.setNome(produto.getNome());
        existing.setPreco(produto.getPreco());
        existing.setDescricao(produto.getDescricao());
        existing.setEstoque(produto.getEstoque());
        
        // No JPA, o save() serve para criar ou atualizar
        return repository.save(existing);
    }

    public void delete(Long id) {
        logger.info("Removendo produto ID: " + id);
        Produto existing = findById(id);
        repository.delete(existing);
    }

    @Transactional // Importante: Garante que a operação de baixa seja segura
    public void baixarEstoque(Long id, Integer qtd) {
        Produto p = findById(id);
        logger.info("Baixa de estoque no banco: " + p.getNome() + " | Qtd: " + qtd);
        
        p.baixarEstoque(qtd); 
        
        // Ao salvar um objeto já existente, o JPA executa um UPDATE
        repository.save(p);
        
        logger.info("Estoque atualizado no banco de dados.");
    }
}
