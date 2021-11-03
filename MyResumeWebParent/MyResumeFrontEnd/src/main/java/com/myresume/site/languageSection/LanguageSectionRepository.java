package com.myresume.site.languageSection;

import com.myresume.common.entity.LanguageSection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Collection;

public interface LanguageSectionRepository extends PagingAndSortingRepository<LanguageSection,Integer> {

    public Long countById(Integer id);

    @Query("SELECT u FROM LanguageSection u WHERE u.user_id=?1 AND (u.language LIKE %?2%)")
    public Page<LanguageSection> findAll(Integer userId, String keyword, Pageable pageable);

    @Query("UPDATE LanguageSection u SET u.enabled=?2 WHERE u.id = ?1")
    @Modifying
    public void updateEnabled(Integer id, boolean currInd);

    @Query("SELECT u FROM LanguageSection u WHERE u.enabled = 1 AND u.user_id=?1")
    Collection<LanguageSection> findAllActiveRecords(Integer userId);

    @Query("SELECT u FROM LanguageSection u WHERE u.user_id=?1")
    public Page<LanguageSection> findAll(Integer userId, Pageable pageable);
}
