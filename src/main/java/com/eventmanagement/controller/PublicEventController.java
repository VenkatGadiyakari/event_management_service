package com.eventmanagement.controller;

import com.eventmanagement.dto.EventDetailResponse;
import com.eventmanagement.dto.EventSummaryResponse;
import com.eventmanagement.dto.PageResponse;
import com.eventmanagement.enums.EventCategory;
import com.eventmanagement.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Public — Events", description = "Open endpoints for browsing and viewing published events")
@RestController
@RequestMapping("/api/events")
public class PublicEventController {

    private final EventService eventService;

    public PublicEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @Operation(summary = "Browse published events", description = "Returns a paginated list of PUBLISHED events. Filterable by category, city, and keyword search.")
    @ApiResponse(responseCode = "200", description = "Paginated event list")
    @GetMapping
    public ResponseEntity<PageResponse<EventSummaryResponse>> browseEvents(
            @Parameter(description = "Filter by category") @RequestParam(value = "category", required = false) EventCategory category,
            @Parameter(description = "Filter by city name (partial match)") @RequestParam(value = "city", required = false) String city,
            @Parameter(description = "Keyword search on event title") @RequestParam(value = "search", required = false) String search,
            @Parameter(description = "Page index (0-based)") @RequestParam(value = "page", defaultValue = "0") int page,
            @Parameter(description = "Page size (1-100, default 12)") @RequestParam(value = "size", defaultValue = "12") int size) {

        if (page < 0) {
            page = 0;
        }
        if (size <= 0 || size > 100) {
            size = 12;
        }

        PageResponse<EventSummaryResponse> response = eventService.browseEvents(category, city, search, page, size);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get event details", description = "Returns full event details including venue info and all ticket tiers.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Event details returned"),
        @ApiResponse(responseCode = "404", description = "Event not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<EventDetailResponse> getEventDetail(@PathVariable("id") UUID eventId) {
        EventDetailResponse response = eventService.getEventDetail(eventId);
        return ResponseEntity.ok(response);
    }
}
