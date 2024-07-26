package com.blog.dtos;

import java.util.List;

public record PageResponseDTO(List<?> pageContent, int totalPages) {

}
