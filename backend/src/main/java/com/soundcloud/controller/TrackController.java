package com.soundcloud.controller;

import com.soundcloud.dto.TrackResponse;
import com.soundcloud.dto.TrackUploadRequest;
import com.soundcloud.service.StorageService;
import com.soundcloud.service.TrackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

@RestController
@RequestMapping("/api/tracks")
@RequiredArgsConstructor
public class TrackController {
    
    private final TrackService trackService;
    private final StorageService storageService;
    
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TrackResponse> uploadTrack(
            @Valid @ModelAttribute TrackUploadRequest request,
            @RequestParam("audio") MultipartFile audioFile,
            @RequestParam(value = "cover", required = false) MultipartFile coverFile) {
        
        TrackResponse track = trackService.uploadTrack(request, audioFile, coverFile);
        return ResponseEntity.ok(track);
    }
    
    @GetMapping
    public ResponseEntity<Page<TrackResponse>> getAllTracks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(trackService.getAllTracks(pageable));
    }
    
    @GetMapping("/trending")
    public ResponseEntity<Page<TrackResponse>> getTrendingTracks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(trackService.getTrendingTracks(pageable));
    }
    
    @GetMapping("/search")
    public ResponseEntity<Page<TrackResponse>> searchTracks(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(trackService.searchTracks(q, pageable));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TrackResponse> getTrack(@PathVariable Long id) {
        return ResponseEntity.ok(trackService.getTrackById(id));
    }
    
    @GetMapping("/{id}/stream")
    public ResponseEntity<Resource> streamTrack(@PathVariable Long id) throws Exception {
        TrackResponse track = trackService.getTrackById(id);
        trackService.incrementPlaysCount(id);
        
        Path filePath = storageService.loadFile(track.getAudioUrl());
        Resource resource = new UrlResource(filePath.toUri());
        
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("audio/mpeg"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + track.getTitle() + ".mp3\"")
                .body(resource);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrack(@PathVariable Long id) {
        trackService.deleteTrack(id);
        return ResponseEntity.noContent().build();
    }
}
