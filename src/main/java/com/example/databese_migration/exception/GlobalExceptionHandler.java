package com.example.databese_migration.exception;


import com.example.databese_migration.payload.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage(), "USER_NOT_FOUND", HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler(ErrorsException.class)
    public ResponseEntity<ApiResponse<Void>> handleErreor(ErrorsException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage(), "USER_NOT_FOUND", HttpStatus.NOT_FOUND.value()));
    }

    /*@ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneralException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erreur interne du serveur", "INTERNAL_ERROR", HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }*/


    /*@ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(HttpServletRequest request, Exception ex) {

        // Ne pas intercepter Swagger/OpenAPI
        String path = request.getRequestURI();
        if (path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui")) {
            return null; // Laisse Swagger gérer l’erreur lui-même
        }

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erreur interne du serveur", "INTERNAL_ERROR", 500));
    }*/
}
