package com.example.databese_migration.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private String errorCode;
    private int statusCode;

    public static <T> ApiResponse<T> success(String message, T data , int code) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setStatusCode(code);

        if (message != null) {
            response.setMessage(message);
        }

        if (data != null) {
            response.setData(data);
        }

        return response;
    }


    public static <T> ApiResponse<T> error(String message, String errorCode, int code) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setStatusCode(code);

        if (message != null) {
            response.setMessage(message);
        }

        if (errorCode != null) {
            response.setErrorCode(errorCode);
        }

        return response;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}

