package edu.java.bot.service;

import edu.java.model.request.LinkUpdate;
import org.springframework.http.ResponseEntity;

public interface UpdateService {

    ResponseEntity<Void> sendUpdate(LinkUpdate dto);
}
