package fr.tse.fise3.info6.start_up_poc.controller;

import fr.tse.fise3.info6.start_up_poc.domain.Project;
import fr.tse.fise3.info6.start_up_poc.domain.User;
import fr.tse.fise3.info6.start_up_poc.service.UserService;
import fr.tse.fise3.info6.start_up_poc.utils.ChangeManagerAction;
import fr.tse.fise3.info6.start_up_poc.utils.ChangeRoleAction;
import fr.tse.fise3.info6.start_up_poc.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS, RequestMethod.PATCH, RequestMethod.DELETE})
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/users/all")
    List<User> findAllUsers(){
        return this.userService.findAllUsers();
    }

    @GetMapping("/users/managers")
    List<User> findAllMangers(){
        return this.userService.findAllManagers();
    }

    @GetMapping("/users/managers_and_users")
    List<User> findAllMangersAndUsers(){
        return this.userService.findAllManagersAndUsers();
    }

    @GetMapping("/users/{id}")
    User findUserById(@PathVariable Long id){
        return this.userService.findUser(id);
    }

    @GetMapping("/users/{id}/subordinates")
    List<User> findSubordinatesForUser(@PathVariable Long id){
        User user = this.userService.findUser(id);
        return this.userService.findSubordinatesForManager(user);
    }

    @GetMapping("/users/{id}/projects")
    List<Project> findProjectsForUser(@PathVariable Long id){
        User user = this.userService.findUser(id);
        return this.userService.findProjectsForUser(user);
    }

    @PatchMapping("/users/{id}/changeRole")
    User changeUserRole(@RequestBody ChangeRoleAction changeRoleAction, @PathVariable Long id){
        User user = this.userService.findUser(id);

        if (Constants.UPGRADE_ACTION.equals(changeRoleAction.getAction())){
            user = this.userService.upgradeUser(user);
        }
        else if(Constants.DOWNGRADE_ACTION.equals(changeRoleAction.getAction())){
            user = this.userService.downgradeUser(user);
        }
        else {
            throw new IllegalStateException();
        }

        return user;
    }

    @PatchMapping("/users/{id}/changeManager")
    User affectUserToManager(@RequestBody ChangeManagerAction changeManagerAction, @PathVariable Long id){
        User user = this.userService.findUser(id);
        User manager = this.userService.findUser(changeManagerAction.getId());
        return this.userService.affectUserToManager(user, manager);
    }

    @PostMapping("/users")
    User createUser(@RequestBody @Valid User user){
        return this.userService.createUser(user);
    }

    @PostMapping("/users/admin")
    User createAdmin(@RequestBody @Valid User admin){
        return this.userService.createAdmin(admin);
    }

    @DeleteMapping("/users/{id}")
    void deleteUser(@PathVariable Long id){
        User user = this.userService.findUser(id);
        this.userService.deleteUser(user);
    }

}
