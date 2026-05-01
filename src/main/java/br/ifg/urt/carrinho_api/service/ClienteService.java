package br.ifg.urt.carrinho_api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ifg.urt.carrinho_api.dto.cliente.ClienteResponseDTO;
import br.ifg.urt.carrinho_api.dto.cliente.ClienteRequestDTO;
import br.ifg.urt.carrinho_api.mapper.ClienteMapper;
import br.ifg.urt.carrinho_api.model.Cliente;
import br.ifg.urt.carrinho_api.repository.ClienteRepository;

@Service
public class ClienteService {

    private final ClienteRepository repository;
    private final ClienteMapper mapper; // Injetando a interface do MapStruct

    public ClienteService(ClienteRepository repository, ClienteMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public ClienteResponseDTO salvar(ClienteRequestDTO dto) {
        // O MapStruct gera o código que faz o 'new Cliente()' e os 'setters' 
        Cliente cliente = mapper.toEntity(dto);
        
        Cliente salvo = repository.save(cliente);
        
        // Converte a entidade salva de volta para o DTO de resposta
        return mapper.toResponseDTO(salvo);
    }
}
