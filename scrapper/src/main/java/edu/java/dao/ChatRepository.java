package edu.java.dao;

import java.util.List;

public interface ChatRepository {
    /**
     * Add new chat to the database
     *
     * @param chatId - telegram chat id
     * @return number of rows affected
     */
    int add(Long chatId);

    /**
     * Remove chats with given params
     *
     * @param chatId - telegram chat id
     * @return number of removed rows
     */
    int remove(Long chatId);

    /**
     * Checks if tg chat registered
     *
     * @param chatId - telegram chat id
     * @return true if registered, otherwise false
     */
    boolean isExists(Long chatId);

    /**
     * Retrieve all chats
     *
     * @return list of all chats
     */
    List<Long> findAll();
}
