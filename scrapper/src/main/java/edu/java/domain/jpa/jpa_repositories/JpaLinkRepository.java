package edu.java.domain.jpa.jpa_repositories;

import edu.java.domain.jpa.entities.LinkEntity;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface JpaLinkRepository extends JpaRepository<LinkEntity, Long> {
    LinkEntity deleteByUrl(String url);

    Optional<LinkEntity> findByUrl(String url);

    List<LinkEntity> findAllByLastCheckTimeBefore(OffsetDateTime time);

    @Modifying
    @Transactional
    @Query(value = "UPDATE LinkEntity SET updatedAt = :updated_at, lastCheckTime = :last_check_time WHERE id = :id")
    void markNewUpdate(
        @Param("id") Long id,
        @Param("updated_at") OffsetDateTime updatedAt,
        @Param("last_check_time") OffsetDateTime lastCheckTime
    );

    @Modifying
    @Transactional
    @Query(value = "UPDATE LinkEntity SET lastCheckTime = :last_check_time WHERE id IN :id")
    void markNewCheck(
        @Param("id") List<Long> id,
        @Param("last_check_time") OffsetDateTime lastCheckTime
    );
}
