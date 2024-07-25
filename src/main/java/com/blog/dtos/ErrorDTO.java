package com.blog.dtos;

public record ErrorDTO(String title, Integer status, String detail, String instance, String timestamp) {
}
