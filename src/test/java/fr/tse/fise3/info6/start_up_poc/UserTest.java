package fr.tse.fise3.info6.start_up_poc;

import fr.tse.fise3.info6.start_up_poc.domain.User;
import fr.tse.fise3.info6.start_up_poc.service.UserService;
import fr.tse.fise3.info6.start_up_poc.utils.Constants;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = "test")
public class UserTest {

    @Autowired
    private UserService userService;

    @Test
    public void testFindAllUsers(){

        Collection<User> users = this.userService.findAllUsers();
        Assert.assertEquals(5 , users.size());
    }

    @Test
    public void testFindAllManagers(){

        Collection<User> users = this.userService.findAllManagers();
        Assert.assertEquals(2 , users.size());
    }

    @Test
    public void testFindAllManagersAndUsers(){

        Collection<User> users = this.userService.findAllManagersAndUsers();
        Assert.assertEquals(4 , users.size());
    }

    @Test
    public void testUpgradeAndDowngradeUser(){
        User user = new User();
        user.setEmail("user@dev.dev");
        user.setFirstName("user");
        user.setLastName("user");
        user.setPassword("user");
        this.userService.createUser(user);

        this.userService.upgradeUser(user);
        Assert.assertEquals(this.userService.findRoleStatus(Constants.ROLE_STATUS_MANAGER_ID), user.getRoleStatus());

        Assert.assertThrows(IllegalStateException.class, () -> this.userService.upgradeUser(user));

        this.userService.downgradeUser(user);
        Assert.assertEquals(this.userService.findRoleStatus(Constants.ROLE_STATUS_USER_ID), user.getRoleStatus());

        Assert.assertThrows(IllegalStateException.class, () -> this.userService.downgradeUser(user));

        this.userService.deleteUser(user);
    }

    @Test
    public void testAffectUserToManager(){

        User manager = new User();
        manager.setEmail("manager@dev.dev");
        manager.setFirstName("manager");
        manager.setLastName("manager");
        manager.setPassword("manager");
        this.userService.createUser(manager);
        this.userService.upgradeUser(manager);

        User user = new User();
        user.setEmail("user@dev.dev");
        user.setFirstName("user");
        user.setLastName("user");
        user.setPassword("user");
        this.userService.createUser(user);

        Assert.assertNull(user.getManager());
        Assert.assertEquals(0, this.userService.findSubordinatesForManager(manager).size());

        this.userService.affectUserToManager(user, manager);

        Assert.assertEquals(user.getManager(),manager);
        Assert.assertEquals(1, this.userService.findSubordinatesForManager(manager).size());

        Assert.assertThrows(IllegalStateException.class, () -> this.userService.affectUserToManager(manager, user));

        this.userService.deleteUser(user);
        this.userService.deleteUser(manager);
    }
}
