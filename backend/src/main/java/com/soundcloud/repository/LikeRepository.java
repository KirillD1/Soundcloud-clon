package com.soundcloud.repository;

import com.soundcloud.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserIdAndTrackId(Long userId, Long trackId);
    Boolean existsByUserIdAndTrackId(Long userId, Long trackId);
    void deleteByUserIdAndTrackId(Long userId, Long trackId);
}
