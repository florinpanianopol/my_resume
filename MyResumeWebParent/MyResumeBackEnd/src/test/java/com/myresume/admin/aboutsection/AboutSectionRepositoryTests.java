package com.myresume.admin.aboutsection;

import com.myresume.common.entity.AboutSection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace=Replace.NONE)
@Rollback(false)
public class AboutSectionRepositoryTests {

	 @Autowired
	    private AboutSectionRepository repo;

	    @Test
	    public void testCreateFirstRow(){
	    	boolean flag = false;
	    	ArrayList<AboutSection> tempList = new ArrayList<AboutSection>(); 
	    	Iterable<AboutSection> listRows = repo.findAll();
	    	listRows.forEach(row -> tempList.add(row));
	    	
	        AboutSection aboutTestRow = new AboutSection("Florin","About mASDSAD2e","I am pizza delivery guy","QA Manual","I qa the shit out of things",
	                "linkedin","Bucharest","Developer","Thats all there is","john@yahoo.com",false,1);
	        
	        for(int i=0;i<tempList.size();i++) {
	        	if(tempList.get(i).getCurrInd() && aboutTestRow.getCurrInd()) {
	        		flag = true;
	        		break;
	        	}
	        }
	        
	        if(flag) {
	        	System.out.println("error");
	        }
	        else {
	        AboutSection savedRow = repo.save(aboutTestRow);
  	        assertThat(savedRow.getId()).isGreaterThan(0);
	        }
	      
	    }
	    
	    @Test
	    public void testCreateMultipleRows(){
	    	
	    	AboutSection aboutTestRow1 = new AboutSection("Florin","About me","I am police offer","LAPD","I qa the shit out of things",
	                "linkedin","Pitesti","Developer","Thats all there is","marcus@yahoo.com",false,1);
	    	AboutSection aboutTestRow2 = new AboutSection("Florin","About me","I am fireman","LAPD","I qa the shit out of things",
	                "linkedin","Pitesti","Developer","Thats all there is","marcus@yahoo.com",false,1);
	    	
	    	repo.saveAll(List.of(aboutTestRow1,aboutTestRow2));
	    }
	    
	    @Test
	    public void testListAllRows() {
	    	Iterable<AboutSection> listRows = repo.findAll();
	    	listRows.forEach(row -> System.out.println(row));
	    }
	    
	    @Test
	    public void testGetRowById() {
	    	AboutSection row1 = repo.findById(1).get();
	    	System.out.println(row1);
	    	assertThat(row1).isNotNull();
	    }
	    
	    @Test
	    public void testCountById() {
	    	Integer id = 1;
	    	Long countById = repo.countById(id);
	    	
	    	assertThat(countById).isNotNull().isGreaterThan(0);
	    }
	    
	    @Test
	    public void testListFirstPage() {
	    	int pageNumber = 0;
	    	int pageSize = 4;
	    	Pageable pageable = PageRequest.of(pageNumber,pageSize);
	    	Page<AboutSection> page = repo.findAll(pageable);
	    	
	    	List<AboutSection> listAboutSections = page.getContent();
	    	listAboutSections.forEach(aboutsection -> System.out.println(aboutsection));
	    	
	    	assertThat(listAboutSections.size()).isEqualTo(pageSize);
	    }
	    
	    @Test
	    public void testSearchUsers() {
	    	String keyword = "bruce";
	    	Integer userId = 2;
	    	int pageNumber = 0;
	    	int pageSize = 4;
	    	
	    	Pageable pageable = PageRequest.of(pageNumber,pageSize);
	    	Page<AboutSection> page = repo.findAll(userId,keyword, pageable);
	    	
	    	List<AboutSection> listAboutSections = page.getContent();
	    	listAboutSections.forEach(aboutsection -> System.out.println(aboutsection));
	    	
	    	assertThat(listAboutSections.size()).isGreaterThan(0);
	    	
	    }
}
