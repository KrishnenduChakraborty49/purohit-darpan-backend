package com.purohitdarpan.service;

import com.purohitdarpan.entity.AiFeedback;
import com.purohitdarpan.entity.AiQueryLog;
import com.purohitdarpan.entity.User;
import com.purohitdarpan.repository.AiFeedbackRepository;
import com.purohitdarpan.repository.AiQueryLogRepository;
import com.purohitdarpan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIService {

    private final ChatClient chatClient;
    private final AiQueryLogRepository queryLogRepo;
    private final AiFeedbackRepository feedbackRepo;
    private final UserRepository userRepo;

    private static final String SYSTEM_PROMPT = """
            You are Guru — a wise and knowledgeable Hindu ritual assistant embedded in Purohit Darpan.
            Your role is to help beginner Brahmin priests understand Sanskrit mantras, shlokas, and
            Vedic rituals with clarity and respect.

            Rules:
            1. Answer ONLY questions about Hindu rituals, Sanskrit, Vedic practices, pujas, mantras, and samagri.
            2. If asked anything unrelated, say: 'I am here only to assist with Vedic rituals and Sanskrit learning.'
            3. When explaining Sanskrit words always provide:
               - Devanagari text
               - IAST transliteration
               - Word-by-word meaning
               - Grammatical notes if helpful
            4. Always be respectful, patient, and encouraging towards learners.
            5. Never fabricate mantras or ritual instructions. If unsure, say so clearly.

            Always structure your responses as follows:
            ## Direct Answer
            (2-3 clear sentences)

            ## Sanskrit Context
            (if applicable — Devanagari with transliteration below)

            ## Step-by-step Explanation
            (numbered steps if applicable)

            ## Related Samagri
            (if applicable — list of ritual items needed)

            ## Learn More
            (suggest related pujas or topics to study)
            """;

    /**
     * MODE A: Explain a specific Sanskrit word in context of a shlok
     */
    @Transactional
    public String explainWord(String word, String shlokContext, String pujaContext,
                              Long userId, Long contextPujaId, Long contextStepId) {
        String prompt = String.format(
                """
                Context: The user is studying the puja: %s
                Full shlok: "%s"
                The user tapped on the word: "%s"
                
                Please explain:
                1. The meaning of "%s" in Devanagari and IAST
                2. Its grammatical role in this shlok
                3. How it is pronounced
                4. Its significance in this ritual context
                """,
                pujaContext, shlokContext, word, word);

        return callAI(prompt, AiQueryLog.QueryType.WORD_QUERY, userId,
                contextPujaId, contextStepId, shlokContext);
    }

    /**
     * MODE A: Explain a full shlok with word-by-word breakdown
     */
    @Transactional
    public String explainShlok(String shlokText, String pujaContext,
                                Long userId, Long contextPujaId, Long contextStepId) {
        String prompt = String.format(
                """
                Context: The user is studying the puja: %s
                Shlok to explain: "%s"
                
                Please provide:
                1. Complete word-by-word meaning (anvaya)
                2. Overall translation
                3. Spiritual significance of this shlok
                4. When and how it is chanted in the ritual
                5. Pronunciation guide for key words
                """,
                pujaContext, shlokText);

        return callAI(prompt, AiQueryLog.QueryType.SHLOK_QUERY, userId,
                contextPujaId, contextStepId, shlokText);
    }

    /**
     * MODE B: Answer a general ritual question from the chat panel
     */
    @Transactional
    public String answerRitualQuestion(String question, String userContext,
                                        Long userId, Long contextPujaId, Long contextStepId) {
        String prompt = userContext != null && !userContext.isBlank()
                ? String.format("Context: %s\n\nUser question: %s", userContext, question)
                : question;

        return callAI(prompt, AiQueryLog.QueryType.GENERAL_QUESTION, userId,
                contextPujaId, contextStepId, null);
    }

    /**
     * Save feedback for an AI response
     */
    @Transactional
    public void saveFeedback(Long queryLogId, Long userId, byte rating, String comment) {
        AiQueryLog log = queryLogRepo.findById(queryLogId)
                .orElseThrow(() -> new IllegalArgumentException("Query log not found"));
        User user = userRepo.getReferenceById(userId);

        AiFeedback feedback = AiFeedback.builder()
                .queryLog(log)
                .user(user)
                .rating(rating)
                .comment(comment)
                .build();
        feedbackRepo.save(feedback);
    }

    private String callAI(String userPrompt, AiQueryLog.QueryType queryType,
                            Long userId, Long contextPujaId, Long contextStepId,
                            String contextShlok) {
        long startTime = Instant.now().toEpochMilli();

        try {
            String response = chatClient.prompt()
                    .system(SYSTEM_PROMPT)
                    .user(userPrompt)
                    .call()
                    .content();

            long duration = Instant.now().toEpochMilli() - startTime;
            logQuery(userId, queryType, userPrompt, contextPujaId, contextStepId,
                    contextShlok, response, (int) duration);

            return response;

        } catch (Exception e) {
            log.error("AI call failed: {}", e.getMessage());
            throw new RuntimeException("AI service temporarily unavailable. Please try again.", e);
        }
    }

    private void logQuery(Long userId, AiQueryLog.QueryType queryType, String queryText,
                           Long pujaId, Long stepId, String shlok, String response, int durationMs) {
        try {
            AiQueryLog logEntry = AiQueryLog.builder()
                    .user(userRepo.getReferenceById(userId))
                    .queryType(queryType)
                    .queryText(queryText)
                    .contextPujaId(pujaId)
                    .contextStepId(stepId)
                    .contextShlok(shlok)
                    .responseText(response)
                    .responseTimeMs(durationMs)
                    .build();
            queryLogRepo.save(logEntry);
        } catch (Exception e) {
            log.warn("Failed to log AI query: {}", e.getMessage());
        }
    }
}
