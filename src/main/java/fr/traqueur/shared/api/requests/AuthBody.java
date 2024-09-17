package fr.traqueur.shared.api.requests;

import java.util.UUID;

public record AuthBody(UUID server, UUID plugin) {}
