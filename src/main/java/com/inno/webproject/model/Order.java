package com.inno.webproject.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Order(Long id, Long userId, Long productId, String productName, int quantity,
                     BigDecimal totalPrice, String status, LocalDateTime createdAt) {
}
