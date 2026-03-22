package com.purohitdarpan.repository;

import com.purohitdarpan.entity.PdfAnnotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PdfAnnotationRepository extends JpaRepository<PdfAnnotation, Long> {
    List<PdfAnnotation> findByUserIdAndResourceIdOrderByPageNumberAscPositionYAsc(
            Long userId, Long resourceId);
    List<PdfAnnotation> findByUserIdAndResourceIdAndPageNumber(
            Long userId, Long resourceId, Integer pageNumber);
    void deleteByUserIdAndId(Long userId, Long id);
}
