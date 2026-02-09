package com.soundcloud.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentRequest {
    @NotBlank
    private String content;
    
    private Integer timestamp; // позиция в треке
}
