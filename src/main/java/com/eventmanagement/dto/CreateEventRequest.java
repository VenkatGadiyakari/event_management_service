package com.eventmanagement.dto;

import com.eventmanagement.enums.EventCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Request body for creating or updating an event")
public class CreateEventRequest {

    @Schema(description = "Event title", example = "Rock Night 2025", maxLength = 255)
    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title cannot exceed 255 characters")
    private String title;

    @Schema(description = "Optional event description", example = "An electrifying rock concert featuring top bands.")
    private String description;

    @Schema(description = "Event category", example = "CONCERT")
    @NotNull(message = "Category is required")
    private EventCategory category;

    @Schema(description = "Date and time of the event (ISO-8601)", example = "2025-12-31T20:00:00")
    @NotNull(message = "Event date is required")
    private LocalDateTime eventDate;

    @Schema(description = "ID of the venue where the event is held", example = "b1c2d3e4-f5a6-7890-bcde-f12345678901")
    @NotNull(message = "Venue ID is required")
    private UUID venueId;

    @Schema(description = "URL of the event banner image", example = "https://cdn.example.com/banners/rock-night.jpg")
    private String bannerImageUrl;

    public CreateEventRequest() {
    }

    public CreateEventRequest(String title, String description, EventCategory category,
                              LocalDateTime eventDate, UUID venueId, String bannerImageUrl) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.eventDate = eventDate;
        this.venueId = venueId;
        this.bannerImageUrl = bannerImageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EventCategory getCategory() {
        return category;
    }

    public void setCategory(EventCategory category) {
        this.category = category;
    }

    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public UUID getVenueId() {
        return venueId;
    }

    public void setVenueId(UUID venueId) {
        this.venueId = venueId;
    }

    public String getBannerImageUrl() {
        return bannerImageUrl;
    }

    public void setBannerImageUrl(String bannerImageUrl) {
        this.bannerImageUrl = bannerImageUrl;
    }
}
