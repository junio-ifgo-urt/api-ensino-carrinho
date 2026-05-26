package br.ifg.urt.carrinho_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CarrinhoApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarrinhoApiApplication.class, args);
	}

}
