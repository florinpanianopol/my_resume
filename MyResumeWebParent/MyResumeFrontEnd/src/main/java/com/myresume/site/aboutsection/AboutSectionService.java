package com.myresume.site.aboutsection;

import com.myresume.common.entity.AboutSection;
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
public class AboutSectionService {
	
		public static final int USERS_PER_PAGE = 4;
	
		@Autowired
		private AboutSectionRepository repo;
		
		public List<AboutSection> listAll() {
				return(List<AboutSection>) repo.findAll(Sort.by("name").ascending());
		}
		
		public List<AboutSection> findAllActiveRecords(Integer userId) {
			return(List<AboutSection>) repo.findAllActiveRecords(userId);
		}
		
		public Page<AboutSection> listByPage(int pageNum, String sortField, String sortDir, String keyword,Integer userId) {
			Sort sort = Sort.by(sortField);
			sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
			
			
			Pageable pageable = PageRequest.of(pageNum - 1, USERS_PER_PAGE, sort);
			
			if(keyword != null) {
				return repo.findAll(userId,keyword,pageable);
			}
			return repo.findAll(userId,pageable);
		}
		
		public AboutSection save(AboutSection aboutsection) {
//			boolean isUpdatingAboutSection = (aboutsection.getId()!= null);
//			
//			if(isUpdatingAboutSection) {
//				AboutSection existingAboutSection = repo.findById(aboutsection.getId()).get();
//			}
			return repo.save(aboutsection);
		}
		
		public AboutSection get(Integer id) throws AboutSectionNotFoundException {
			try {
			return repo.findById(id).get();
			} catch (NoSuchElementException ex) {
				throw new AboutSectionNotFoundException("Could not find any about section with ID "+id);
			}
		}
		
		public void delete(Integer id) throws AboutSectionNotFoundException {
			Long countById = repo.countById(id);
			if(countById ==null || countById ==0) {
				throw new AboutSectionNotFoundException("Could not find any about section with ID "+id);
			}
			
			repo.deleteById(id);
		}
		
		public void updateCurrentInd(Integer id, boolean currInd) {
			repo.updateCurrentInd(id, currInd);
		}

}