package fr.traqueur.shared.api.domain;

import java.util.Map;

public record Schema(Map<String, Map<String, String>> tables) {

    public void addTable(String name, Map<String, String> table) {
        this.tables.put(name, table);
    }

}
