package fr.tse.fise3.info6.start_up_poc.controller;

import fr.tse.fise3.info6.start_up_poc.domain.Log;
import fr.tse.fise3.info6.start_up_poc.domain.Project;
import fr.tse.fise3.info6.start_up_poc.domain.User;
import fr.tse.fise3.info6.start_up_poc.service.ProjectService;
import fr.tse.fise3.info6.start_up_poc.service.UserService;
import fr.tse.fise3.info6.start_up_poc.utils.AddLogAction;
import fr.tse.fise3.info6.start_up_poc.utils.AddUserAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS, RequestMethod.PATCH, RequestMethod.DELETE})
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
    Project createProject(@RequestBody @Valid Project project){
        return this.projectService.createProject(project);
    }

    @PostMapping("/logs")
    Log createLog(@RequestBody @Valid Log log){
        return this.projectService.createLog(log);
    }

    @DeleteMapping("/projects/{id}")
    void deleteProject(@PathVariable Long id){
        Project project = this.projectService.findProject(id);
        this.projectService.deleteProject(project);
    }

    @DeleteMapping("/logs/{id}")
    void deleteLog(@PathVariable Long id){
        Log log = this.projectService.findLog(id);
        this.projectService.deleteLog(log);
    }

    @PatchMapping("/projects/{id}/addLog")
    Project affectLogToProject(@RequestBody AddLogAction addLogAction, @PathVariable Long id){
        Project project = this.projectService.findProject(id);
        Log log = this.projectService.findLog(addLogAction.getId());
        return this.projectService.affectLogToProject(project,log);
    }

    @PatchMapping("/projects/{id}/addUser")
    Project affectUserToProject(@RequestBody AddUserAction addUserAction, @PathVariable Long id){
        Project project = this.projectService.findProject(id);
        User user = this.userService.findUser(addUserAction.getId());
        return this.projectService.affectUserToProject(project,user);
    }

}
