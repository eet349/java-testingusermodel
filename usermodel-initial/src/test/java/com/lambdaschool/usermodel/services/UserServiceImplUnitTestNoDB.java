package com.lambdaschool.usermodel.services;

import com.lambdaschool.usermodel.UserModelApplicationTesting;
import com.lambdaschool.usermodel.exceptions.ResourceNotFoundException;
import com.lambdaschool.usermodel.models.Role;
import com.lambdaschool.usermodel.models.User;
import com.lambdaschool.usermodel.models.UserRoles;
import com.lambdaschool.usermodel.models.Useremail;
import com.lambdaschool.usermodel.repository.RoleRepository;
import com.lambdaschool.usermodel.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserModelApplicationTesting.class,
    properties = {"command.line.runner.enable=false"})
public class UserServiceImplUnitTestNoDB {

    //  Mocks -> fake data
    //  Stubs -> fake methods
    //  Java calls them both mocks usually
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userrepos;

    @MockBean
    private RoleService roleService;

    @MockBean
    private HelperFunctions helperFunctions;

    private List<User> userList;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @Before
    public void setUp() throws Exception {
        userList = new ArrayList<>();

        Role r1 = new Role("admin");
        r1.setRoleid(1);
        Role r2 = new Role("user");
        r2.setRoleid(1);
        Role r3 = new Role("data");
        r3.setRoleid(1);

        User u1 = new User("admin", "testpw", "admin@lambdaschool.test");
        u1.getRoles().add(new UserRoles(u1, r1));
        u1.getRoles().add(new UserRoles(u1, r2));
        u1.getRoles().add(new UserRoles(u1, r3));
        u1.getUseremails().add(new Useremail(u1, "admin@mymail.test"));
        u1.getUseremails().get(1).setUseremailid(11);
        u1.setUserid(101);
        userList.add(u1);

        ArrayList<UserRoles> datas = new ArrayList<>();
        User u2 = new User("cinnamon", "testpw", "cinnamon@lambdaschool.test");
        u2.getRoles().add(new UserRoles(u2, r2));
        u2.getRoles().add(new UserRoles(u2, r3));
        u2.getUseremails().add(new Useremail(u2, "cinnamon@ymail.test"));
        u2.getUseremails().get(0).setUseremailid(20);
        u2.getUseremails().add(new Useremail(u2, "bunny@ymail.test"));
        u2.getUseremails().get(1).setUseremailid(21);
        u2.getUseremails().add(new Useremail(u2, "hops@ymail.test"));
        u2.getUseremails().get(2).setUseremailid(22);

        u2.setUserid(102);
        userList.add(u2);

        User u3 = new User("testingbarn", "testpw", "testingbarn@lambdaschool.test");
        u3.getRoles().add(new UserRoles(u3, r1));
        u3.getUseremails().add(new Useremail(u3, "barnbarn@email.test"));
        u3.getUseremails().get(0).setUseremailid(30);

        u3.setUserid(103);
        userList.add(u3);

        User u4 = new User("testingcat", "testpw", "testingcat@lambdaschool.test");
        u4.getRoles().add(new UserRoles(u4, r2));

        u4.setUserid(104);
        userList.add(u4);

        User u5 = new User("testingdog", "testpw", "testingdog@lambdaschool.test");
        u5.getRoles().add(new UserRoles(u5, r2));

        u5.setUserid(105);
        userList.add(u5);

        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void findUserById() {
        Mockito.when(userrepos.findById(101L))
        .thenReturn(Optional.of(userList.get(0)));

        assertEquals("admin", userService.findUserById(101L).getUsername());
    }
    @Test(expected = ResourceNotFoundException.class)
    public void findUserByIdNotFound() {
        Mockito.when(userrepos.findById(101L))
        .thenReturn(Optional.empty());

        assertEquals("admin", userService.findUserById(101L).getUsername());
    }

    @Test
    public void findByNameContaining() {
        Mockito.when(userrepos.findByUsernameContainingIgnoreCase("a")).thenReturn(userList);

        assertEquals(5, userService.findByNameContaining("a").size());
    }

    @Test
    public void findAll() {
        Mockito.when(userrepos.findAll()).thenReturn(userList);

        assertEquals(5, userService.findAll().size());
    }

    @Test
    public void delete() {
        Mockito.when(userrepos.findById(103L)).thenReturn(Optional.of(userList.get(0)));

        Mockito.doNothing().when(userrepos).deleteById(103L);

        userService.delete(103L);
        assertEquals(5, userList.size());
    }
    @Test(expected = ResourceNotFoundException.class)
    public void deleteNotFound() {
        Mockito.when(userrepos.findById(10L)).thenReturn(Optional.empty());

        Mockito.doNothing().when(userrepos).deleteById(10L);

        userService.delete(10L);
        assertEquals(5, userList.size());
    }

    @Test
    public void findByName() {
        Mockito.when(userrepos.findByUsername("admin")).thenReturn(userList.get(0));

        assertEquals("admin", userService.findByName("admin").getUsername());
    }
    @Test(expected = ResourceNotFoundException.class)
    public void findByNameNotFound() {
        Mockito.when(userrepos.findByUsername("gibberish")).thenReturn(null);

        assertEquals("gibberish", userService.findByName("gibberish").getUsername());
    }

    @Test
    public void save() {
        Role r2 = new Role("user");
        r2.setRoleid(2);

        User u2 = new User("user", "password", "user@user.test");
        u2.getRoles().add(new UserRoles(u2, r2));
        u2.getUseremails().add(new Useremail(u2, "user@ymail.test"));

        Mockito.when(userrepos.save(any(User.class))).thenReturn(u2);
        Mockito.when(roleService.findRoleById(2)).thenReturn(r2);

        assertEquals("user", userService.save(u2).getUsername());
    }

    @Test
    public void update() {
        Role r2 = new Role("user");
        r2.setRoleid(2);

        User u2 = new User("user", "password", "user@user.test");
        u2.getRoles().add(new UserRoles(u2, r2));
        u2.getUseremails().add(new Useremail(u2, "user@ymail.test"));
        u2.getUseremails().add(new Useremail(u2, "bunny@email.test"));

        Mockito.when(roleService.findRoleById(2)).thenReturn(r2);

        Mockito.when(userrepos.findById(103L)).thenReturn(Optional.of(userList.get(2)));
        Mockito.when(userrepos.save(any(User.class))).thenReturn(u2);

        Mockito.when(helperFunctions.isAuthorizedToMakeChange(anyString())).thenReturn(true);

        assertEquals("bunny@email.test", userService.update(u2, 103L).getUseremails().get(1).getUseremail());

    }

    @Test
    public void deleteAll() {
        Mockito.doNothing().when(userrepos).deleteAll();

        userService.deleteAll();
        assertEquals(5, userList.size());
    }
}