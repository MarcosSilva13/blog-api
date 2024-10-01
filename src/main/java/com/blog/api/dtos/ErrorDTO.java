package com.blog.api.dtos;

public record ErrorDTO(String title, Integer status, String detail, String instance, String timestamp) {
}
