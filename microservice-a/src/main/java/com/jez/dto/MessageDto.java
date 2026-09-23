package com.jez.dto;

public record MessageDto(
        Long id,
        String topic,
        String content
) {}
