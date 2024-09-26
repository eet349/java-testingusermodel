package com.lambdaschool.usermodel.services;

import com.lambdaschool.usermodel.UserModelApplicationTesting;
import com.lambdaschool.usermodel.exceptions.ResourceNotFoundException;
import com.lambdaschool.usermodel.models.Role;
import com.lambdaschool.usermodel.models.User;
import com.lambdaschool.usermodel.models.UserRoles;
import com.lambdaschool.usermodel.models.Useremail;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;




@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserModelApplicationTesting.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServiceImplTestWithDB {

    @Autowired
    private UserService userService;

    @MockBean
    HelperFunctions helperFunctions;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void findUserById() {
        assertEquals("admin", userService.findUserById(4).getUsername());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void findUserByIdNotFound() {
        assertEquals("admin", userService.findUserById(100).getUsername());
    }

    @Test
    public void findByNameContaining() {
        assertEquals(4, userService.findByNameContaining("a").size());
    }

    @Test
    public void findAll() {
        assertEquals(5, userService.findAll().size());
    }

    @Test
    public void delete() {
        userService.delete(13);
        assertEquals(4, userService.findAll().size());
    }
   @Test(expected = ResourceNotFoundException.class)
    public void deleteNotFound() {
        userService.delete(100);
        assertEquals(4, userService.findAll().size());
    }

    @Test
    public void findByName() {
        assertEquals("admin", userService.findByName("admin").getUsername());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void findByNameNotFound() {
        assertEquals("admin", userService.findByName("namenotindb").getUsername());
    }

    @Test
    public void save() {
        Role r2 = new Role("user");
        r2.setRoleid(2);

        User u2 = new User("test", "testpw", "test@email.com");
        u2.getRoles().add(new UserRoles(u2, r2));
        u2.getUseremails().add(new Useremail(u2, "test2@test2.com"));

        User saveU2 = userService.save(u2);

        assertEquals("test2@test2.com", saveU2.getUseremails().get(0).getUseremail());
    }

    @Test
    public void update() {
        Mockito.when(helperFunctions.isAuthorizedToMakeChange(anyString())).thenReturn(true);

        Role r2 = new Role("user");
        r2.setRoleid(2);

        User u2 = new User("testupdate", "testpw", "temail@temail.com");
        u2.getRoles().add(new UserRoles(u2, r2));

        u2.getUseremails().add(new Useremail(u2, "temail2@temail2.com"));
        u2.getUseremails().add(new Useremail(u2, "temail3@temail3.com"));
        u2.getUseremails().add(new Useremail(u2, "temail4@temail4.com"));

        User updatedu2 = userService.update(u2, 7);

        int checking = updatedu2.getUseremails().size() - 1;
        assertEquals("temail4@temail4.com", updatedu2.getUseremails().get(checking).getUseremail());
    }

    @Test
    public void deleteAll() {
        userService.deleteAll();
        assertEquals(0, userService.findAll().size());
    }
}