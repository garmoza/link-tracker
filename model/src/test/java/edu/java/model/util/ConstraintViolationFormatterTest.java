package edu.java.model.util;

import java.util.Map;
import java.util.TreeMap;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ConstraintViolationFormatterTest {

    @Test
    void getDescription() {
        Map<String, String> violations = new TreeMap<>(
            Map.of("Name1", "Description1", "Name2", "Description2"));

        String actual = ConstraintViolationFormatter.getDescription(violations);

        String expected = "Constraint violation{Name1=Description1, Name2=Description2}";
        assertEquals(expected, actual);
    }
}
