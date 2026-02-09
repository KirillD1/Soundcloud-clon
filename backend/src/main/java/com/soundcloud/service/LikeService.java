package com.soundcloud.service;

import com.soundcloud.exception.ResourceNotFoundException;
import com.soundcloud.model.Like;
import com.soundcloud.model.Track;
import com.soundcloud.model.User;
import com.soundcloud.repository.LikeRepository;
import com.soundcloud.repository.TrackRepository;
import com.soundcloud.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {
    
    private final LikeRepository likeRepository;
    private final TrackRepository trackRepository;
    private final UserRepository userRepository;
    
    @Transactional
    public void toggleLike(Long trackId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new ResourceNotFoundException("Track not found"));
        
        if (likeRepository.existsByUserIdAndTrackId(user.getId(), trackId)) {
            likeRepository.deleteByUserIdAndTrackId(user.getId(), trackId);
            track.setLikesCount(track.getLikesCount() - 1);
        } else {
            Like like = Like.builder()
                    .user(user)
                    .track(track)
                    .build();
            likeRepository.save(like);
            track.setLikesCount(track.getLikesCount() + 1);
        }
        
        trackRepository.save(track);
    }
}
