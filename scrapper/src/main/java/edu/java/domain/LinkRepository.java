package edu.java.domain;

import edu.java.dto.dao.LinkDto;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface LinkRepository {
    /**
     * Add a new link to the database
     *
     * @param url - link url
     * @return inserted link
     */
    LinkDto add(String url);

    /**
     * Remove the link with given id
     *
     * @param linkId - link id
     * @return removed link
     */
    Optional<LinkDto> remove(Long linkId);

    /**
     * Remove the link with given url
     *
     * @param url - link rul
     * @return removed link
     */
    Optional<LinkDto> remove(String url);

    /**
     * Find the link with given id
     *
     * @param linkId - link's id
     * @return link with given id, if it exists, otherwise empty
     */
    Optional<LinkDto> findById(Long linkId);

    /**
     * Find the link with given url
     *
     * @param url - link's url
     * @return link with given url, if it exists, otherwise empty
     */
    Optional<LinkDto> findByUrl(String url);

    /**
     * Return all links having id in a given list
     *
     * @param setOfLinksId - set of links id
     * @return list of links
     */
    List<LinkDto> findAllById(Set<Long> setOfLinksId);

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

    /**
     * Modify updateAt link's field in db
     *
     * @param linkId       - links to modify
     * @param newUpdatedAt - new updatedAt value
     * @return number of affected rows
     */
    int markNewUpdate(Long linkId, OffsetDateTime newUpdatedAt);

    /**
     * Updates last_check_time field in db
     *
     * @param linkIds           - link to update
     * @param newLastCheckTime - new last_check_time value
     * @return number of affected rows
     */
    int markNewCheck(List<Long> linkIds, OffsetDateTime newLastCheckTime);
}
