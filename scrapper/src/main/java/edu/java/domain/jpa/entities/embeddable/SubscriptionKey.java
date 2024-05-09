package edu.java.domain.jpa.entities.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionKey implements Serializable {
    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "link_id")
    private Long linkId;

    @Override public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        SubscriptionKey that = (SubscriptionKey) object;

        return Objects.equals(getChatId(), that.getChatId()) && Objects.equals(getLinkId(), that.getLinkId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getChatId(), getLinkId());
    }
}
