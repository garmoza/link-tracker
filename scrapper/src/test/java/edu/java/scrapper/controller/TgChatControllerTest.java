package edu.java.scrapper.controller;

import edu.java.scrapper.exception.TgChatNotFoundException;
import edu.java.scrapper.service.TgChatService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({TgChatController.class})
@ExtendWith(MockitoExtension.class)
class TgChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TgChatService tgChatService;

    @Test
    void registerChat_Ok() throws Exception {
        when(tgChatService.registerChat(1L)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResultActions response = mockMvc.perform(post("/tg-chat/1"));

        response.andExpect(status().isOk());
    }

    @Test
    void registerChat_Validation_NegativeId() throws Exception {
        ResultActions response = mockMvc.perform(post("/tg-chat/-2"));

        response.andExpect(status().isBadRequest())
            .andExpect(result -> assertInstanceOf(
                ConstraintViolationException.class,
                result.getResolvedException()
            ));
    }

    @Test
    void registerChat_Validation_NotLongId() throws Exception {
        ResultActions response = mockMvc.perform(post("/tg-chat/not-long"));

        response.andExpect(status().isBadRequest())
            .andExpect(result -> assertInstanceOf(
                MethodArgumentTypeMismatchException.class,
                result.getResolvedException()
            ));
    }

    @Test
    void deleteChat_Ok() throws Exception {
        when(tgChatService.deleteChat(1L)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResultActions response = mockMvc.perform(delete("/tg-chat/1"));

        response.andExpect(status().isOk());
    }

    @Test
    void deleteChat_Validation_NegativeId() throws Exception {
        ResultActions response = mockMvc.perform(delete("/tg-chat/-2"));

        response.andExpect(status().isBadRequest())
            .andExpect(result -> assertInstanceOf(
                ConstraintViolationException.class,
                result.getResolvedException()
            ));
    }

    @Test
    void deleteChat_Validation_NotLongId() throws Exception {
        ResultActions response = mockMvc.perform(delete("/tg-chat/not-long"));

        response.andExpect(status().isBadRequest())
            .andExpect(result -> assertInstanceOf(
                MethodArgumentTypeMismatchException.class,
                result.getResolvedException()
            ));
    }

    @Test
    void deleteChat_NotFound() throws Exception {
        when(tgChatService.deleteChat(1L)).thenThrow(new TgChatNotFoundException(1L));
        ResultActions response = mockMvc.perform(delete("/tg-chat/1"));

        response.andExpect(status().isNotFound())
            .andExpect(result -> assertInstanceOf(
                TgChatNotFoundException.class,
                result.getResolvedException()
            ));
    }
}
