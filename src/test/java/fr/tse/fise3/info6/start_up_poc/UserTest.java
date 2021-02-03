package fr.tse.fise3.info6.start_up_poc;

import fr.tse.fise3.info6.start_up_poc.domain.User;
import fr.tse.fise3.info6.start_up_poc.service.UserService;
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
    public void findAllUsers(){

        Collection<User> users = this.userService.findAllUsers();
        Assert.assertEquals(5 , users.size());
    }
}
