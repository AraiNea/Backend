package com.example.pizza_backend.api.controller;

import com.example.pizza_backend.exception.IdNotFoundException;
import com.example.pizza_backend.exception.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleNotFound() {
        IdNotFoundException ex = new IdNotFoundException("ID not found");
        ResponseEntity<?> response = handler.handleNotFound(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertThat(body).containsEntry("status", 404);
        assertThat(body).containsEntry("error", "Not Found Resource");
        assertThat(body).containsEntry("message", "ID not found");
        assertThat(body).containsKey("timestamp");
    }

    @Test
    void testHandleUnauthorized() {
        UnauthorizedException ex = new UnauthorizedException("Unauthorized access");
        ResponseEntity<Map<String, Object>> response = handler.handleUnauthorized(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        Map<String, Object> body = response.getBody();
        assertThat(body).containsEntry("status", 401);
        assertThat(body).containsEntry("error", "Unauthorized");
        assertThat(body).containsEntry("message", "Unauthorized access");
        assertThat(body).containsKey("timestamp");
    }

    @Test
    void testHandleNullAgg() {
        IllegalArgumentException ex = new IllegalArgumentException("Illegal argument");
        ResponseEntity<Map<String, Object>> response = handler.handleNullAgg(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Map<String, Object> body = response.getBody();
        assertThat(body).containsEntry("status", 400);
        assertThat(body).containsEntry("error", "Bad Request");
        assertThat(body).containsEntry("message", "Illegal argument");
        assertThat(body).containsKey("timestamp");
    }

    @Test
    void testHandleMultipart() {
        MultipartException ex = new MultipartException("Multipart failed");
        ResponseEntity<Map<String, Object>> response = handler.handleMultipart(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Map<String, Object> body = response.getBody();
        assertThat(body.get("message").toString()).contains("Failed to parse multipart request");
        assertThat(body).containsEntry("status", 400);
        assertThat(body).containsEntry("error", "Bad Request");
        assertThat(body).containsKey("timestamp");
    }

    @Test
    void testHandleServletRequestPart() {
        MissingServletRequestPartException ex = new MissingServletRequestPartException("part");
        ResponseEntity<Map<String, Object>> response = handler.handleServletRequestPart(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Map<String, Object> body = response.getBody();
        assertThat(body).containsEntry("status", 400);
        assertThat(body).containsEntry("error", "Bad Request");
        assertThat(body.get("message")).isEqualTo(ex.getMessage());
        assertThat(body).containsKey("timestamp");
    }

    @Test
    void testHandleHttpMediaType() {
        HttpMediaTypeNotSupportedException ex = new HttpMediaTypeNotSupportedException("Media type not supported");
        ResponseEntity<Map<String, Object>> response = handler.handleHttpMediaType(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Map<String, Object> body = response.getBody();
        assertThat(body.get("message").toString()).contains("Please use application/json");
        assertThat(body).containsEntry("status", 400);
        assertThat(body).containsEntry("error", "Bad Request");
        assertThat(body).containsKey("timestamp");
    }

    @Test
    void testHandleIO() {
        IOException ex = new IOException("IO Error");
        ResponseEntity<Map<String, Object>> response = handler.handleIO(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Map<String, Object> body = response.getBody();
        assertThat(body).containsEntry("status", 400);
        assertThat(body).containsEntry("error", "Bad Request");
        assertThat(body).containsEntry("message", "IO Error");
        assertThat(body).containsKey("timestamp");
    }

    @Test
    void testHandleJsonParse() {
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("JSON parse error");
        ResponseEntity<Map<String, Object>> response = handler.handleJsonParse(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Map<String, Object> body = response.getBody();
        assertThat(body).containsEntry("status", 400);
        assertThat(body).containsEntry("error", "Bad Request");
        assertThat(body).containsEntry("message", "Invalid JSON format or wrong data type");
        assertThat(body).containsKey("timestamp");
    }

    @Test
    void testHandleDataIntegrityViolation_foreignKey() {
        DataIntegrityViolationException ex = mock(DataIntegrityViolationException.class);
        Throwable cause = new Throwable("violates foreign key constraint");
        when(ex.getMostSpecificCause()).thenReturn(cause);

        ResponseEntity<Map<String, Object>> response = handler.handleDataIntegrityViolation(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        Map<String, Object> body = response.getBody();
        assertThat(body).containsEntry("status", 409);
        assertThat(body).containsEntry("error", "Conflict");
        assertThat(body).containsEntry("message", "Cannot delete record: it is still referenced by another table.");
        assertThat(body).containsKey("timestamp");
    }

    @Test
    void testHandleDataIntegrityViolation_other() {
        DataIntegrityViolationException ex = mock(DataIntegrityViolationException.class);
        Throwable cause = new Throwable("Some other error");
        when(ex.getMostSpecificCause()).thenReturn(cause);

        ResponseEntity<Map<String, Object>> response = handler.handleDataIntegrityViolation(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        Map<String, Object> body = response.getBody();
        assertThat(body).containsEntry("status", 409);
        assertThat(body).containsEntry("error", "Conflict");
        assertThat(body.get("message").toString()).contains("Data integrity violation");
        assertThat(body).containsKey("timestamp");
    }
}
