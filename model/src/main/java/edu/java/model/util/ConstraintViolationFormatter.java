package edu.java.model.util;

import java.util.Map;

public class ConstraintViolationFormatter {

    private ConstraintViolationFormatter() {
    }

    public static String getDescription(Map<String, String> violations) {
        return "Constraint violation" + violations;
    }
}
