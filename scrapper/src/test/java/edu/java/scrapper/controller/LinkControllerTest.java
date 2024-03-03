package edu.java.scrapper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.model.request.AddLinkRequest;
import edu.java.model.request.RemoveLinkRequest;
import edu.java.model.response.LinkResponse;
import edu.java.model.response.ListLinksResponse;
import edu.java.scrapper.service.LinkService;
import jakarta.validation.ConstraintViolationException;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({LinkController.class})
@ExtendWith(MockitoExtension.class)
class LinkControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LinkService linkService;

    @Test
    void getAllLinks_Ok() throws Exception {
        var responseBody = ListLinksResponse.builder()
            .links(List.of(
                new LinkResponse(1L, URI.create("https://example1.com")),
                new LinkResponse(2L, URI.create("https://example2.com"))
            ))
            .size(2)
            .build();
        when(linkService.getAllLinks(any(Long.class))).thenReturn(ResponseEntity.ok(responseBody));

        ResultActions response = mockMvc.perform(get("/links")
            .accept(MediaType.APPLICATION_JSON)
            .header("Tg-Chat-Id", 123L));

        String expected = """
            {
                "links": [
                    {
                        "id": 1,
                        "url": "https://example1.com"
                    },
                    {
                        "id": 2,
                        "url": "https://example2.com"
                    }
                ],
                "size": 2
            }
            """;
        response.andExpect(status().isOk())
            .andExpect(content().json(expected));
    }

    @Test
    void getAllLinks_HeaderTgChatId_Missing() throws Exception {
        ResultActions response = mockMvc.perform(get("/links")
            .accept(MediaType.APPLICATION_JSON));

        response.andExpect(status().isBadRequest())
            .andExpect(result -> assertInstanceOf(
                MissingRequestHeaderException.class,
                result.getResolvedException()
            ));
    }

    @Test
    void getAllLinks_HeaderTgChatId_NegativeId() throws Exception {
        ResultActions response = mockMvc.perform(get("/links")
            .accept(MediaType.APPLICATION_JSON)
            .header("Tg-Chat-Id", "-2"));

        response.andExpect(status().isBadRequest())
            .andExpect(result -> assertInstanceOf(
                ConstraintViolationException.class,
                result.getResolvedException()
            ));
    }

    @Test
    void getAllLinks_HeaderTgChatId_NotLongId() throws Exception {
        ResultActions response = mockMvc.perform(get("/links")
            .accept(MediaType.APPLICATION_JSON)
            .header("Tg-Chat-Id", "not-long"));

        response.andExpect(status().isBadRequest())
            .andExpect(result -> assertInstanceOf(
                MethodArgumentTypeMismatchException.class,
                result.getResolvedException()
            ));
    }

    @Test
    void addLink_Ok() throws Exception {
        var responseBody = new LinkResponse(1L, URI.create("https://example.com"));
        when(linkService.addLink(any(Long.class), any())).thenReturn(ResponseEntity.ok(responseBody));

        var requestBody = new AddLinkRequest("https://example.com");
        ResultActions response = mockMvc.perform(post("/links")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Tg-Chat-Id", 123L)
            .content(objectMapper.writeValueAsString(requestBody)));

        String expected = """
            {
                "id": 1,
                "url": "https://example.com"
            }
            """;
        response.andExpect(status().isOk())
            .andExpect(content().json(expected));
    }

    @Test
    void addLink_HeaderTgChatId_Missing() throws Exception {
        var requestBody = new AddLinkRequest("https://example.com");
        ResultActions response = mockMvc.perform(post("/links")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestBody)));

        response.andExpect(status().isBadRequest())
            .andExpect(result -> assertInstanceOf(
                MissingRequestHeaderException.class,
                result.getResolvedException()
            ));
    }

    @Test
    void addLink_HeaderTgChatId_NegativeId() throws Exception {
        var requestBody = new AddLinkRequest("https://example.com");
        ResultActions response = mockMvc.perform(post("/links")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestBody))
            .header("Tg-Chat-Id", "-2"));

        response.andExpect(status().isBadRequest())
            .andExpect(result -> assertInstanceOf(
                ConstraintViolationException.class,
                result.getResolvedException()
            ));
    }

    @Test
    void addLink_HeaderTgChatId_NotLongId() throws Exception {
        var requestBody = new AddLinkRequest("https://example.com");
        ResultActions response = mockMvc.perform(post("/links")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestBody))
            .header("Tg-Chat-Id", "not-long"));

        response.andExpect(status().isBadRequest())
            .andExpect(result -> assertInstanceOf(
                MethodArgumentTypeMismatchException.class,
                result.getResolvedException()
            ));
    }

    @Test
    void addLink_Validation_Body() throws Exception {
        ResultActions response = mockMvc.perform(post("/links")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Tg-Chat-Id", "1")
            .content("{}"));

        response.andExpect(status().isBadRequest())
            .andExpect(result -> assertInstanceOf(
                MethodArgumentNotValidException.class,
                result.getResolvedException()
            ));
    }

    @Test
    void deleteLink_Ok() throws Exception {
        var responseBody = new LinkResponse(1L, URI.create("https://example.com"));
        when(linkService.deleteLink(any(Long.class), any())).thenReturn(ResponseEntity.ok(responseBody));

        var requestBody = new RemoveLinkRequest("https://example.com");
        ResultActions response = mockMvc.perform(delete("/links")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Tg-Chat-Id", 123L)
            .content(objectMapper.writeValueAsString(requestBody)));

        String expected = """
            {
                "id": 1,
                "url": "https://example.com"
            }
            """;
        response.andExpect(status().isOk())
            .andExpect(content().json(expected));
    }

    @Test
    void deleteLink_HeaderTgChatId_Missing() throws Exception {
        var requestBody = new RemoveLinkRequest("https://example.com");
        ResultActions response = mockMvc.perform(delete("/links")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestBody)));

        response.andExpect(status().isBadRequest())
            .andExpect(result -> assertInstanceOf(
                MissingRequestHeaderException.class,
                result.getResolvedException()
            ));
    }

    @Test
    void deleteLink_HeaderTgChatId_NegativeId() throws Exception {
        var requestBody = new RemoveLinkRequest("https://example.com");
        ResultActions response = mockMvc.perform(delete("/links")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestBody))
            .header("Tg-Chat-Id", "-2"));

        response.andExpect(status().isBadRequest())
            .andExpect(result -> assertInstanceOf(
                ConstraintViolationException.class,
                result.getResolvedException()
            ));
    }

    @Test
    void deleteLink_HeaderTgChatId_NotLongId() throws Exception {
        var requestBody = new RemoveLinkRequest("https://example.com");
        ResultActions response = mockMvc.perform(delete("/links")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestBody))
            .header("Tg-Chat-Id", "not-long"));

        response.andExpect(status().isBadRequest())
            .andExpect(result -> assertInstanceOf(
                MethodArgumentTypeMismatchException.class,
                result.getResolvedException()
            ));
    }

    @Test
    void deleteLink_Validation_Body() throws Exception {
        ResultActions response = mockMvc.perform(delete("/links")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Tg-Chat-Id", "1")
            .content("{}"));

        response.andExpect(status().isBadRequest())
            .andExpect(result -> assertInstanceOf(
                MethodArgumentNotValidException.class,
                result.getResolvedException()
            ));
    }
}
