package br.ifg.urt.carrinho_api.exception.handler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
import br.ifg.urt.carrinho_api.exception.ProdutoNotFoundException;
import jakarta.validation.ConstraintViolationException;
import br.ifg.urt.carrinho_api.exception.EstoqueInsuficienteException;

@RestControllerAdvice // Substitui o uso de @ControllerAdvice + @RestController
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // sobreescreve o método padrão do Spring para Validação (400)
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, 
            HttpHeaders headers, 
            HttpStatusCode status, 
            WebRequest request) {

        // Criando uma lista de strings mais organizada
        List<String> listaDeErros = ex.getBindingResult().getFieldErrors().stream()
            .map(field -> field.getField() + ": " + field.getDefaultMessage())
            .toList();
        // Junta os erros em uma única string, separados por nova linha    
        String detalhesErro = String.join("\n", listaDeErros);

        ExceptionResponse exceptionResponse = new ExceptionResponse(
                LocalDateTime.now(),
                "Erro de validação: " + listaDeErros.size() + " campo(s) inválido(s)",
                detalhesErro);

        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    // Tratamento para erros de validação em @RequestParam e @PathVariable
    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<ExceptionResponse> handleConstraintViolation(
        ConstraintViolationException ex, WebRequest request) {
        // ex.getMessage() retornará algo como: "baixarEstoque.quantidade: A quantidade mínima para baixa é 1 unidade"
        String mensagemLimpa = ex.getMessage();

        // Se você quiser limpar o nome do método e deixar só a mensagem:
        if (mensagemLimpa.contains(": ")) {
            mensagemLimpa = mensagemLimpa.split(": ")[1];
        }

        ExceptionResponse exceptionResponse = new ExceptionResponse(
            LocalDateTime.now(),
            "Erro de validação no parâmetro",
            mensagemLimpa); // Agora aparecerá apenas: "A quantidade mínima para baixa é 1 unidade"

        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    // Tratamento para Recurso Não Encontrado (404)
    @ExceptionHandler(ProdutoNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> handleNotFoundExceptions(
            Exception ex, WebRequest request) {
        
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    // Tratamento para erro de Estoque (Regra de Negócio)
    @ExceptionHandler(EstoqueInsuficienteException.class)
    public final ResponseEntity<ExceptionResponse> handleEstoqueExceptions(
            Exception ex, WebRequest request) {
        
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)); // Retorna a URI do erro

        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    // Tratamento para erros de Argumentos Inválidos (Ex: Quantidade Negativa)
    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity<ExceptionResponse> handleBadRequestExceptions(
            IllegalArgumentException ex, WebRequest request) {
        
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                LocalDateTime.now(),
                ex.getMessage(), // "A quantidade para baixar deve ser maior que zero."
                request.getDescription(false));

        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    // Tratamento para Exceções Gerais (Erro 500)
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleAllExceptions(
            Exception ex, WebRequest request) {
        
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                LocalDateTime.now(),
                "Ocorreu um erro interno no servidor.",
                request.getDescription(false));

        // Em ambiente de desenvolvimento, você pode logar o stack trace:
        // ex.printStackTrace();

        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}