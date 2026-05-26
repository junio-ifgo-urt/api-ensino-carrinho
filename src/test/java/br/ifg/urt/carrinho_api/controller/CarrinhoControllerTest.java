package br.ifg.urt.carrinho_api.controller;

// 1. Imports de Teste (JUnit e Mockito)
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

// 2. Imports do Spring Test e MockMvc (Static para facilitar a escrita)
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// 3. Imports de Configuração do Spring Boot
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean; // No lugar de MockBean
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.hateoas.EntityModel;

// 4. Utilitários e Classes do Projeto
import com.fasterxml.jackson.databind.ObjectMapper;
import br.ifg.urt.carrinho_api.assembler.CarrinhoModelAssembler;
import br.ifg.urt.carrinho_api.dto.carrinho.CarrinhoResponseDTO;
import br.ifg.urt.carrinho_api.dto.carrinho.ItemCarrinhoRequestDTO;
import br.ifg.urt.carrinho_api.exception.ResourceNotFoundException;
import br.ifg.urt.carrinho_api.service.CarrinhoService;

@WebMvcTest(CarrinhoController.class)
@Import(ObjectMapper.class) // Importa o ObjectMapper para converter objetos em JSON
class CarrinhoControllerTest {

    @Autowired
    private MockMvc mockMvc; // Simula requisições HTTP

    @MockitoBean
    private CarrinhoService service; // Mock do Service

    @MockitoBean
    private CarrinhoModelAssembler assembler; // Mock do Assembler (HATEOAS)

    @Autowired
    private ObjectMapper objectMapper; // Converte objetos para JSON

    @Test
    @DisplayName("Deve adicionar produto com sucesso e retornar 200")
    void deveAdicionarProdutoComSucesso() throws Exception {
        // Arrange
        // Criamos o DTO que o usuário enviaria no corpo da requisição (JSON)
        ItemCarrinhoRequestDTO requestDTO = new ItemCarrinhoRequestDTO(1L, 10L, 2);

        // Criamos o DTO de resposta que o Service devolveria
        CarrinhoResponseDTO responseDTO = new CarrinhoResponseDTO(1L, 1L, "João", null, null, 200.0);

        // Configuramos o Mock do Service
        when(service.adicionarProduto(any(ItemCarrinhoRequestDTO.class))).thenReturn(responseDTO);
        
        // Configuramos o Mock do Assembler (HATEOAS) para envolver o DTO em um EntityModel
        when(assembler.toModel(responseDTO)).thenReturn(EntityModel.of(responseDTO));

        // Act & Assert (Usando o padrão AAA dentro do MockMvc)
        mockMvc.perform(post("/carrinhos/adicionar") // Define o método e URL
                .contentType(MediaType.APPLICATION_JSON) // Diz que estamos enviando JSON
                .content(objectMapper.writeValueAsString(requestDTO))) // Converte o DTO para String JSON
                .andExpect(status().isOk()) // Assert: HTTP 200
                .andExpect(jsonPath("$.clienteNome").value("João")) // Assert: Verifica dado no JSON
                .andExpect(jsonPath("$.total").value(200.0)); // Assert: Verifica valor
    }

    @Test
    @DisplayName("Deve retornar 404 quando o cliente ou produto não existem")
    void deveRetornar404QuandoNaoEncontrado() throws Exception {
        // Arrange
        ItemCarrinhoRequestDTO requestDTO = new ItemCarrinhoRequestDTO(99L, 1L, 1);
        
        // Simula o Service lançando a exceção
        when(service.adicionarProduto(any()))
            .thenThrow(new ResourceNotFoundException("Cliente não encontrado"));

        // Act & Assert
        mockMvc.perform(post("/carrinhos/adicionar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound()); // Verifica se o Spring converteu a Exception em 404
    }

    @Test
    @DisplayName("Deve retornar 204 ao remover produto")
    void deveRetornar204AoRemoverProduto() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/carrinhos/cliente/1/remover-produto/10"))
                .andExpect(status().isNoContent()); // Espera HTTP 204
        // Verificação adicional do Mockito para garantir que o service foi chamado
        verify(service).removerProduto(1L, 10L);
    }

    @Test
    @DisplayName("Deve retornar 400 quando quantidade for inválida")
    void deveRetornar400QuandoQuantidadeForInvalida() throws Exception {
        // Arrange
        ItemCarrinhoRequestDTO requestDTO =
                new ItemCarrinhoRequestDTO(1L, 10L, 0);

        // Act & Assert
        mockMvc.perform(post("/carrinhos/adicionar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());

        // Verifica que o service nem chegou a ser chamado
        verify(service, never()).adicionarProduto(any());
    }
}
