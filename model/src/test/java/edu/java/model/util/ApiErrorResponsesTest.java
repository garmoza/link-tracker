package edu.java.model.util;

import edu.java.model.response.ApiErrorResponse;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApiErrorResponsesTest {

    static class TestException extends RuntimeException {

        public TestException(String message) {
            super(message);
        }
    }

    @Spy
    private Exception e = new TestException("Message test");
    @Mock
    private StackTraceElement stackTraceElement;

    @Test
    void of() {
        when(stackTraceElement.toString()).thenReturn("stackTraceElement test");
        when(e.getStackTrace()).thenReturn(new StackTraceElement[] {stackTraceElement, stackTraceElement});

        ApiErrorResponse actual = ApiErrorResponses.of(e, 400, "Description test");

        ApiErrorResponse expected = ApiErrorResponse.builder()
            .code("400")
            .description("Description test")
            .exceptionName("edu.java.model.util.ApiErrorResponsesTest$TestException")
            .exceptionMessage("Message test")
            .stacktrace(List.of("stackTraceElement test", "stackTraceElement test"))
            .build();
        assertEquals(expected, actual);
    }
}
