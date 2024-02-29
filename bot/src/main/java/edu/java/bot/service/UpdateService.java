package edu.java.bot.service;

import edu.java.bot.dto.request.LinkUpdate;
import org.springframework.http.ResponseEntity;

public interface UpdateService {

    ResponseEntity<Void> sendUpdates(LinkUpdate dto);
}
