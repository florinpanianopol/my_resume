package com.myresume.admin.skillsection;


import com.myresume.admin.aboutsection.AboutSectionNotFoundException;
import com.myresume.common.entity.SkillsSection;
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
public class SkillsSectionService {
    public static final int SKILLS_PER_PAGE = 4;

    @Autowired
    private SkillsSectionRepository skillsRepo;

    public List<SkillsSection> listAll() {
        return(List<SkillsSection>) skillsRepo.findAll(Sort.by("skillTitle").ascending());
    }

    public List<SkillsSection> findAllActiveRecords() {
        return(List<SkillsSection>) skillsRepo.findAllActiveRecords();
    }

    public Page<SkillsSection> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();


        Pageable pageable = PageRequest.of(pageNum - 1, SKILLS_PER_PAGE, sort);

        if(keyword != null) {
            return skillsRepo.findAll(keyword,pageable);
        }
        return skillsRepo.findAll(pageable);
    }

    public SkillsSection save(SkillsSection skillsSection) {
//			boolean isUpdatingAboutSection = (aboutsection.getId()!= null);
//
//			if(isUpdatingAboutSection) {
//				AboutSection existingAboutSection = repo.findById(aboutsection.getId()).get();
//			}
        return skillsRepo.save(skillsSection);
    }

    public SkillsSection get(Integer id) throws AboutSectionNotFoundException {
        try {
            return skillsRepo.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new AboutSectionNotFoundException("Could not find any about section with ID "+id);
        }
    }

    public void delete(Integer id) throws SkillsSectionNotFoundException {
        Long countById = skillsRepo.countById(id);
        if(countById ==null || countById ==0) {
            throw new SkillsSectionNotFoundException("Could not find any skill section with ID "+id);
        }

        skillsRepo.deleteById(id);
    }

    public void updateEnabled(Integer id, boolean enabled) {
        skillsRepo.updateEnabled(id, enabled);
    }



}
