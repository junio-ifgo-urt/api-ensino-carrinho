package br.ifg.urt.carrinho_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.ifg.urt.carrinho_api.dto.carrinho.AtualizarQuantidadeItemDTO;
import br.ifg.urt.carrinho_api.dto.carrinho.CarrinhoResponseDTO;
import br.ifg.urt.carrinho_api.dto.carrinho.ItemCarrinhoRequestDTO;
import br.ifg.urt.carrinho_api.exception.ResourceNotFoundException;
import br.ifg.urt.carrinho_api.mapper.CarrinhoMapper;
import br.ifg.urt.carrinho_api.model.Carrinho;
import br.ifg.urt.carrinho_api.model.Cliente;
import br.ifg.urt.carrinho_api.model.Produto;
import br.ifg.urt.carrinho_api.mother.ProdutoMother;
import br.ifg.urt.carrinho_api.repository.CarrinhoRepository;
import br.ifg.urt.carrinho_api.repository.ClienteRepository;
import br.ifg.urt.carrinho_api.repository.ProdutoRepository;

@ExtendWith(MockitoExtension.class)
class CarrinhoServiceTest {
    // O Service que estamos testando, com as dependências injetadas via @InjectMocks
    @InjectMocks
    private CarrinhoService service;
    // Mocks para os repositórios e o mapper, que serão injetados no Service
    @Mock 
    private CarrinhoRepository carrinhoRepository;
    @Mock
    private ProdutoRepository produtoRepository;
    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private CarrinhoMapper carrinhoMapper;

    @Test
    @DisplayName("Deve adicionar produto em um novo carrinho")
    void deveAdicionarProdutoEmNovoCarrinho() {
        // Arrange
        Produto produto = ProdutoMother.mouseGamer();
        ItemCarrinhoRequestDTO dto = new ItemCarrinhoRequestDTO(1L, produto.getId(), 2);
        Carrinho carrinhoSalvo = new Carrinho();
        // Simula o comportamento dos repositórios e do mapper
        when(clienteRepository.findByIdOrThrow(1L)).thenReturn(new Cliente());
        when(produtoRepository.findByIdOrThrow(produto.getId())).thenReturn(produto);
        when(carrinhoRepository.findByClienteId(1L)).thenReturn(Optional.empty());
        when(carrinhoRepository.save(any(Carrinho.class))).thenReturn(carrinhoSalvo);

        // Act
        // O método que estamos testando
        service.adicionarProduto(dto);

        // Assert
        // Verifica se o carrinho foi salvo no repositório
        verify(carrinhoRepository).save(any(Carrinho.class));
        // Verifica se o mapper foi chamado com o carrinho salvo
        verify(carrinhoMapper).toResponseDTO(carrinhoSalvo);
    }

    @Test
    @DisplayName("Deve interromper execução quando cliente não existe (Fail Fast)")
    void deveLancarExcecaoQuandoClienteNaoExiste() {
        // Arrange
        ItemCarrinhoRequestDTO dto = new ItemCarrinhoRequestDTO(99L, 1L, 1);
        
        when(clienteRepository.findByIdOrThrow(99L))
            .thenThrow(new ResourceNotFoundException("Cliente não encontrado"));

        // Act + Assert
        assertThrows(ResourceNotFoundException.class, () -> service.adicionarProduto(dto));

        // Verifica se o código parou no primeiro erro e não chamou os próximos repositórios
        verifyNoInteractions(produtoRepository);
        verify(carrinhoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve atualizar quantidade do item no carrinho")
    void deveAtualizarQuantidadeDoItemNoCarrinho() {
        // Arrange
        Produto produto = ProdutoMother.monitor();
        Carrinho carrinho = new Carrinho();
        carrinho.adicionarItem(produto, 2);
        
        AtualizarQuantidadeItemDTO dto = new AtualizarQuantidadeItemDTO(produto.getId(), 10);

        when(carrinhoRepository.findByClienteId(1L)).thenReturn(Optional.of(carrinho));
        when(produtoRepository.findByIdOrThrow(produto.getId())).thenReturn(produto);
        when(carrinhoRepository.save(carrinho)).thenReturn(carrinho);

        // Act
        service.atualizarQuantidade(1L, dto);

        // Assert
        verify(carrinhoRepository).save(carrinho);
        // Verifica se a lógica da Model foi executada (2 virou 10)
        assertEquals(10, carrinho.getItens().get(0).getQuantidade());
    }

    @Test
    @DisplayName("Deve remover produto do carrinho")
    void deveRemoverProdutoDoCarrinho() {
        // Arrange
        Produto produto = ProdutoMother.notebookGamer();
        Carrinho carrinho = new Carrinho();
        carrinho.adicionarItem(produto, 1);

        when(carrinhoRepository.findByClienteId(1L)).thenReturn(Optional.of(carrinho));
        when(produtoRepository.findByIdOrThrow(produto.getId())).thenReturn(produto);

        // Act
        service.removerProduto(1L, produto.getId());

        // Assert
        verify(carrinhoRepository).save(carrinho);
        assertTrue(carrinho.getItens().isEmpty());
    }
}