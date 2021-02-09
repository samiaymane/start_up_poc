package fr.tse.fise3.info6.start_up_poc.controller;

import fr.tse.fise3.info6.start_up_poc.domain.Project;
import fr.tse.fise3.info6.start_up_poc.domain.RoleStatus;
import fr.tse.fise3.info6.start_up_poc.domain.User;
import fr.tse.fise3.info6.start_up_poc.service.UserService;
import fr.tse.fise3.info6.start_up_poc.utils.ChangeManagerAction;
import fr.tse.fise3.info6.start_up_poc.utils.ChangeRoleAction;
import fr.tse.fise3.info6.start_up_poc.utils.Constants;
import fr.tse.fise3.info6.start_up_poc.utils.LogPDFExporter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/hello")
    User hello(@AuthenticationPrincipal User currentUser){
        User foundUser = this.userService.findUser(currentUser.getId());
        return foundUser;
    }

    @GetMapping("/isAdmin")
    String securityOk(@AuthenticationPrincipal User user) throws AccessDeniedException {
        RoleStatus adminRole = this.userService.findRoleStatus(Constants.ROLE_STATUS_ADMIN_ID);

        if (!user.getRoleStatus().equals(adminRole)){
            throw new AccessDeniedException("Current user must have admin credentials.");
        }

        return "All good!";
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
        User user = this.userService.findUser(id);
        if(user != null){
            return user;
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    @GetMapping("/users/{id}/subordinates")
    List<User> findSubordinatesForUser(@PathVariable Long id){
        User user = this.userService.findUser(id);
        if(user != null){
            return this.userService.findSubordinatesForManager(user);
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    @GetMapping("/users/{id}/projects")
    List<Project> findProjectsForUser(@PathVariable Long id){
        User user = this.userService.findUser(id);
        if(user != null){
            return this.userService.findProjectsForUser(user);
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    @PatchMapping("/users/{id}/changeRole")
    User changeUserRole(@RequestBody ChangeRoleAction changeRoleAction, @PathVariable Long id, @AuthenticationPrincipal User currentUser) throws AccessDeniedException {
        RoleStatus adminRole = this.userService.findRoleStatus(Constants.ROLE_STATUS_ADMIN_ID);

        if (!currentUser.getRoleStatus().equals(adminRole)){
            throw new AccessDeniedException("Current user must have admin credentials.");
        }

        User user = this.userService.findUser(id);

        if(user == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        if (Constants.UPGRADE_ACTION.equals(changeRoleAction.getAction())){
            user = this.userService.upgradeUser(user);
        }
        else if(Constants.DOWNGRADE_ACTION.equals(changeRoleAction.getAction())){
            user = this.userService.downgradeUser(user);
        }
        else {
            throw new IllegalStateException("Bad Action.");
        }

        return user;
    }

    @PatchMapping("/users/{id}/changeManager")
    User affectUserToManager(@RequestBody ChangeManagerAction changeManagerAction, @PathVariable Long id, @AuthenticationPrincipal User currentUser) throws AccessDeniedException {

        RoleStatus adminRole = this.userService.findRoleStatus(Constants.ROLE_STATUS_ADMIN_ID);

        if (!currentUser.getRoleStatus().equals(adminRole)){
            throw new AccessDeniedException("Current user must have admin credentials.");
        }

        User user = this.userService.findUser(id);

        if(user == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        User manager = this.userService.findUser(changeManagerAction.getId());

        if(manager == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Manager not found");
        }

        return this.userService.affectUserToManager(user, manager);
    }

    @PostMapping("/users")
    User createUser(@RequestBody @Valid User user, @AuthenticationPrincipal User currentUser) throws AccessDeniedException {

        RoleStatus managerRole = this.userService.findRoleStatus(Constants.ROLE_STATUS_MANAGER_ID);

        if (!currentUser.getRoleStatus().equals(managerRole)){
            throw new AccessDeniedException("Current user must have manager credentials.");
        }
        this.userService.createUser(user);
        User manager = this.userService.findUser(currentUser.getId());
        return this.userService.affectUserToManager(user, manager);
    }

    @PostMapping("/users/admin")
    User createAdmin(@RequestBody @Valid User admin, @AuthenticationPrincipal User currentUser) throws AccessDeniedException {

        RoleStatus adminRole = this.userService.findRoleStatus(Constants.ROLE_STATUS_ADMIN_ID);

        if (!currentUser.getRoleStatus().equals(adminRole)){
            throw new AccessDeniedException("Current user must have admin credentials.");
        }

        return this.userService.createAdmin(admin);
    }

    @DeleteMapping("/users/{id}")
    void deleteUser(@PathVariable Long id, @AuthenticationPrincipal User currentUser) throws AccessDeniedException {

        RoleStatus adminRole = this.userService.findRoleStatus(Constants.ROLE_STATUS_ADMIN_ID);

        if (!currentUser.getRoleStatus().equals(adminRole)){
            throw new AccessDeniedException("Current user must have admin credentials.");
        }

        User user = this.userService.findUser(id);
        if(user == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        this.userService.deleteUser(user);
    }

}
