package com.soundcloud.repository;

import com.soundcloud.model.Track;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackRepository extends JpaRepository<Track, Long> {
    Page<Track> findByIsPublicTrue(Pageable pageable);
    Page<Track> findByUserId(Long userId, Pageable pageable);
    
    @Query("SELECT t FROM Track t WHERE t.isPublic = true ORDER BY t.playsCount DESC")
    Page<Track> findTrendingTracks(Pageable pageable);
    
    @Query("SELECT t FROM Track t WHERE t.isPublic = true AND " +
           "(LOWER(t.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(t.genre) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Track> searchTracks(String query, Pageable pageable);
}
