package edu.java.model.util;

import java.util.Map;

public class ConstraintViolationFormatter {

    private ConstraintViolationFormatter() {
    }

    public static String getDescription(Map<String, String> violations) {
        StringBuilder description = new StringBuilder("Constraint violation\n");
        for (var entry : violations.entrySet()) {
            description.append(entry.getKey());
            description.append(" - ");
            description.append(entry.getValue());
            description.append('\n');
        }
        return description.toString();
    }
}
