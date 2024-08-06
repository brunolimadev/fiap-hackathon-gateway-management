package br.com.fiap.gatewaymanagement.infra.models.dto.errors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import br.com.fiap.gatewaymanagement.infra.models.dtos.errors.ErrorFieldDto;

public class ErrorFieldDtoTest {

    @Test
    void testErrorFieldDto() {
        String field = "fieldName";
        String message = "errorMessage";

        ErrorFieldDto errorFieldDto = new ErrorFieldDto(field, message);

        Assertions.assertEquals(field, errorFieldDto.field());
        Assertions.assertEquals(message, errorFieldDto.message());
    }
}