package edu.java.bot.controller;

import edu.java.bot.api.UpdateControllerApi;
import edu.java.bot.service.UpdateService;
import edu.java.model.request.LinkUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UpdateController implements UpdateControllerApi {

    private final UpdateService updateService;

    @Override
    public ResponseEntity<Void> sendUpdate(LinkUpdate dto) {
        return updateService.sendUpdate(dto);
    }
}
