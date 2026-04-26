package com.eventmanagement.controller;

import com.eventmanagement.dto.CreateEventRequest;
import com.eventmanagement.dto.CreateTierRequest;
import com.eventmanagement.dto.EventDetailResponse;
import com.eventmanagement.dto.EventResponse;
import com.eventmanagement.dto.PageResponse;
import com.eventmanagement.dto.SalesSummaryResponse;
import com.eventmanagement.dto.TierResponse;
import com.eventmanagement.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Admin — Events", description = "Organiser operations: create/update/publish/cancel events and manage ticket tiers")
@RestController
@RequestMapping("/api/admin/events")
public class AdminEventController {

    private final EventService eventService;

    public AdminEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @Operation(summary = "List organiser's events", description = "Returns all events (DRAFT, PUBLISHED, CANCELLED) owned by the authenticated organiser.")
    @GetMapping
    public ResponseEntity<PageResponse<EventDetailResponse>> getMyEvents(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") UUID organiserId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        PageResponse<EventDetailResponse> response = eventService.getOrganiserEvents(organiserId, page, size);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get a single organiser event", description = "Returns a specific event owned by the authenticated organiser, regardless of status.")
    @GetMapping("/{id}")
    public ResponseEntity<EventDetailResponse> getMyEvent(
            @PathVariable("id") UUID eventId,
            @Parameter(hidden = true) @RequestHeader("X-User-Id") UUID organiserId) {
        EventDetailResponse response = eventService.getOrganiserEvent(eventId, organiserId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create a new event", description = "Creates a new event in DRAFT status. The organiser must provide a valid venue ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Event created"),
        @ApiResponse(responseCode = "400", description = "Validation error", content = @Content),
        @ApiResponse(responseCode = "404", description = "Venue not found", content = @Content)
    })
    @PostMapping
    public ResponseEntity<EventResponse> createEvent(
            @Valid @RequestBody CreateEventRequest request,
            @Parameter(hidden = true) @RequestHeader("X-User-Id") UUID organiserId) {
        EventResponse response = eventService.createEvent(request, organiserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Add a ticket tier to an event", description = "Adds a new ticket tier (e.g. VIP, General) to an existing event. Only the owning organiser can add tiers.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Tier created"),
        @ApiResponse(responseCode = "400", description = "Validation error", content = @Content),
        @ApiResponse(responseCode = "403", description = "Not the event organiser", content = @Content),
        @ApiResponse(responseCode = "404", description = "Event not found", content = @Content)
    })
    @PostMapping("/{id}/tiers")
    public ResponseEntity<TierResponse> addTier(
            @PathVariable("id") UUID eventId,
            @Valid @RequestBody CreateTierRequest request,
            @Parameter(hidden = true) @RequestHeader("X-User-Id") UUID organiserId) {
        TierResponse response = eventService.addTier(eventId, request, organiserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update a ticket tier", description = "Replaces all fields of an existing tier. Only the owning organiser can update tiers.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tier updated"),
        @ApiResponse(responseCode = "403", description = "Not the event organiser", content = @Content),
        @ApiResponse(responseCode = "404", description = "Event or tier not found", content = @Content)
    })
    @PutMapping("/{id}/tiers/{tierId}")
    public ResponseEntity<TierResponse> updateTier(
            @PathVariable("id") UUID eventId,
            @PathVariable("tierId") UUID tierId,
            @Valid @RequestBody CreateTierRequest request,
            @Parameter(hidden = true) @RequestHeader("X-User-Id") UUID organiserId) {
        TierResponse response = eventService.updateTier(eventId, tierId, request, organiserId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a ticket tier", description = "Removes a tier from an event. Only the owning organiser can delete tiers.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Tier deleted"),
        @ApiResponse(responseCode = "403", description = "Not the event organiser", content = @Content),
        @ApiResponse(responseCode = "404", description = "Event or tier not found", content = @Content)
    })
    @DeleteMapping("/{id}/tiers/{tierId}")
    public ResponseEntity<Void> deleteTier(
            @PathVariable("id") UUID eventId,
            @PathVariable("tierId") UUID tierId,
            @Parameter(hidden = true) @RequestHeader("X-User-Id") UUID organiserId) {
        eventService.deleteTier(eventId, tierId, organiserId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update an event", description = "Replaces all fields of an existing event. Only allowed while the event is in DRAFT status.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Event updated"),
        @ApiResponse(responseCode = "403", description = "Not the event organiser", content = @Content),
        @ApiResponse(responseCode = "404", description = "Event not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<EventResponse> updateEvent(
            @PathVariable("id") UUID eventId,
            @Valid @RequestBody CreateEventRequest request,
            @Parameter(hidden = true) @RequestHeader("X-User-Id") UUID organiserId) {
        EventResponse response = eventService.updateEvent(eventId, request, organiserId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Publish an event", description = "Transitions an event from DRAFT → PUBLISHED, making it visible to buyers.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Event published"),
        @ApiResponse(responseCode = "400", description = "Event cannot be published (e.g. no tiers)", content = @Content),
        @ApiResponse(responseCode = "403", description = "Not the event organiser", content = @Content),
        @ApiResponse(responseCode = "404", description = "Event not found", content = @Content)
    })
    @PatchMapping("/{id}/publish")
    public ResponseEntity<EventResponse> publishEvent(
            @PathVariable("id") UUID eventId,
            @Parameter(hidden = true) @RequestHeader("X-User-Id") UUID organiserId) {
        EventResponse response = eventService.publishEvent(eventId, organiserId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Cancel an event", description = "Transitions an event to CANCELLED status. Cannot be reversed.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Event cancelled"),
        @ApiResponse(responseCode = "403", description = "Not the event organiser", content = @Content),
        @ApiResponse(responseCode = "404", description = "Event not found", content = @Content)
    })
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<EventResponse> cancelEvent(
            @PathVariable("id") UUID eventId,
            @Parameter(hidden = true) @RequestHeader("X-User-Id") UUID organiserId) {
        EventResponse response = eventService.cancelEvent(eventId, organiserId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get sales summary", description = "Returns ticket sales stats per tier: total qty, remaining, sold, and revenue.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Sales summary returned"),
        @ApiResponse(responseCode = "403", description = "Not the event organiser", content = @Content),
        @ApiResponse(responseCode = "404", description = "Event not found", content = @Content)
    })
    @GetMapping("/{id}/summary")
    public ResponseEntity<SalesSummaryResponse> getSalesSummary(
            @PathVariable("id") UUID eventId,
            @Parameter(hidden = true) @RequestHeader("X-User-Id") UUID organiserId) {
        SalesSummaryResponse response = eventService.getSalesSummary(eventId, organiserId);
        return ResponseEntity.ok(response);
    }
}
