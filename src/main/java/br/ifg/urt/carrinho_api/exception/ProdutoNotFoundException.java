package br.ifg.urt.carrinho_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProdutoNotFoundException extends RuntimeException {
    // Construtor para mensagens especificas de ID
    public ProdutoNotFoundException(Long id) {
        super(String.format("Produto com ID %d não foi encontrado no sistema.", id));
    }
    
    // Construtor alternativo para mensagens genéricas
    public ProdutoNotFoundException(String message) {
        super(message);
    }
}
