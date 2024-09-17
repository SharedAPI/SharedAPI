package fr.traqueur.shared.api.requests.body;

import java.util.UUID;

public record AuthBody(UUID server, UUID plugin) {}
