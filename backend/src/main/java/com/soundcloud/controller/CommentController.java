package com.soundcloud.controller;

import com.soundcloud.dto.CommentRequest;
import com.soundcloud.dto.CommentResponse;
import com.soundcloud.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tracks/{trackId}/comments")
@RequiredArgsConstructor
public class CommentController {
    
    private final CommentService commentService;
    
    @PostMapping
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable Long trackId,
            @Valid @RequestBody CommentRequest request) {
        return ResponseEntity.ok(commentService.addComment(trackId, request));
    }
    
    @GetMapping
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable Long trackId) {
        return ResponseEntity.ok(commentService.getTrackComments(trackId));
    }
    
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long trackId,
            @PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
