package edu.java.scrapper.controller;

import edu.java.scrapper.service.TgChatService;
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
    void deleteChat_Ok() throws Exception {
        when(tgChatService.deleteChat(1L)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResultActions response = mockMvc.perform(delete("/tg-chat/1"));

        response.andExpect(status().isOk());
    }
}
