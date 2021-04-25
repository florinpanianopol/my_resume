package com.myresume.admin.aboutsection;

import com.myresume.common.entity.AboutSection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface AboutSectionRepository extends PagingAndSortingRepository<AboutSection,Integer> {
		
	public Long countById(Integer id);
	
	@Query("SELECT u FROM AboutSection u WHERE u.header LIKE %?1% OR u.name LIKE %?1% OR u.email LIKE %?1%")
	public Page<AboutSection> findAll(String keyword, Pageable pageable);
	
	@Query("UPDATE AboutSection u SET u.currInd=?2 WHERE u.id = ?1")
	@Modifying
	public void updateCurrentInd(Integer id, boolean currInd);
	
	@Query("SELECT u FROM AboutSection u WHERE u.currInd = 1")
	Collection<AboutSection> findAllActiveRecords();
}
