package com.soundcloud.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TrackUploadRequest {
    @NotBlank
    private String title;
    
    private String description;
    private String genre;
    private Boolean isPublic = true;
}
