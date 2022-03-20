package com.example.air_companies_management_system.exception;

public class ErrorInfo {
    public String url;
    public String message;

    public ErrorInfo(String url, Exception ex) {
        this.url = url;
        this.message = ex.getMessage();
    }
}
