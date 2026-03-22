package com.purohitdarpan.controller;

import com.purohitdarpan.repository.AiQueryLogRepository;
import com.purohitdarpan.service.AIService;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AIController {

    private final AIService aiService;
    private final AiQueryLogRepository queryLogRepo;

    /**
     * POST /api/ai/query — general ritual question
     */
    @PostMapping("/query")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, String>> query(@RequestBody QueryRequest req) {
        String response = aiService.answerRitualQuestion(
                req.getQuestion(), req.getUserContext(),
                req.getUserId(), req.getContextPujaId(), req.getContextStepId());
        return ResponseEntity.ok(Map.of("response", response));
    }

    /**
     * POST /api/ai/explain-word — explain a Sanskrit word in shlok context
     */
    @PostMapping("/explain-word")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, String>> explainWord(@RequestBody WordRequest req) {
        String response = aiService.explainWord(
                req.getWord(), req.getShlokContext(), req.getPujaContext(),
                req.getUserId(), req.getContextPujaId(), req.getContextStepId());
        return ResponseEntity.ok(Map.of("response", response));
    }

    /**
     * POST /api/ai/explain-shlok — full shlok breakdown
     */
    @PostMapping("/explain-shlok")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, String>> explainShlok(@RequestBody ShlokRequest req) {
        String response = aiService.explainShlok(
                req.getShlokText(), req.getPujaContext(),
                req.getUserId(), req.getContextPujaId(), req.getContextStepId());
        return ResponseEntity.ok(Map.of("response", response));
    }

    /**
     * GET /api/ai/history/{userId} — conversation history
     */
    @GetMapping("/history/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> history(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(
                queryLogRepo.findByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(page, size)));
    }

    /**
     * POST /api/ai/feedback — thumbs up/down for a query
     */
    @PostMapping("/feedback")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> saveFeedback(@RequestBody FeedbackRequest req) {
        aiService.saveFeedback(req.getQueryLogId(), req.getUserId(),
                req.getRating(), req.getComment());
        return ResponseEntity.ok().build();
    }

    // ── Inner DTOs ──────────────────────────────────────────────
    @Data
    public static class QueryRequest {
        @NotBlank private String question;
        private String userContext;
        private Long userId, contextPujaId, contextStepId;
    }

    @Data
    public static class WordRequest {
        @NotBlank private String word;
        private String shlokContext, pujaContext;
        private Long userId, contextPujaId, contextStepId;
    }

    @Data
    public static class ShlokRequest {
        @NotBlank private String shlokText;
        private String pujaContext;
        private Long userId, contextPujaId, contextStepId;
    }

    @Data
    public static class FeedbackRequest {
        private Long queryLogId, userId;
        private byte rating;
        private String comment;
    }
}
