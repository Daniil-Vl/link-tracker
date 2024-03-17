package edu.java.service;

public interface TgChatService {
    /**
     * Register user
     *
     * @param tgChatId - user's telegram chat id
     */
    void register(Long tgChatId);

    /**
     * Removes user from database
     *
     * @param tgChatId - user's telegram chat id
     */
    void unregister(Long tgChatId);

    /**
     * Checks if user authenticated
     *
     * @param tgChatId - user's telegram chat id
     * @return true if authenticated, otherwise false
     */
    boolean isAuthenticated(Long tgChatId);
}
