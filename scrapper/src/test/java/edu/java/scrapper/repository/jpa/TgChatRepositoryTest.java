package edu.java.scrapper.repository.jpa;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.entity.TgChat;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TgChatRepositoryTest extends IntegrationTest {

    @Autowired
    private TgChatRepository tgChatRepository;

    @Test
    @Transactional
    @Rollback
    void save() {
        tgChatRepository.save(new TgChat(1L));

        List<TgChat> actual = tgChatRepository.findAll();

        List<TgChat> expected = List.of(new TgChat(1L));
        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    @Rollback
    void findById_Found() {
        tgChatRepository.save(new TgChat(1L));

        Optional<TgChat> actual = tgChatRepository.findById(1L);

        Optional<TgChat> expected = Optional.of(new TgChat(1L));
        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    @Rollback
    void findById_NotFound() {
        Optional<TgChat> actual = tgChatRepository.findById(1L);

        assertThat(actual).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    void existsById_ReturnsTrue() {
        tgChatRepository.save(new TgChat(1L));

        boolean exists = tgChatRepository.existsById(1L);

        assertThat(exists).as("checks that Telegram chat exists").isTrue();
    }

    @Test
    @Transactional
    @Rollback
    void existsById_ReturnsFalse() {
        boolean exists = tgChatRepository.existsById(1L);

        assertThat(exists).as("checks that Telegram chat does not exist").isFalse();
    }

    @Test
    @Transactional
    @Rollback
    void findAll() {
        tgChatRepository.save(new TgChat(1L));
        tgChatRepository.save(new TgChat(2L));

        List<TgChat> actual = tgChatRepository.findAll();

        List<TgChat> expected = List.of(
            new TgChat(1L),
            new TgChat(2L)
        );
        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    @Rollback
    void deleteById() {
        tgChatRepository.save(new TgChat(1L));

        tgChatRepository.deleteById(1L);

        boolean isDeleted = !tgChatRepository.existsById(1L);
        assertThat(isDeleted).as("checks that Telegram chat has been deleted").isTrue();
    }
}
