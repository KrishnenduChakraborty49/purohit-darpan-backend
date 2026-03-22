package com.purohitdarpan.controller;

import com.purohitdarpan.entity.HinduFestival;
import com.purohitdarpan.entity.PanchangCache;
import com.purohitdarpan.repository.HinduFestivalRepository;
import com.purohitdarpan.service.PanchangService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PanchangController {

    private final PanchangService panchangService;
    private final HinduFestivalRepository festivalRepo;

    // ── Panchang endpoints ─────────────────────────────────────

    @GetMapping("/api/panchang/today")
    public ResponseEntity<PanchangCache> today() {
        return ResponseEntity.ok(panchangService.getForDate(LocalDate.now()));
    }

    @GetMapping("/api/panchang/date/{date}")
    public ResponseEntity<PanchangCache> forDate(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return ResponseEntity.ok(panchangService.getForDate(date));
    }

    @GetMapping("/api/panchang/month/{yearMonth}")
    public ResponseEntity<List<PanchangCache>> forMonth(
            @PathVariable String yearMonth) {
        YearMonth ym = YearMonth.parse(yearMonth); // e.g. "2025-10"
        return ResponseEntity.ok(panchangService.getForMonth(ym));
    }

    // ── Festival endpoints ─────────────────────────────────────

    @GetMapping("/api/festivals/upcoming")
    public ResponseEntity<List<HinduFestival>> upcoming(
            @RequestParam(defaultValue = "30") int days) {
        LocalDate today = LocalDate.now();
        return ResponseEntity.ok(festivalRepo.findUpcoming(today, today.plusDays(days)));
    }

    @GetMapping("/api/festivals/year/{year}")
    public ResponseEntity<List<HinduFestival>> byYear(@PathVariable int year) {
        return ResponseEntity.ok(festivalRepo.findByYear(year));
    }

    @GetMapping("/api/festivals/{id}")
    public ResponseEntity<HinduFestival> getOne(@PathVariable Long id) {
        return festivalRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
