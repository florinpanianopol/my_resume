package com.myresume.admin.user;

import com.myresume.common.entity.Role;
import com.myresume.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {

    @Autowired
    private UserRepository repo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateUserWithOneRole() {

        Role roleAdmin = entityManager.find(Role.class,1);
        User userFlorin = new User("florinpani@yahoo.com","abcd1234","Florin","Panianopol");

        userFlorin.addRole(roleAdmin);

        User savedUser = repo.save(userFlorin);

        assertThat(savedUser.getId()).isGreaterThan(0);

    }

    @Test
    public void testCreateUserWithTwoRoles() {
        User userRavi=new User("ravi3@gmail.com","ravi2021","Ravi","Kumar");
        Role roleEditor = new Role(3);
        Role roleAssistant = new Role(5);

        userRavi.addRole(roleEditor);
        userRavi.addRole(roleAssistant);

        User savedUser = repo.save(userRavi);

        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testListAllUsers() {
        Iterable <User> listUsers = repo.findAll();
        listUsers.forEach(user -> System.out.println(user));
    }

    @Test
    public void testGetUserById() {
        User userTest = repo.findById(1).get();
        System.out.println(userTest);
        assertThat(userTest).isNotNull();
    }

    @Test
    public void testUpdateUserDetails() {
        User userNam = repo.findById(5).get();
        userNam.setEnabled(false);
        userNam.setEmail("test2@test.com");

        repo.save(userNam);

    }

    @Test
    public void testUpdateUserRoles() {
        User userUpdate = repo.findById(9).get();
        Role roleEditor = new Role(3);
        Role roleSalesPerson = new Role(4);

        userUpdate.getRoles().remove(roleEditor);
        userUpdate.addRole(roleSalesPerson);
        repo.save(userUpdate);

    }

    @Test
    public void testDeleteUser() {
        Integer userId = 9;
        repo.deleteById(userId);
    }

    @Test
    public void testGetUserByEmail() {
        String email = "florinpani@yahoo.com";
        User user = repo.getUserByEmail(email);

        assertThat(user).isNotNull();
    }

    @Test
    public void testCountById() {
        Integer id = 1;
        Long countById = repo.countById(id);

        assertThat(countById).isNotNull().isGreaterThan(0);
    }

    @Test
    public void testDisableUser() {
        Integer id = 26;
        repo.updateEnabledStatus(id, false);
    }

    @Test
    public void testEnableUser() {
        Integer id = 26;
        repo.updateEnabledStatus(id, true);
    }

    @Test
    public void testListFirstPage() {
        int pageNumber = 0;
        int pageSize = 4;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = repo.findAll(pageable);

        List<User> listUsers = page.getContent();

        listUsers.forEach(user -> System.out.println(user));

        assertThat(listUsers.size()).isEqualTo(pageSize);
    }

    @Test
    public void testSearchUsers() {
        String keyword = "bruce";
        int pageNumber = 0;
        int pageSize = 4;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = repo.findAll(keyword,pageable);

        List<User> listUsers = page.getContent();

        listUsers.forEach(user -> System.out.println(user));

        assertThat(listUsers.size()).isGreaterThan(0);

    }

}