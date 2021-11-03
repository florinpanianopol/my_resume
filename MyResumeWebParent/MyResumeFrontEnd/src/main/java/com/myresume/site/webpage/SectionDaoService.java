package com.myresume.site.webpage;


import com.myresume.common.entity.AboutSection;
import com.myresume.common.entity.SkillsSection;
import com.myresume.common.entity.User;
import com.myresume.site.aboutsection.AboutSectionService;
import com.myresume.site.skillsection.SkillsSectionService;
import com.myresume.site.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SectionDaoService {
    @Autowired
    private AboutSectionService aboutService;

    @Autowired
    private UserService userService;

    @Autowired
    private SkillsSectionService skillService;


    private static final List<AboutSection> abSections = new ArrayList<>();
    private static final List<SkillsSection> skillsSections = new ArrayList<>();

    public List<AboutSection> findAllAboutSections(){
        abSections.clear();
        List<User> listUsers = getUsers();
        List<AboutSection> listAboutRecords = getAboutSections(listUsers);
        abSections.add(new AboutSection(listAboutRecords.get(0).getName(),listAboutRecords.get(0).getCurrentJob(),
                listAboutRecords.get(0).getShortDesc().replaceAll("\\<[^>]*>",""),listAboutRecords.get(0).getWebSite(),listAboutRecords.get(0).getCity(),
                listAboutRecords.get(0).getDegree(),listAboutRecords.get(0).getEmail(),listAboutRecords.get(0).getCurrInd(),listAboutRecords.get(0).getUser_id()));
        return abSections;
    }

    private List<AboutSection> getAboutSections(List<User> listUsers) {
        List<AboutSection> listAboutRecords = aboutService.findAllActiveRecords(listUsers.get(0).getId());
        return listAboutRecords;
    }

    public List<SkillsSection> findAllSkillsSections(){
        skillsSections.clear();
        List<User> listUsers = getUsers();
        List<SkillsSection> listSkillRecords = skillService.listAll().stream()
                .filter(p -> p.isEnabled()&& p.getUser_id().equals(listUsers.get(0).getId())).collect(Collectors.toList());


        for(int i=0;i<listSkillRecords.size();i++){
            skillsSections.add(new SkillsSection(listSkillRecords.get(i).getSkillDescription(),listSkillRecords.get(i).getSkillTitle(),
                    listSkillRecords.get(i).getSkillCategory(),listSkillRecords.get(i).getSkillLevel(),listSkillRecords.get(i).getUser_id()));
        }

        return skillsSections;

    }

    private List<User> getUsers() {
        return userService.listAll().stream()
                .filter(p -> p.isEnabled()).collect(Collectors.toList());
    }

    public AboutSection save(AboutSection aboutSection) {
        abSections.add(aboutSection);
        return aboutSection;
    }

//    public AboutSection findOne(int id){
//        List<AboutSection> listAboutRecords = getAboutSections(getUsers());
//        for(AboutSection aboutSection:listAboutRecords){
//            if(aboutSection.getUser_id()==id){
//                return aboutSection;
//            }
//        }
//        return null;
//    }
}
