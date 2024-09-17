package fr.traqueur.shared.api.domain;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public interface Table {

    String getName();

    static <T extends Table> Map<String, String> getColumns(Class<T> table) {
        Map<String,String> columns = new HashMap<>();
        Arrays.asList(table.getRecordComponents()).forEach(component -> {
            String[] splitType = component.getType().getName().split("\\.");
            columns.put(component.getName(), splitType[splitType.length - 1]);
        });
        return columns;
    }

}
