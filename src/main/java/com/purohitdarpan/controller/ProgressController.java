package com.purohitdarpan.controller;

import com.purohitdarpan.entity.*;
import com.purohitdarpan.service.ProgressService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProgressController {

    private final ProgressService progressService;

    // ── Progress ──────────────────────────────────────────────
    @PostMapping("/progress/update")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserPujaProgress> updateProgress(@RequestBody ProgressRequest req) {
        return ResponseEntity.ok(
                progressService.updateProgress(req.getUserId(), req.getPujaId(),
                        req.getStepId(), req.getFormat(), req.isCompleted()));
    }

    @GetMapping("/progress/{userId}/{pujaId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<UserPujaProgress>> getProgress(
            @PathVariable Long userId, @PathVariable Long pujaId) {
        return ResponseEntity.ok(progressService.getProgress(userId, pujaId));
    }

    // ── Notes ─────────────────────────────────────────────────
    @PostMapping("/notes")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserStepNote> saveNote(@RequestBody NoteRequest req) {
        return ResponseEntity.ok(
                progressService.saveNote(req.getUserId(), req.getStepId(), req.getNoteText()));
    }

    @GetMapping("/notes/{userId}/{stepId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<UserStepNote>> getNotes(
            @PathVariable Long userId, @PathVariable Long stepId) {
        return ResponseEntity.ok(progressService.getNotes(userId, stepId));
    }

    @DeleteMapping("/notes/{userId}/{noteId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteNote(
            @PathVariable Long userId, @PathVariable Long noteId) {
        progressService.deleteNote(userId, noteId);
        return ResponseEntity.noContent().build();
    }

    // ── Highlights ────────────────────────────────────────────
    @PostMapping("/highlights")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserHighlight> saveHighlight(@RequestBody HighlightRequest req) {
        return ResponseEntity.ok(
                progressService.saveHighlight(req.getUserId(), req.getStepId(),
                        req.getText(), req.getStartOffset(), req.getEndOffset()));
    }

    @GetMapping("/highlights/{userId}/{stepId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<UserHighlight>> getHighlights(
            @PathVariable Long userId, @PathVariable Long stepId) {
        return ResponseEntity.ok(progressService.getHighlights(userId, stepId));
    }

    // ── Video Bookmarks ───────────────────────────────────────
    @PostMapping("/bookmarks/video")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<VideoBookmark> saveVideoBookmark(@RequestBody VideoBookmarkRequest req) {
        return ResponseEntity.ok(
                progressService.saveVideoBookmark(req.getUserId(), req.getStepId(),
                        req.getVideoUrl(), req.getTimestampSeconds(), req.getLabel()));
    }

    @GetMapping("/bookmarks/video/{userId}/{stepId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<VideoBookmark>> getVideoBookmarks(
            @PathVariable Long userId, @PathVariable Long stepId) {
        return ResponseEntity.ok(progressService.getVideoBookmarks(userId, stepId));
    }

    // ── PDF Bookmarks / Annotations ───────────────────────────
    @PostMapping("/bookmarks/pdf-page")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PdfAnnotation> savePdfAnnotation(@RequestBody PdfAnnotationRequest req) {
        return ResponseEntity.ok(
                progressService.savePdfAnnotation(req.getUserId(), req.getResourceId(),
                        req.getPageNumber(), req.getAnnotationText(), req.getX(), req.getY()));
    }

    @GetMapping("/bookmarks/pdf-page/{userId}/{resourceId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<PdfAnnotation>> getPdfAnnotations(
            @PathVariable Long userId, @PathVariable Long resourceId) {
        return ResponseEntity.ok(progressService.getPdfAnnotations(userId, resourceId));
    }

    // ── Inner DTOs ────────────────────────────────────────────
    @Data public static class ProgressRequest {
        private Long userId, pujaId, stepId;
        private String format;
        private boolean completed;
    }

    @Data public static class NoteRequest {
        private Long userId, stepId;
        private String noteText;
    }

    @Data public static class HighlightRequest {
        private Long userId, stepId;
        private String text;
        private int startOffset, endOffset;
    }

    @Data public static class VideoBookmarkRequest {
        private Long userId, stepId;
        private String videoUrl, label;
        private int timestampSeconds;
    }

    @Data public static class PdfAnnotationRequest {
        private Long userId, resourceId;
        private int pageNumber;
        private String annotationText;
        private float x, y;
    }
}
