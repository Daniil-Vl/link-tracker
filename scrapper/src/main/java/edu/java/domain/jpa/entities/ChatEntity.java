package edu.java.domain.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "chat")
@Getter
@Setter
public class ChatEntity {
    @Id
    @Column(name = "chat_id")
    Long chatId;

    @OneToMany(mappedBy = "chat", fetch = FetchType.EAGER)
    Set<SubscriptionEntity> subscriptions;
}
