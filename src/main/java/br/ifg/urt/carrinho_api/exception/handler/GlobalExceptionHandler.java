package br.ifg.urt.carrinho_api.exception.handler;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.ifg.urt.carrinho_api.exception.ExceptionResponse;
import br.ifg.urt.carrinho_api.exception.ProdutoNotFoundException;
import br.ifg.urt.carrinho_api.exception.EstoqueInsuficienteException;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // Tratamento para Recurso Não Encontrado (404)
    @ExceptionHandler(ProdutoNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> handleNotFoundExceptions(
            Exception ex, WebRequest request) {
        
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    // Tratamento para erro de Estoque (Regra de Negócio)
    @ExceptionHandler(EstoqueInsuficienteException.class)
    public final ResponseEntity<ExceptionResponse> handleEstoqueExceptions(
            Exception ex, WebRequest request) {
        
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(),
                ex.getMessage(),
                request.getDescription(false)); // Retorna a URI do erro

        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    // Tratamento para erros de Argumentos Inválidos (Ex: Quantidade Negativa)
    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity<ExceptionResponse> handleBadRequestExceptions(
            IllegalArgumentException ex, WebRequest request) {
        
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(),
                ex.getMessage(), // "A quantidade para baixar deve ser maior que zero."
                request.getDescription(false));

        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    // Tratamento para Exceções Gerais (Erro 500)
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleAllExceptions(
            Exception ex, WebRequest request) {
        
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(),
                "Ocorreu um erro interno no servidor.",
                request.getDescription(false));

        // Em ambiente de desenvolvimento, você pode logar o stack trace:
        // ex.printStackTrace();

        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}