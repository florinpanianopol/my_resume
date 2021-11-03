package com.myresume.site.educationSection;

import com.myresume.common.entity.EducationSection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class EducationSectionService {
	
		public static final int SECTIONS_PER_PAGE = 4;
	
		@Autowired
		private EducationSectionRepository repo;
		
		public List<EducationSection> listAll() {
				return(List<EducationSection>) repo.findAll(Sort.by("id").descending());
		}
		
		public List<EducationSection> findAllActiveRecords(Integer userId) {
			return(List<EducationSection>) repo.findAllActiveRecords(userId);
		}
		
		public Page<EducationSection> listByPage(int pageNum, String sortField, String sortDir, String keyword,Integer userId) {
			Sort sort = Sort.by(sortField);
			sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
			
			
			Pageable pageable = PageRequest.of(pageNum - 1, SECTIONS_PER_PAGE, sort);
			
			if(keyword != null) {
				return repo.findAll(userId,keyword,pageable);
			}
			return repo.findAll(userId,pageable);
		}
		
		public EducationSection save(EducationSection educationSection) {
			return repo.save(educationSection);
		}
		
		public EducationSection get(Integer id) throws EducationSectionNotFoundException {
			try {
			return repo.findById(id).get();
			} catch (NoSuchElementException ex) {
				throw new EducationSectionNotFoundException("Could not find any education section with ID "+id);
			}
		}
		
		public void delete(Integer id) throws EducationSectionNotFoundException {
			Long countById = repo.countById(id);
			if(countById ==null || countById ==0) {
				throw new EducationSectionNotFoundException("Could not find any education section with ID "+id);
			}
			
			repo.deleteById(id);
		}
		
		public void updateEnabled(Integer id, boolean currInd) {
			repo.updateEnabled(id, currInd);
		}

}