package edu.java.dao;

import edu.java.dto.dao.LinkDto;
import java.time.Duration;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface LinkRepository {
    /**
     * Add a new link to the database
     *
     * @param url - link url
     * @return number of affected rows
     */
    @Transactional
    int add(String url);

    /**
     * Remove the link with given id
     *
     * @param linkId - link id
     * @return number of affected rows
     */
    @Transactional
    int remove(Long linkId);

    /**
     * Remove the link with given url
     *
     * @param url - link rul
     * @return number of affected rows
     */
    @Transactional
    int remove(String url);

    /**
     * Retrieve all links
     *
     * @return list of all links
     */
    List<LinkDto> findAll();

    /**
     * Retrieve all links, that were checked more than a certain time ago
     *
     * @param interval - time, used in filter
     * @return list of long-standing links
     */
    List<LinkDto> findAll(Duration interval);
}
