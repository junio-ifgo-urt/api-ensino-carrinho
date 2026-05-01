package br.ifg.urt.carrinho_api.service;

import br.ifg.urt.carrinho_api.dto.carrinho.AtualizarQuantidadeItemDTO;
import br.ifg.urt.carrinho_api.dto.carrinho.CarrinhoResponseDTO;
import br.ifg.urt.carrinho_api.dto.carrinho.ItemCarrinhoRequestDTO;
import br.ifg.urt.carrinho_api.exception.ResourceNotFoundException;
import br.ifg.urt.carrinho_api.mapper.CarrinhoMapper;
import br.ifg.urt.carrinho_api.model.Carrinho;
import br.ifg.urt.carrinho_api.model.Cliente;
import br.ifg.urt.carrinho_api.model.Produto;
import br.ifg.urt.carrinho_api.repository.CarrinhoRepository;
import br.ifg.urt.carrinho_api.repository.ClienteRepository;
import br.ifg.urt.carrinho_api.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CarrinhoService {

    private final CarrinhoRepository carrinhoRepository;
    private final ProdutoRepository produtoRepository;
    private final ClienteRepository clienteRepository;
    private final CarrinhoMapper carrinhoMapper;

    // Construtor manual para Injeção de Dependência
    // O Spring detecta automaticamente este construtor e injeta os Beans necessários
    public CarrinhoService(
            CarrinhoRepository carrinhoRepository,
            ProdutoRepository produtoRepository,
            ClienteRepository clienteRepository,
            CarrinhoMapper carrinhoMapper) {
        this.carrinhoRepository = carrinhoRepository;
        this.produtoRepository = produtoRepository;
        this.clienteRepository = clienteRepository;
        this.carrinhoMapper = carrinhoMapper;
    }

    @Transactional
    public CarrinhoResponseDTO adicionarProduto(ItemCarrinhoRequestDTO dto) {
        // 1. Validar se o cliente existe
        Cliente cliente = clienteRepository.findByIdOrThrow(dto.clienteId());

        // 2. Validar se o produto existe
        Produto produto = produtoRepository.findByIdOrThrow(dto.produtoId());

        // 3. Buscar o carrinho ou criar um novo
        Carrinho carrinho = carrinhoRepository.findByClienteId(dto.clienteId())
                .orElseGet(() -> {
                    Carrinho novoCarrinho = new Carrinho();
                    novoCarrinho.setCliente(cliente);
                    return novoCarrinho;
                });

        // 4. Executar regra de negócio no Model
        carrinho.adicionarItem(produto, dto.quantidade());

        // 5. Salvar e converter via Mapper
        Carrinho salvo = carrinhoRepository.save(carrinho);
        return carrinhoMapper.toResponseDTO(salvo);
    }

    @Transactional
    public CarrinhoResponseDTO atualizarQuantidade(Long clienteId, AtualizarQuantidadeItemDTO dto) {
        // 1. Busca o carrinho
        Carrinho carrinho = carrinhoRepository.findByClienteId(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Carrinho não encontrado para o cliente: " + clienteId));

        // 2. Busca o produto
        Produto produto = produtoRepository.findByIdOrThrow(dto.produtoId());

        // 3. Executa a atualização no Model
        carrinho.atualizarQuantidadeItem(produto, dto.quantidade());

        // 4. Salvar e converter via Mapper
        Carrinho salvo = carrinhoRepository.save(carrinho);
        return carrinhoMapper.toResponseDTO(salvo);
    }

    @Transactional
    public void removerProduto(Long clienteId, Long produtoId) {
        Carrinho carrinho = carrinhoRepository.findByClienteId(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Carrinho não encontrado para o cliente ID: " + clienteId));

        Produto produto = produtoRepository.findByIdOrThrow(produtoId);

        carrinho.removerItem(produto);
        
        carrinhoRepository.save(carrinho);
    }

    @Transactional(readOnly = true)
    public CarrinhoResponseDTO buscarPorCliente(Long clienteId) {
        Carrinho carrinho = carrinhoRepository.findByClienteId(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Carrinho não encontrado para o cliente: " + clienteId));
        
        return carrinhoMapper.toResponseDTO(carrinho);
    }
}
