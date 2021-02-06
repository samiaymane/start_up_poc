package fr.tse.fise3.info6.start_up_poc.controller;

import fr.tse.fise3.info6.start_up_poc.domain.Log;
import fr.tse.fise3.info6.start_up_poc.domain.Project;
import fr.tse.fise3.info6.start_up_poc.domain.RoleStatus;
import fr.tse.fise3.info6.start_up_poc.domain.User;
import fr.tse.fise3.info6.start_up_poc.service.ProjectService;
import fr.tse.fise3.info6.start_up_poc.service.UserService;
import fr.tse.fise3.info6.start_up_poc.utils.AddLogAction;
import fr.tse.fise3.info6.start_up_poc.utils.AddUserAction;
import fr.tse.fise3.info6.start_up_poc.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @GetMapping("/projects")
    List<Project> findAllProjects(){
        return this.projectService.findAllProjects();
    }

    @GetMapping("/projects/{id}")
    Project findProject(@PathVariable Long id){
        return this.projectService.findProject(id);
    }

    @GetMapping("/logs/{id}")
    Log findLog(@PathVariable Long id){
        return this.projectService.findLog(id);
    }

    @GetMapping("/project/{id}/logs")
    List<Log> findLogsForProject(@PathVariable Long id){
        Project project = this.projectService.findProject(id);
        return this.projectService.findLogsForProject(project);
    }

    @GetMapping("/users/{id}/logs")
    List<Log> findLogsForUser(@PathVariable Long id){
        User user = this.userService.findUser(id);
        return this.projectService.findLogsForUser(user);
    }

    @PostMapping("/projects")
    Project createProject(@RequestBody @Valid Project project, @AuthenticationPrincipal User currentUser) throws AccessDeniedException {

        RoleStatus managerRole = this.userService.findRoleStatus(Constants.ROLE_STATUS_MANAGER_ID);

        if (!currentUser.getRoleStatus().equals(managerRole)){
            throw new AccessDeniedException(null);
        }

        this.projectService.createProject(project);

        return this.projectService.affectUserToProject(project,currentUser);
    }

    @PostMapping("/logs")
    Log createLog(@RequestBody @Valid Log log, @AuthenticationPrincipal User currentUser) throws AccessDeniedException {

        RoleStatus userRole = this.userService.findRoleStatus(Constants.ROLE_STATUS_USER_ID);

        if (!currentUser.getRoleStatus().equals(userRole)){
            throw new AccessDeniedException(null);
        }

        this.projectService.createLog(log);
        return this.projectService.affectLogToUser(log, currentUser);
    }

    @DeleteMapping("/projects/{id}")
    void deleteProject(@PathVariable Long id, @AuthenticationPrincipal User currentUser) throws AccessDeniedException {

        RoleStatus managerRole = this.userService.findRoleStatus(Constants.ROLE_STATUS_MANAGER_ID);

        if (!currentUser.getRoleStatus().equals(managerRole)){
            throw new AccessDeniedException(null);
        }

        Project project = this.projectService.findProject(id);

        if( !this.userService.findProjectsForUser(currentUser).contains(project)){
            throw new AccessDeniedException(null);
        }

        this.projectService.deleteProject(project);
    }

    @DeleteMapping("/logs/{id}")
    void deleteLog(@PathVariable Long id, @AuthenticationPrincipal User currentUser) throws AccessDeniedException {

        RoleStatus userRole = this.userService.findRoleStatus(Constants.ROLE_STATUS_USER_ID);

        if (!currentUser.getRoleStatus().equals(userRole)){
            throw new AccessDeniedException(null);
        }

        Log log = this.projectService.findLog(id);

        if (!this.projectService.findLogsForUser(currentUser).contains(log)){
            throw new AccessDeniedException(null);
        }

        this.projectService.deleteLog(log);
    }

    @PatchMapping("/projects/{id}/addLog")
    Project affectLogToProject(@RequestBody AddLogAction addLogAction, @PathVariable Long id, @AuthenticationPrincipal User currentUser) throws AccessDeniedException {

        RoleStatus userRole = this.userService.findRoleStatus(Constants.ROLE_STATUS_USER_ID);

        if (!currentUser.getRoleStatus().equals(userRole)){
            throw new AccessDeniedException(null);
        }

        Project project = this.projectService.findProject(id);
        Log log = this.projectService.findLog(addLogAction.getId());
        return this.projectService.affectLogToProject(project,log);
    }

    @PatchMapping("/projects/{id}/addUser")
    Project affectUserToProject(@RequestBody AddUserAction addUserAction, @PathVariable Long id, @AuthenticationPrincipal User currentUser) throws AccessDeniedException {

        RoleStatus managerRole = this.userService.findRoleStatus(Constants.ROLE_STATUS_MANAGER_ID);

        if (!currentUser.getRoleStatus().equals(managerRole)){
            throw new AccessDeniedException(null);
        }

        Project project = this.projectService.findProject(id);

        if( !this.userService.findProjectsForUser(currentUser).contains(project)){
            throw new AccessDeniedException(null);
        }

        User user = this.userService.findUser(addUserAction.getId());
        return this.projectService.affectUserToProject(project,user);
    }

}
