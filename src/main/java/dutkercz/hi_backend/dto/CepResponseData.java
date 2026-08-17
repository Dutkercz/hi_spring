package dutkercz.hi_backend.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public record CepResponseData(
        @JsonAlias("cep")
        String zipCode,
        @JsonAlias("logradouro")
        String street,
        @JsonAlias("localidade")
        String city,
        @JsonAlias("estado")
        String state) {
}
