package com.soundcloud.controller;

import com.soundcloud.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tracks/{trackId}/like")
@RequiredArgsConstructor
public class LikeController {
    
    private final LikeService likeService;
    
    @PostMapping
    public ResponseEntity<Void> toggleLike(@PathVariable Long trackId) {
        likeService.toggleLike(trackId);
        return ResponseEntity.ok().build();
    }
}
