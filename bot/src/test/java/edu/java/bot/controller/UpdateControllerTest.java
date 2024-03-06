package edu.java.bot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.bot.service.UpdateService;
import edu.java.model.request.LinkUpdate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.bind.MethodArgumentNotValidException;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({UpdateController.class})
@ExtendWith(MockitoExtension.class)
class UpdateControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UpdateService updateService;

    @Test
    void sendUpdates_Ok() throws Exception {
        when(updateService.sendUpdate(any())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        var requestBody = new LinkUpdate(1L, "url-test", "description-test", List.of(2L));
        ResultActions response = mockMvc.perform(post("/updates")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestBody)));

        response.andExpect(status().isOk());
    }

    @Test
    void sendUpdates_Validation_Body() throws Exception {
        ResultActions response = mockMvc.perform(post("/updates")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{}"));

        response.andExpect(status().isBadRequest())
            .andExpect(result -> assertInstanceOf(
                MethodArgumentNotValidException.class,
                result.getResolvedException()
            ));
    }
}
