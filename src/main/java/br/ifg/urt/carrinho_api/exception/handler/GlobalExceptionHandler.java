package br.ifg.urt.carrinho_api.exception.handler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.ifg.urt.carrinho_api.exception.ExceptionResponse;
import jakarta.validation.ConstraintViolationException;
import br.ifg.urt.carrinho_api.exception.EstoqueInsuficienteException;
import br.ifg.urt.carrinho_api.exception.ResourceNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // Erros de Validação em Objetos/DTOs (@Valid @RequestBody)
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, 
            HttpHeaders headers, 
            HttpStatusCode status, 
            WebRequest request) {

        List<String> listaDeErros = ex.getBindingResult().getFieldErrors().stream()
            .map(field -> field.getField() + ": " + field.getDefaultMessage())
            .toList();
            
        String detalhesErro = String.join("\n", listaDeErros);

        ExceptionResponse exceptionResponse = new ExceptionResponse(
                LocalDateTime.now(),
                "Erro de validação: " + listaDeErros.size() + " campo(s) inválido(s)",
                detalhesErro);

        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    // Erros de Validação em Parâmetros (@RequestParam, @PathVariable)
    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<ExceptionResponse> handleConstraintViolation(
        ConstraintViolationException ex, WebRequest request) {
        
        String mensagem = ex.getMessage();
        if (mensagem.contains(": ")) {
            mensagem = mensagem.split(": ")[1];
        }

        ExceptionResponse exceptionResponse = new ExceptionResponse(
            LocalDateTime.now(),
            "Erro de validação no parâmetro",
            mensagem);

        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    // Tratamento Genérico para Recursos Não Encontrados (404)
    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> handleNotFoundExceptions(
            Exception ex, WebRequest request) {
        
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    // Regras de Negócio Específicas (400 - Bad Request)
    // Captura tanto erro de estoque quanto argumentos ilegais
    @ExceptionHandler({
        EstoqueInsuficienteException.class, 
        IllegalArgumentException.class,
        IllegalStateException.class
    })
    public final ResponseEntity<ExceptionResponse> handleBusinessLogicExceptions(
            Exception ex, WebRequest request) {
        
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    // Erro Genérico (500) 
    // Captura qualquer outra exceção não tratada explicitamente
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleAllExceptions(
            Exception ex, WebRequest request) {
        
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                LocalDateTime.now(),
                "Ocorreu um erro interno no servidor.",
                request.getDescription(false));

        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}