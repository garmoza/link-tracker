package edu.java.bot.controller;

import edu.java.bot.dto.request.LinkUpdate;
import edu.java.bot.service.UpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/updates")
@RequiredArgsConstructor
public class UpdateController {

    private final UpdateService updateService;

    @PostMapping
    public ResponseEntity<Void> sendUpdate(@RequestBody LinkUpdate dto) {
        return updateService.sendUpdate(dto);
    }
}
