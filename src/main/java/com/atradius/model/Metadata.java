package com.atradius.model;

import lombok.Builder;

import java.util.Optional;
import java.util.UUID;


@Builder
public record Metadata(
    String eventName,
    String eventDomain,
    String eventType,
    Optional<String> origin,
    Optional<UUID> correlationId) {}
