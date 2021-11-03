package com.myresume.site.workSection;

import com.myresume.common.entity.WorkSection;
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
public class WorkSectionService {

    public static final int SECTIONS_PER_PAGE = 4;

    @Autowired
    private WorkSectionRepository workSectionRepository;

    public List<WorkSection> listAll() {
        return(List<WorkSection>) workSectionRepository.findAll(Sort.by("fromDate").descending());
    }

    public List<WorkSection> findAllActiveRecords(Integer userId) {
        return(List<WorkSection>) workSectionRepository.findAllActiveRecords(userId);
    }

    public Page<WorkSection> listByPage(int pageNum, String sortField, String sortDir, String keyword, Integer userId) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();


        Pageable pageable = PageRequest.of(pageNum - 1, SECTIONS_PER_PAGE, sort);

        if(keyword != null) {
            return workSectionRepository.findAll(userId,keyword,pageable);
        }
        return workSectionRepository.findAll(userId,pageable);
    }

    public WorkSection save(WorkSection workSection) {
        return workSectionRepository.save(workSection);
    }

    public WorkSection get(Integer id) throws WorkSectionNotFoundException {
        try {
            return workSectionRepository.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new WorkSectionNotFoundException("Could not find any work section with ID "+id);
        }
    }

    public void delete(Integer id) throws WorkSectionNotFoundException {
        Long countById = workSectionRepository.countById(id);
        if(countById ==null || countById ==0) {
            throw new WorkSectionNotFoundException("Could not find any work section with ID "+id);
        }

        workSectionRepository.deleteById(id);
    }

    public void updateEnabled(Integer id, boolean enabled) {
        workSectionRepository.updateEnabled(id, enabled);
    }

}
