package br.ifg.urt.carrinho_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("Shopping Cart & Product Catalog API")
                .version("v1")
                .description("API REST para gerenciamento de carrinho de compras e catálogo de produtos. " +
                             "Permite operações de CRUD para o modelo de Produtos e controle de itens no carrinho.")
                .termsOfService("https://ifgoiano.edu.br/urutai/termos")
                .license(apiLicense());
    }

    private License apiLicense() {
        return new License()
                .name("Apache 2.0")
                .url("https://www.apache.org/licenses/LICENSE-2.0.html");
    }
}
