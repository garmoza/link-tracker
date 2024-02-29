package edu.java.bot.service.impl;

import edu.java.bot.dto.request.LinkUpdate;
import edu.java.bot.service.UpdateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UpdateServiceImpl implements UpdateService {

    @Override
    public ResponseEntity<Void> sendUpdate(LinkUpdate dto) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}
