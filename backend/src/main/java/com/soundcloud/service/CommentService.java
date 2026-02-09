package com.soundcloud.service;

import com.soundcloud.dto.CommentRequest;
import com.soundcloud.dto.CommentResponse;
import com.soundcloud.dto.UserResponse;
import com.soundcloud.exception.ResourceNotFoundException;
import com.soundcloud.model.Comment;
import com.soundcloud.model.Track;
import com.soundcloud.model.User;
import com.soundcloud.repository.CommentRepository;
import com.soundcloud.repository.TrackRepository;
import com.soundcloud.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    
    private final CommentRepository commentRepository;
    private final TrackRepository trackRepository;
    private final UserRepository userRepository;
    
    @Transactional
    public CommentResponse addComment(Long trackId, CommentRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new ResourceNotFoundException("Track not found"));
        
        Comment comment = Comment.builder()
                .user(user)
                .track(track)
                .content(request.getContent())
                .timestamp(request.getTimestamp())
                .build();
        
        comment = commentRepository.save(comment);
        return mapToCommentResponse(comment);
    }
    
    public List<CommentResponse> getTrackComments(Long trackId) {
        return commentRepository.findByTrackIdOrderByTimestampAsc(trackId)
                .stream()
                .map(this::mapToCommentResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You can only delete your own comments");
        }
        
        commentRepository.delete(comment);
    }
    
    private CommentResponse mapToCommentResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .timestamp(comment.getTimestamp())
                .createdAt(comment.getCreatedAt())
                .user(mapToUserResponse(comment.getUser()))
                .build();
    }
    
    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .displayName(user.getDisplayName())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }
}
