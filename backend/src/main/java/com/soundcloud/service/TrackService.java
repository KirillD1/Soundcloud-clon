package com.soundcloud.service;

import com.soundcloud.dto.TrackResponse;
import com.soundcloud.dto.TrackUploadRequest;
import com.soundcloud.dto.UserResponse;
import com.soundcloud.exception.ResourceNotFoundException;
import com.soundcloud.model.Track;
import com.soundcloud.model.User;
import com.soundcloud.repository.LikeRepository;
import com.soundcloud.repository.TrackRepository;
import com.soundcloud.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class TrackService {
    
    private final TrackRepository trackRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final StorageService storageService;
    
    @Transactional
    public TrackResponse uploadTrack(TrackUploadRequest request, MultipartFile audioFile, MultipartFile coverFile) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        String audioUrl = storageService.uploadFile(audioFile, "audio");
        String coverUrl = coverFile != null ? storageService.uploadFile(coverFile, "covers") : null;
        
        Track track = Track.builder()
                .user(user)
                .title(request.getTitle())
                .description(request.getDescription())
                .genre(request.getGenre())
                .audioUrl(audioUrl)
                .coverUrl(coverUrl)
                .isPublic(request.getIsPublic())
                .playsCount(0)
                .likesCount(0)
                .build();
        
        track = trackRepository.save(track);
        return mapToTrackResponse(track, user.getId());
    }
    
    public Page<TrackResponse> getAllTracks(Pageable pageable) {
        Long currentUserId = getCurrentUserId();
        return trackRepository.findByIsPublicTrue(pageable)
                .map(track -> mapToTrackResponse(track, currentUserId));
    }
    
    public Page<TrackResponse> getTrendingTracks(Pageable pageable) {
        Long currentUserId = getCurrentUserId();
        return trackRepository.findTrendingTracks(pageable)
                .map(track -> mapToTrackResponse(track, currentUserId));
    }
    
    public Page<TrackResponse> searchTracks(String query, Pageable pageable) {
        Long currentUserId = getCurrentUserId();
        return trackRepository.searchTracks(query, pageable)
                .map(track -> mapToTrackResponse(track, currentUserId));
    }
    
    public TrackResponse getTrackById(Long id) {
        Track track = trackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Track not found"));
        Long currentUserId = getCurrentUserId();
        return mapToTrackResponse(track, currentUserId);
    }
    
    @Transactional
    public void incrementPlaysCount(Long trackId) {
        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new ResourceNotFoundException("Track not found"));
        track.setPlaysCount(track.getPlaysCount() + 1);
        trackRepository.save(track);
    }
    
    @Transactional
    public void deleteTrack(Long id) {
        Track track = trackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Track not found"));
        
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        if (!track.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You can only delete your own tracks");
        }
        
        trackRepository.delete(track);
    }
    
    private Long getCurrentUserId() {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            return userRepository.findByEmail(email)
                    .map(User::getId)
                    .orElse(null);
        } catch (Exception e) {
            return null;
        }
    }
    
    private TrackResponse mapToTrackResponse(Track track, Long currentUserId) {
        boolean isLiked = currentUserId != null && 
                         likeRepository.existsByUserIdAndTrackId(currentUserId, track.getId());
        
        return TrackResponse.builder()
                .id(track.getId())
                .title(track.getTitle())
                .description(track.getDescription())
                .genre(track.getGenre())
                .audioUrl(track.getAudioUrl())
                .coverUrl(track.getCoverUrl())
                .duration(track.getDuration())
                .playsCount(track.getPlaysCount())
                .likesCount(track.getLikesCount())
                .isPublic(track.getIsPublic())
                .createdAt(track.getCreatedAt())
                .user(mapToUserResponse(track.getUser()))
                .isLiked(isLiked)
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
