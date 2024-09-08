package fr.traqueur.shared.api.domain;

import java.util.UUID;

public record AuthBody(UUID server, UUID plugin) {}
