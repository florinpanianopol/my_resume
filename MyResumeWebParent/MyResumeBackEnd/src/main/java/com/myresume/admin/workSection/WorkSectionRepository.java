package com.myresume.admin.workSection;

import com.myresume.common.entity.WorkSection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface WorkSectionRepository extends PagingAndSortingRepository<WorkSection,Integer> {

    public Long countById(Integer id);

    @Query("SELECT u FROM WorkSection u WHERE u.user_id=?1 AND (u.jobName LIKE %?2% OR u.jobDesc LIKE %?2%)")
    public Page<WorkSection> findAll(Integer userId, String keyword, Pageable pageable);

    @Query("UPDATE WorkSection u SET u.enabled=?2 WHERE u.id = ?1")
    @Modifying
    public void updateEnabled(Integer id, boolean enabled);

    @Query("SELECT u FROM WorkSection u WHERE u.enabled = 1 AND u.user_id=?1")
    Collection<WorkSection> findAllActiveRecords(Integer userId);

    @Query("SELECT u FROM WorkSection u WHERE u.user_id=?1")
    public Page<WorkSection> findAll(Integer userId, Pageable pageable);
}
