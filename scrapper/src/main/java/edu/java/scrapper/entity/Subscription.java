package edu.java.scrapper.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "subscribe")
public class Subscription {

    @EmbeddedId
    private Id id = new Id();

    @Column(name = "last_update")
    private OffsetDateTime lastUpdate;

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Subscription that = (Subscription) o;

        if (!Objects.equals(id, that.id)) {
            return false;
        }
        return Objects.equals(lastUpdate, that.lastUpdate);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (lastUpdate != null ? lastUpdate.hashCode() : 0);
        return result;
    }

    @Embeddable
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Id implements Serializable {

        @Column(name = "tg_chat_id")
        private long chatId;

        @Column(name = "trackable_link_url")
        private String linkUrl;

        @Override public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Id id = (Id) o;

            if (chatId != id.chatId) {
                return false;
            }
            return Objects.equals(linkUrl, id.linkUrl);
        }

        @Override
        public int hashCode() {
            int result = (int) (chatId ^ (chatId >>> 32));
            result = 31 * result + (linkUrl != null ? linkUrl.hashCode() : 0);
            return result;
        }
    }
}
