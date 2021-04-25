package com.myresume.admin.skillsection;


import com.myresume.common.entity.SkillsSection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface SkillsSectionRepository extends PagingAndSortingRepository<SkillsSection,Integer> {

    public Long countById(Integer id);

    @Query("SELECT u FROM SkillsSection u WHERE u.skillTitle LIKE %?1% OR u.skillDescription LIKE %?1%")
    public Page<SkillsSection> findAll(String keyword, Pageable pageable);

    @Query("UPDATE SkillsSection u SET u.enabled=?2 WHERE u.id = ?1")
    @Modifying
    public void updateEnabled(Integer id, boolean currInd);

    @Query("SELECT u FROM SkillsSection u WHERE u.enabled = 1")
    Collection<SkillsSection> findAllActiveRecords();
}
