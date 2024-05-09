package edu.java.domain.jpa.entities;

import edu.java.domain.jpa.entities.embeddable.SubscriptionKey;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "subscription")
@Getter
@Setter
public class SubscriptionEntity {
    @EmbeddedId
    SubscriptionKey subscriptionKey;

    @ManyToOne
    @MapsId("chatId")
    @JoinColumn(name = "chat_id")
    ChatEntity chat;

    @ManyToOne
    @MapsId("linkId")
    @JoinColumn(name = "link_id")
    LinkEntity link;
}
