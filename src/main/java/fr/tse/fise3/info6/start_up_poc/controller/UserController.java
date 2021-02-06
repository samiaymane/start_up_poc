package fr.tse.fise3.info6.start_up_poc.controller;

import fr.tse.fise3.info6.start_up_poc.domain.Project;
import fr.tse.fise3.info6.start_up_poc.domain.RoleStatus;
import fr.tse.fise3.info6.start_up_poc.domain.User;
import fr.tse.fise3.info6.start_up_poc.service.UserService;
import fr.tse.fise3.info6.start_up_poc.utils.ChangeManagerAction;
import fr.tse.fise3.info6.start_up_poc.utils.ChangeRoleAction;
import fr.tse.fise3.info6.start_up_poc.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.Role;
import javax.validation.Valid;
import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/isAdmin")
    String SecurityOk(@AuthenticationPrincipal User user) throws AccessDeniedException {
        RoleStatus adminRole = this.userService.findRoleStatus(Constants.ROLE_STATUS_ADMIN_ID);

        if (!user.getRoleStatus().equals(adminRole)){
            throw new AccessDeniedException(null);
        }

        return "All good ! ";
    }

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
    User changeUserRole(@RequestBody ChangeRoleAction changeRoleAction, @PathVariable Long id, @AuthenticationPrincipal User currentUser) throws AccessDeniedException {
        RoleStatus adminRole = this.userService.findRoleStatus(Constants.ROLE_STATUS_ADMIN_ID);

        if (!currentUser.getRoleStatus().equals(adminRole)){
            throw new AccessDeniedException(null);
        }

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
    User affectUserToManager(@RequestBody ChangeManagerAction changeManagerAction, @PathVariable Long id, @AuthenticationPrincipal User currentUser) throws AccessDeniedException {

        RoleStatus adminRole = this.userService.findRoleStatus(Constants.ROLE_STATUS_ADMIN_ID);

        if (!currentUser.getRoleStatus().equals(adminRole)){
            throw new AccessDeniedException(null);
        }

        User user = this.userService.findUser(id);
        User manager = this.userService.findUser(changeManagerAction.getId());
        return this.userService.affectUserToManager(user, manager);
    }

    @PostMapping("/users")
    User createUser(@RequestBody @Valid User user, @AuthenticationPrincipal User currentUser) throws AccessDeniedException {

        RoleStatus managerRole = this.userService.findRoleStatus(Constants.ROLE_STATUS_MANAGER_ID);

        if (!currentUser.getRoleStatus().equals(managerRole)){
            throw new AccessDeniedException(null);
        }
        this.userService.createUser(user);
        return this.userService.affectUserToManager(user, currentUser);
    }

    @PostMapping("/users/admin")
    User createAdmin(@RequestBody @Valid User admin, @AuthenticationPrincipal User currentUser) throws AccessDeniedException {

        RoleStatus adminRole = this.userService.findRoleStatus(Constants.ROLE_STATUS_MANAGER_ID);

        if (!currentUser.getRoleStatus().equals(adminRole)){
            throw new AccessDeniedException(null);
        }

        return this.userService.createAdmin(admin);
    }

    @DeleteMapping("/users/{id}")
    void deleteUser(@PathVariable Long id, @AuthenticationPrincipal User currentUser) throws AccessDeniedException {

        RoleStatus adminRole = this.userService.findRoleStatus(Constants.ROLE_STATUS_MANAGER_ID);

        if (!currentUser.getRoleStatus().equals(adminRole)){
            throw new AccessDeniedException(null);
        }

        User user = this.userService.findUser(id);
        this.userService.deleteUser(user);
    }

}
