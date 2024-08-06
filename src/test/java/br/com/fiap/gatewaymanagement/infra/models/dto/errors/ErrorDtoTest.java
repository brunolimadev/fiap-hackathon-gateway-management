package br.com.fiap.gatewaymanagement.infra.models.dto.errors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import br.com.fiap.gatewaymanagement.infra.models.dtos.errors.ErrorDto;
import br.com.fiap.gatewaymanagement.infra.models.dtos.errors.ErrorFieldDto;

import java.util.Arrays;
import java.util.List;

public class ErrorDtoTest {

    @Test
    void testErrorDto() {
        String title = "Error Title";
        String message = "Error Message";
        String code = "ERROR_CODE";
        List<ErrorFieldDto> fields = Arrays.asList(
                new ErrorFieldDto("field1", "Field 1 error"),
                new ErrorFieldDto("field2", "Field 2 error"));

        ErrorDto errorDto = new ErrorDto(title, message, code, fields);

        Assertions.assertEquals(title, errorDto.title());
        Assertions.assertEquals(message, errorDto.message());
        Assertions.assertEquals(code, errorDto.code());
        Assertions.assertEquals(fields, errorDto.fields());
    }
}