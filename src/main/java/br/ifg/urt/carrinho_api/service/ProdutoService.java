package br.ifg.urt.carrinho_api.service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

import br.ifg.urt.carrinho_api.mock.ProdutoMock;
import br.ifg.urt.carrinho_api.model.Produto;

@Service
public class ProdutoService {

    // Logger padrão do Java
    private static final Logger logger = Logger.getLogger(ProdutoService.class.getName());

    /// Inicia o contador com o tamanho da lista mockada para evitar colisão de IDs
    AtomicLong counter = new AtomicLong(ProdutoMock.createList().size()); 

    // Lista simulando o banco de dados
     private final List<Produto> produtos = ProdutoMock.createList();

    public Produto findById(Long id) {
        logger.info("Buscando produto com ID: " + id);

        return produtos.stream()
            .filter(p -> p.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> {
                logger.warning("Produto ID " + id + " não encontrado.");
                return new RuntimeException("Produto não encontrado");
            });
    }

    public List<Produto> findAll() {
        logger.info("Buscando todos os produtos. Total: " + produtos.size());
        return produtos;
    }

    public Produto create(Produto produto) {
        logger.info("Criando um  produto!");
        produto.setId(counter.incrementAndGet());
        produtos.add(produto);
        return produto;
    }

    public Produto update(Produto produto) {
        logger.info("Atualizando um produto!");

        Produto existing = findById(produto.getId());
        existing.setNome(produto.getNome());
        existing.setPreco(produto.getPreco());
        existing.setDescricao(produto.getDescricao());
        existing.setEstoque(produto.getEstoque());
        return existing;
    }

    public void delete(Long id) {
        logger.info("Removendo um produto!");
        Produto existing = findById(id);
        produtos.remove(existing);
    }

    public void baixarEstoque(Long id, Integer qtd) {
        Produto p = findById(id);
        logger.info("Tentativa de baixa no estoque: Produto " + p.getNome() + " | Qtd: " + qtd);
        
        // Chama a lógica de negócio que criamos no Model
        p.baixarEstoque(qtd); 
        
        logger.info("Estoque atualizado: " + p.getNome() + " agora possui " + p.getEstoque() + " unidades.");
    }
}
