package com.myresume.site.languageSection;

import com.myresume.common.entity.LanguageSection;
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
public class LanguageSectionService {
    public static final int LANGUAGES_PER_PAGE = 4;

    @Autowired
    private LanguageSectionRepository languageSectionRepository;

    public List<LanguageSection> listAll() {
        return(List<LanguageSection>) languageSectionRepository.findAll(Sort.by("language").ascending());
    }

    public List<LanguageSection> findAllActiveRecords(Integer userId) {
        return(List<LanguageSection>) languageSectionRepository.findAllActiveRecords(userId);
    }

    public Page<LanguageSection> listByPage(int pageNum, String sortField, String sortDir, String keyword, Integer userId) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNum - 1, LANGUAGES_PER_PAGE, sort);
        if(keyword != null) {
            return languageSectionRepository.findAll(userId,keyword,pageable);
        }
        return languageSectionRepository.findAll(userId,pageable);
    }

    public LanguageSection save(LanguageSection LanguageSection) {
//			boolean isUpdatingAboutSection = (aboutsection.getId()!= null);
//
//			if(isUpdatingAboutSection) {
//				AboutSection existingAboutSection = repo.findById(aboutsection.getId()).get();
//			}
        return languageSectionRepository.save(LanguageSection);
    }

    public LanguageSection get(Integer id) throws LanguageSectionNotFoundException {
        try {
            return languageSectionRepository.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new LanguageSectionNotFoundException("Could not find any language section with ID "+id);
        }
    }

    public void delete(Integer id) throws LanguageSectionNotFoundException {
        Long countById = languageSectionRepository.countById(id);
        if(countById ==null || countById ==0) {
            throw new LanguageSectionNotFoundException("Could not find any language section with ID "+id);
        }

        languageSectionRepository.deleteById(id);
    }

    public void updateEnabled(Integer id, boolean enabled) {
        languageSectionRepository.updateEnabled(id, enabled);
    }
}
