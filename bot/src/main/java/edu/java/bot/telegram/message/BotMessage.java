package edu.java.bot.telegram.message;

import java.util.Objects;

public record BotMessage(long userId, String text) {
    @Override public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        BotMessage that = (BotMessage) object;
        return userId == that.userId && Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, text);
    }
}
