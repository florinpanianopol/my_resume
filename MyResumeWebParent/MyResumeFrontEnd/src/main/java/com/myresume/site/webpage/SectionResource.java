package com.myresume.site.webpage;

import com.myresume.common.entity.AboutSection;
import com.myresume.common.entity.SkillsSection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SectionResource {

    @Autowired
    private SectionDaoService service;

    @GetMapping(path="/aboutsections")
    public List<AboutSection> retrieveAllAboutSections(){

        return service.findAllAboutSections();
    }

    @GetMapping(path="/skillssections")
    public List<SkillsSection> retrieveAllSkillSections(){

        return service.findAllSkillsSections();
    }

//    @GetMapping(path="/aboutsections/{id}")
//    public EntityModel<AboutSection> retrieveAboutSections(@PathVariable int id) throws UserNotFoundException {
//        AboutSection aboutSection = service.findOne(id);
//        if(aboutSection==null){
//            throw new UserNotFoundException("id -> " + id);
//        }
//
//        EntityModel<AboutSection> model = EntityModel.of(aboutSection);
//
//        WebMvcLinkBuilder linkToUAboutSections =
//                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).retrieveAllAboutSections());
//
//        model.add(linkToUAboutSections.withRel("all-aboutsections"));
//        return model;
//
//    }

}
