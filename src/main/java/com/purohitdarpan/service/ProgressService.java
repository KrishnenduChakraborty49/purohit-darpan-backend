package com.purohitdarpan.service;

import com.purohitdarpan.entity.*;
import com.purohitdarpan.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProgressService {

    private final UserPujaProgressRepository progressRepo;
    private final UserStepNoteRepository noteRepo;
    private final UserHighlightRepository highlightRepo;
    private final VideoBookmarkRepository videoBookmarkRepo;
    private final PdfAnnotationRepository pdfAnnotationRepo;
    private final UserRepository userRepo;
    private final PujaRepository pujaRepo;
    private final PujaStepRepository stepRepo;
    private final ResourceRepository resourceRepo;

    @Transactional
    public UserPujaProgress updateProgress(Long userId, Long pujaId, Long stepId,
                                           String format, boolean completed) {
        UserPujaProgress.LearningFormat fmt = UserPujaProgress.LearningFormat.valueOf(format);
        Optional<UserPujaProgress> existing =
                progressRepo.findByUserIdAndPujaIdAndStepIdAndFormat(userId, pujaId, stepId, fmt);

        UserPujaProgress progress;
        if (existing.isPresent()) {
            progress = existing.get();
            progress.setCompleted(completed);
        } else {
            progress = UserPujaProgress.builder()
                    .user(userRepo.getReferenceById(userId))
                    .puja(pujaRepo.getReferenceById(pujaId))
                    .step(stepRepo.getReferenceById(stepId))
                    .format(fmt)
                    .completed(completed)
                    .build();
        }
        return progressRepo.save(progress);
    }

    public List<UserPujaProgress> getProgress(Long userId, Long pujaId) {
        return progressRepo.findByUserIdAndPujaId(userId, pujaId);
    }

    @Transactional
    public UserStepNote saveNote(Long userId, Long stepId, String noteText) {
        UserStepNote note = UserStepNote.builder()
                .user(userRepo.getReferenceById(userId))
                .step(stepRepo.getReferenceById(stepId))
                .noteText(noteText)
                .build();
        return noteRepo.save(note);
    }

    public List<UserStepNote> getNotes(Long userId, Long stepId) {
        return noteRepo.findByUserIdAndStepIdOrderByCreatedAtDesc(userId, stepId);
    }

    @Transactional
    public void deleteNote(Long userId, Long noteId) {
        noteRepo.deleteByUserIdAndId(userId, noteId);
    }

    @Transactional
    public UserHighlight saveHighlight(Long userId, Long stepId, String text, int start, int end) {
        UserHighlight hl = UserHighlight.builder()
                .user(userRepo.getReferenceById(userId))
                .step(stepRepo.getReferenceById(stepId))
                .highlightedText(text)
                .startOffset(start)
                .endOffset(end)
                .build();
        return highlightRepo.save(hl);
    }

    public List<UserHighlight> getHighlights(Long userId, Long stepId) {
        return highlightRepo.findByUserIdAndStepId(userId, stepId);
    }

    @Transactional
    public VideoBookmark saveVideoBookmark(Long userId, Long stepId,
                                           String videoUrl, int timestampSeconds, String label) {
        VideoBookmark bm = VideoBookmark.builder()
                .user(userRepo.getReferenceById(userId))
                .step(stepRepo.getReferenceById(stepId))
                .videoUrl(videoUrl)
                .timestampSeconds(timestampSeconds)
                .label(label)
                .build();
        return videoBookmarkRepo.save(bm);
    }

    public List<VideoBookmark> getVideoBookmarks(Long userId, Long stepId) {
        return videoBookmarkRepo.findByUserIdAndStepIdOrderByTimestampSecondsAsc(userId, stepId);
    }

    @Transactional
    public PdfAnnotation savePdfAnnotation(Long userId, Long resourceId, int pageNumber,
                                            String text, float x, float y) {
        PdfAnnotation ann = PdfAnnotation.builder()
                .user(userRepo.getReferenceById(userId))
                .resource(resourceRepo.getReferenceById(resourceId))
                .pageNumber(pageNumber)
                .annotationText(text)
                .positionX(x)
                .positionY(y)
                .build();
        return pdfAnnotationRepo.save(ann);
    }

    public List<PdfAnnotation> getPdfAnnotations(Long userId, Long resourceId) {
        return pdfAnnotationRepo
                .findByUserIdAndResourceIdOrderByPageNumberAscPositionYAsc(userId, resourceId);
    }
}
