package edu.java.scrapper.entity.mapper;

import edu.java.model.response.TgChatResponse;
import edu.java.scrapper.entity.TgChat;

public class TgChatModelMapper {

    private TgChatModelMapper() {
    }

    public static TgChatResponse toTgChatResponse(TgChat entity) {
        if (entity == null) {
            return null;
        }

        return TgChatResponse.builder()
            .id(entity.getId())
            .build();
    }
}
