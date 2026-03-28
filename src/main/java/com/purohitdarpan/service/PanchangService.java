package com.purohitdarpan.service;

import com.purohitdarpan.entity.PanchangCache;
import com.purohitdarpan.repository.PanchangCacheRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PanchangService {

    private final PanchangCacheRepository cacheRepo;
    private final WebClient.Builder webClientBuilder;

    @Value("${panchang.api.base-url}")
    private String apiBaseUrl;

    @Value("${panchang.api.key}")
    private String apiKey;

    @Value("${panchang.api.use-mock}")
    private boolean useMock;

    // ──────────────────────────────────────────────────────────
    // PUBLIC API
    // ──────────────────────────────────────────────────────────

    @Cacheable(value = "panchang", key = "#date.toString()")
    public PanchangCache getForDate(LocalDate date) {
        return cacheRepo.findByDate(date)
                .orElseGet(() -> fetchAndCache(date));
    }

    public List<PanchangCache> getForMonth(YearMonth month) {
        LocalDate start = month.atDay(1);
        LocalDate end = month.atEndOfMonth().plusDays(1);
        List<PanchangCache> cached = cacheRepo.findByDateBetween(start, end);

        // Fill any missing dates
        LocalDate current = start;
        while (!current.isAfter(month.atEndOfMonth())) {
            final LocalDate d = current;
            boolean exists = cached.stream().anyMatch(p -> p.getDate().equals(d));
            if (!exists) {
                cached.add(fetchAndCache(d));
            }
            current = current.plusDays(1);
        }
        cached.sort((a, b) -> a.getDate().compareTo(b.getDate()));
        return cached;
    }

    // ──────────────────────────────────────────────────────────
    // INTERNAL FETCH & CACHE
    // ──────────────────────────────────────────────────────────

    private PanchangCache fetchAndCache(LocalDate date) {
        PanchangCache data = useMock ? generateMockPanchang(date) : fetchFromApi(date);
        return cacheRepo.save(data);
    }

    private PanchangCache fetchFromApi(LocalDate date) {
        try {
            WebClient client = webClientBuilder.baseUrl(apiBaseUrl).build();
            Map response = client.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/panchang")
                            .queryParam("date", date.toString())
                            .queryParam("api_key", apiKey)
                            .queryParam("lang", "en")
                            .queryParam("tz", "5.5")
                            .build())
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            return parsePanchangResponse(response, date);
        } catch (Exception e) {
            log.warn("Panchang API error for {}, using mock: {}", date, e.getMessage());
            return generateMockPanchang(date);
        }
    }

    @SuppressWarnings("unchecked")
    private PanchangCache parsePanchangResponse(Map<?,?> response, LocalDate date) {
        Map<?,?> status = (Map<?,?>) response.get("status");
        Map<?,?> data = (Map<?,?>) response.get("panchang");

        String tithi     = extractString(data, "tithi");
        String nakshatra = extractString(data, "nakshatra");
        String yoga      = extractString(data, "yoga");
        String karana    = extractString(data, "karana");
        String vara      = extractString(data, "vara");

        return buildPanchangCache(date, tithi, nakshatra, yoga, karana, vara);
    }

    private String extractString(Map<?,?> map, String key) {
        Object val = map.get(key);
        return val != null ? val.toString() : "";
    }

    /**
     * Mock Panchang data calculated using simplified rules for development/testing.
     * Real implementation uses Swiss Ephemeris or external API.
     */
    private PanchangCache generateMockPanchang(LocalDate date) {
        // Simplified tithi calculation: lunar day based on days from a known new moon
        // Reference new moon: Jan 29, 2025 (Amavasya)
        // Reference new moon: Aligned so Mar 27 2026 is Navami
        LocalDate referenceNewMoon = LocalDate.of(2026, 3, 18);
        long daysSince = date.toEpochDay() - referenceNewMoon.toEpochDay();
        int lunarDay = (int) (daysSince % 30);
        if (lunarDay < 0) lunarDay += 30;

        String[] tithis = {
            "Amavasya","Pratipada","Dwitiya","Tritiya","Chaturthi",
            "Panchami","Shashthi","Saptami","Ashtami","Navami",
            "Dashami","Ekadashi","Dwadashi","Trayodashi","Chaturdashi","Purnima",
            "Pratipada","Dwitiya","Tritiya","Chaturthi","Panchami",
            "Shashthi","Saptami","Ashtami","Navami","Dashami",
            "Ekadashi","Dwadashi","Trayodashi","Chaturdashi"
        };

        String[] nakshatras = {
            "Ashwini","Bharani","Krittika","Rohini","Mrigashira",
            "Ardra","Punarvasu","Pushya","Ashlesha","Magha",
            "Purva Phalguni","Uttara Phalguni","Hasta","Chitra","Swati",
            "Vishakha","Anuradha","Jyeshtha","Mula","Purva Ashadha",
            "Uttara Ashadha","Shravana","Dhanishtha","Shatabhisha","Purva Bhadrapada",
            "Uttara Bhadrapada","Revati"
        };

        String[] yogas = {
            "Vishkambha","Priti","Ayushman","Saubhagya","Shobhana",
            "Atiganda","Sukarma","Dhriti","Shula","Ganda",
            "Vriddhi","Dhruva","Vyaghata","Harshana","Vajra",
            "Siddhi","Vyatipata","Variyan","Parigha","Shiva",
            "Siddha","Sadhya","Shubha","Shukla","Brahma","Indra","Vaidhriti"
        };

        String[] varas = {"Ravivar","Somvar","Mangalvar","Budhvar","Guruvar","Shukravar","Shanivar"};

        int dayOfWeek = date.getDayOfWeek().getValue() % 7;
        int nakshatraIdx = (int) (daysSince % 27 + 27) % 27;
        int yogaIdx = (int) (daysSince % 27 + 27) % 27;

        String tithi     = tithis[lunarDay % tithis.length];
        String nakshatra = nakshatras[nakshatraIdx];
        String yoga      = yogas[yogaIdx % yogas.length];
        String vara      = varas[dayOfWeek];
        String karana    = (lunarDay % 2 == 0) ? "Bava" : "Balava";

        boolean isPurnima = "Purnima".equals(tithi);
        boolean isAmavasya = "Amavasya".equals(tithi);
        boolean isEkadashi = tithi.contains("Ekadashi");

        return buildPanchangCache(date, tithi, nakshatra, yoga, karana, vara)
                .toBuilder()
                .isPurnima(isPurnima)
                .isAmavasya(isAmavasya)
                .isEkadashi(isEkadashi)
                .build();
    }

    private PanchangCache buildPanchangCache(LocalDate date, String tithi,
            String nakshatra, String yoga, String karana, String vara) {

        // Rahu Kaal: varies by day of week
        // Sun=4:30-6, Mon=7:30-9, Tue=3-4:30, Wed=12-1:30, Thu=1:30-3, Fri=10:30-12, Sat=9-10:30
        LocalTime[] rahuStarts = {
            LocalTime.of(16,30), LocalTime.of(7,30), LocalTime.of(15,0),
            LocalTime.of(12,0), LocalTime.of(13,30), LocalTime.of(10,30), LocalTime.of(9,0)
        };
        int dow = date.getDayOfWeek().getValue() % 7;
        LocalTime rahuStart = rahuStarts[dow];
        LocalTime rahuEnd   = rahuStart.plusHours(1).plusMinutes(30);

        LocalTime sunrise   = LocalTime.of(6, 15);
        LocalTime sunset    = LocalTime.of(18, 30);
        LocalTime brahmaMuhurtaStart = sunrise.minusHours(1).minusMinutes(36);
        LocalTime brahmaMuhurtaEnd   = sunrise.minusMinutes(48);

        // Abhijit: ~48 min around solar noon
        long sunriseMinutes = sunrise.toSecondOfDay() / 60;
        long sunsetMinutes  = sunset.toSecondOfDay() / 60;
        long noon = (sunriseMinutes + sunsetMinutes) / 2;
        LocalTime abhijitStart = LocalTime.ofSecondOfDay((noon - 24) * 60);
        LocalTime abhijitEnd   = LocalTime.ofSecondOfDay((noon + 24) * 60);

        return PanchangCache.builder()
                .date(date)
                .tithi(tithi)
                .nakshatra(nakshatra)
                .yoga(yoga)
                .karana(karana)
                .vara(vara)
                .rahuStart(rahuStart)
                .rahuEnd(rahuEnd)
                .gulikaStart(rahuStart.plusHours(3))
                .gulikaEnd(rahuEnd.plusHours(3))
                .yamghantStart(rahuStart.plusHours(6).minusMinutes(30))
                .yamghantEnd(rahuEnd.plusHours(6).minusMinutes(30))
                .abhijitStart(abhijitStart)
                .abhijitEnd(abhijitEnd)
                .brahmaMuhurtaStart(brahmaMuhurtaStart)
                .brahmaMuhurtaEnd(brahmaMuhurtaEnd)
                .sunrise(sunrise)
                .sunset(sunset)
                .build();
    }
}

