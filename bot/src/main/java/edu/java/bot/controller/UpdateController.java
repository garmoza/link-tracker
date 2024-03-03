package edu.java.bot.controller;

import edu.java.bot.service.UpdateService;
import edu.java.model.request.LinkUpdate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/updates")
@RequiredArgsConstructor
@Validated
public class UpdateController {

    private final UpdateService updateService;

    @PostMapping
    public ResponseEntity<Void> sendUpdate(@Valid @RequestBody LinkUpdate dto) {
        return updateService.sendUpdate(dto);
    }
}
