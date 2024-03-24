package edu.java.scrapper.entity.mapper;

import edu.java.model.response.TgChatResponse;
import edu.java.scrapper.entity.TgChat;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TgChatModelMapperTest {

    @Test
    void toTgChatResponse() {
        TgChat entity = new TgChat(2L);

        TgChatResponse actual = TgChatModelMapper.toTgChatResponse(entity);

        TgChatResponse expected = new TgChatResponse(2L);
        assertEquals(expected, actual);
    }

    @Test
    void toTgChatResponse_EntityIsNull() {
        TgChatResponse actual = TgChatModelMapper.toTgChatResponse(null);

        assertNull(actual);
    }
}
