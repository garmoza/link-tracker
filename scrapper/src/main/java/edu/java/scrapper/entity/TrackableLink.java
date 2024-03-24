package edu.java.scrapper.entity;

import java.time.OffsetDateTime;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrackableLink {
    private String url;
    private OffsetDateTime lastChange;
    private OffsetDateTime lastCrawl;

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TrackableLink that = (TrackableLink) o;

        if (!Objects.equals(url, that.url)) {
            return false;
        }
        if (!Objects.equals(lastChange, that.lastChange)) {
            return false;
        }
        return Objects.equals(lastCrawl, that.lastCrawl);
    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + (lastChange != null ? lastChange.hashCode() : 0);
        result = 31 * result + (lastCrawl != null ? lastCrawl.hashCode() : 0);
        return result;
    }
}
