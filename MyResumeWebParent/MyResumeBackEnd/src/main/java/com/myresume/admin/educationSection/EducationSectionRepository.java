package com.myresume.admin.educationSection;

import com.myresume.common.entity.EducationSection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface EducationSectionRepository extends PagingAndSortingRepository<EducationSection,Integer> {
		
	public Long countById(Integer id);
	
	@Query("SELECT u FROM EducationSection u WHERE u.user_id=?1 AND (u.institutionName LIKE %?2% OR u.programType LIKE %?2%)")
	public Page<EducationSection> findAll(Integer userId,String keyword, Pageable pageable);
	
	@Query("UPDATE EducationSection u SET u.enabled=?2 WHERE u.id = ?1")
	@Modifying
	public void updateEnabled(Integer id, boolean enabled);
	
	@Query("SELECT u FROM EducationSection u WHERE u.enabled = 1 AND u.user_id=?1")
	Collection<EducationSection> findAllActiveRecords(Integer userId);

	@Query("SELECT u FROM EducationSection u WHERE u.user_id=?1")
	public Page<EducationSection> findAll(Integer userId, Pageable pageable);


}
