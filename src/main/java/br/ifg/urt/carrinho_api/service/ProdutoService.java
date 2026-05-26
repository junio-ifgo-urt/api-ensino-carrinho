package br.ifg.urt.carrinho_api.service;

import java.util.logging.Logger;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ifg.urt.carrinho_api.dto.produto.ProdutoRequestDTO;
import br.ifg.urt.carrinho_api.dto.produto.ProdutoEstoqueResponseDTO;
import br.ifg.urt.carrinho_api.dto.produto.ProdutoInventarioDTO;
import br.ifg.urt.carrinho_api.dto.produto.ProdutoResponseDTO;
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

    // Busca produto por ID - resultado é cacheado para otimizar consultas frequentes
    @Cacheable(value = "produtos", key = "#id")
    public ProdutoResponseDTO findById(Long id) {
        logger.info("Buscando produto ID: " + id);
        Produto produto = repository.findByIdOrThrow(id);
        return mapper.toResponseDTO(produto); // Uso do Mapper
    }

    // Busca todos os produto - caso tenha nome no parâmetro é feito a filtragem
    // O Cacheable é usado para otimizar consultas frequentes de listagem de produtos, especialmente com filtros e paginação
    @Cacheable(value = "produtosPaginados", 
               key = "{ #nome, #pageable.pageNumber, #pageable.pageSize, #pageable.sort }")
    public Page<ProdutoResponseDTO> findAll(String nome, Pageable pageable) {
        Page<Produto> pagina;

        if (nome != null && !nome.isBlank()) {
            pagina = repository.findByNomeContainingIgnoreCase(nome, pageable);
        } else {
            pagina = repository.findAll(pageable);
        }

        return pagina.map(mapper::toResponseDTO);
    }

    @CacheEvict(value = "produtosPaginados", allEntries = true)
    @Transactional
    public ProdutoResponseDTO create(ProdutoRequestDTO dto) {
        logger.info("Criando novo produto: " + dto.nome());
        
        // Converte RequestDTO -> Entity via Mapper
        Produto novoProduto = mapper.toEntity(dto);
        
        Produto salvo = repository.save(novoProduto);
        return mapper.toResponseDTO(salvo);
    }

    // O CacheEvict é usado para limpar o cache do produto após uma atualização, garantindo que consultas futuras obtenham os dados atualizados
    @Caching(evict = {
        @CacheEvict(value = "produtos", key = "#id"),
        @CacheEvict(value = "produtosPaginados", allEntries = true)
    })
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

    // O CacheEvict é usado para limpar o cache do produto e da listagem paginada após a remoção, garantindo que consultas futuras obtenham os dados atualizados
    @Caching(evict = {
        @CacheEvict(value = "produtos", key = "#id"),
        @CacheEvict(value = "produtosPaginados", allEntries = true)
    })
    public void delete(Long id) {
        logger.info("Removendo produto ID: " + id);
        Produto existing = repository.findByIdOrThrow(id);
        repository.delete(existing);
    }

    // Método para obter apenas o estoque (DTO específico)
    // O Cacheable é usado para otimizar consultas frequentes do estoque, que podem ser feitas por outros serviços (ex: Carrinho)
    @Cacheable(value = "estoque", key = "#id")
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

    // O CacheEvict é usado para limpar o cache do produto e do estoque após a baixa, garantindo que consultas futuras obtenham os dados atualizados
    @Caching(evict = {
        @CacheEvict(value = "produtos", key = "#id"),
        @CacheEvict(value = "estoque", key = "#id"),
        @CacheEvict(value = "produtosPaginados", allEntries = true)
    })
    @Transactional 
    public ProdutoEstoqueResponseDTO baixarEstoque(Long id, Integer qtd) {
        logger.info("Baixa de estoque ID: " + id + " | Qtd: " + qtd);
        
        Produto p = repository.findByIdOrThrow(id);
        p.baixarEstoque(qtd); 
        
        Produto produtoAtualizado = repository.save(p);
        
        // Usamos o mesmo DTO de estoque para retornar a quantidade atualizada após a baixa
        return mapper.toEstoqueDTO(produtoAtualizado);
    }

    @Caching(evict = {
        @CacheEvict(value = "produtos", key = "#id"),
        @CacheEvict(value = "produtosPaginados", allEntries = true) // Garante o preço novo na lista
    })
    @Transactional
    public void aplicarDescontoGlobal(Long id, Double percentual) {
        Produto produto = repository.findByIdOrThrow(id);
        
        // O Service delega a regra de negócio para a Entidade/VO
        produto.atualizarPrecoComPromocao(percentual);
        
        repository.save(produto);
    }

    
}

