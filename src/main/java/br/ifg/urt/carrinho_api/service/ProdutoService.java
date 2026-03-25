package br.ifg.urt.carrinho_api.service;

import java.util.List;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ifg.urt.carrinho_api.dto.request.ProdutoRequestDTO;
import br.ifg.urt.carrinho_api.dto.response.ProdutoEstoqueResponseDTO;
import br.ifg.urt.carrinho_api.dto.response.ProdutoInventarioDTO;
import br.ifg.urt.carrinho_api.dto.response.ProdutoResponseDTO;
import br.ifg.urt.carrinho_api.mapper.ProdutoMapper; // Import do Mapper
import br.ifg.urt.carrinho_api.model.Produto;
import br.ifg.urt.carrinho_api.repository.ProdutoRepository;


@Service
public class ProdutoService {

    private static final Logger logger = Logger.getLogger(ProdutoService.class.getName());

    private final ProdutoRepository repository;
    private final ProdutoMapper mapper; // Injetando o Mapper

    // Construtor atualizado com o Mapper
    public ProdutoService(ProdutoRepository repository, ProdutoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public ProdutoResponseDTO findById(Long id) {
        logger.info("Buscando produto ID: " + id);
        Produto produto = repository.findByIdOrThrow(id);
        return mapper.toResponseDTO(produto); // Uso do Mapper
    }

    public List<ProdutoResponseDTO> findAll() {
        logger.info("Listando todos os produtos.");
        List<Produto> produtos = repository.findAll();
        // O MapStruct resolve a lista inteira de uma vez
        return mapper.toResponseDTOList(produtos);
    }

    @Transactional
    public ProdutoResponseDTO create(ProdutoRequestDTO dto) {
        logger.info("Criando novo produto: " + dto.nome());
        
        // Converte RequestDTO -> Entity via Mapper
        Produto novoProduto = mapper.toEntity(dto);
        
        Produto salvo = repository.save(novoProduto);
        return mapper.toResponseDTO(salvo);
    }

    @Transactional
    public ProdutoResponseDTO update(Long id, ProdutoRequestDTO dto) {
        logger.info("Atualizando produto ID: " + id);
        // Verifica se o produto existe (lança 404 se não)
        repository.findByIdOrThrow(id);
        
        // Converte o DTO e seta o ID da URL manualmente
        Produto produtoParaAtualizar = mapper.toEntity(dto);
        produtoParaAtualizar.setId(id); 
        
        Produto atualizado = repository.save(produtoParaAtualizar);
        return mapper.toResponseDTO(atualizado);
    }

    public void delete(Long id) {
        logger.info("Removendo produto ID: " + id);
        Produto existing = repository.findByIdOrThrow(id);
        repository.delete(existing);
    }

    // Método para obter apenas o estoque (DTO específico)
    public ProdutoEstoqueResponseDTO getEstoque(Long id) {
        logger.info("Consultando saldo de estoque do produto ID: " + id);
        Produto produto = repository.findByIdOrThrow(id);
        return mapper.toEstoqueDTO(produto); // Reaproveitando o mapeamento!
    }

    // Método para obter relatório de inventário (DTO com campo calculado)
    public ProdutoInventarioDTO getRelatorioInventario(Long id) {
        Produto produto = repository.findByIdOrThrow(id);
        return mapper.toInventarioDTO(produto);
    }

    @Transactional 
    public ProdutoEstoqueResponseDTO baixarEstoque(Long id, Integer qtd) {
        logger.info("Baixa de estoque ID: " + id + " | Qtd: " + qtd);
        
        Produto p = repository.findByIdOrThrow(id);
        p.baixarEstoque(qtd); 
        
        Produto produtoAtualizado = repository.save(p);
        
        // Agora usamos o novo método do mapper
        return mapper.toEstoqueDTO(produtoAtualizado);
    }

    @Transactional
    public void aplicarDescontoGlobal(Long id, Double percentual) {
        Produto produto = repository.findByIdOrThrow(id);
        
        // MUDANÇA NO SERVICE:
        // Em vez de fazer: produto.setPreco(produto.getPreco() * 0.9);
        // O Service delega a regra de negócio para a Entidade/VO
        produto.atualizarPrecoComPromocao(percentual);
        
        repository.save(produto);
    }

    
}