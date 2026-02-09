package com.soundcloud.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrackResponse {
    private Long id;
    private String title;
    private String description;
    private String genre;
    private String audioUrl;
    private String coverUrl;
    private Integer duration;
    private Integer playsCount;
    private Integer likesCount;
    private Boolean isPublic;
    private LocalDateTime createdAt;
    private UserResponse user;
    private Boolean isLiked; // для текущего пользователя
}
