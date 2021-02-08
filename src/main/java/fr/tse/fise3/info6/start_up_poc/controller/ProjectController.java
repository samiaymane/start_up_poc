package fr.tse.fise3.info6.start_up_poc.controller;

import fr.tse.fise3.info6.start_up_poc.domain.Log;
import fr.tse.fise3.info6.start_up_poc.domain.Project;
import fr.tse.fise3.info6.start_up_poc.domain.RoleStatus;
import fr.tse.fise3.info6.start_up_poc.domain.User;
import fr.tse.fise3.info6.start_up_poc.service.ProjectService;
import fr.tse.fise3.info6.start_up_poc.service.UserService;
import fr.tse.fise3.info6.start_up_poc.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
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
        Project project = this.projectService.findProject(id);
        if(project == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found");
        }else{
            return project;
        }

    }

    @GetMapping("/logs/{id}")
    Log findLog(@PathVariable Long id){
        Log log =  this.projectService.findLog(id);
        if(log == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Log not found");
        }else{
            return log;
        }
    }

    @GetMapping("/project/{id}/logs")
    List<Log> findLogsForProject(@PathVariable Long id){
        Project project = this.projectService.findProject(id);

        if(project == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found");
        }

        return this.projectService.findLogsForProject(project);
    }

    @GetMapping("/users/{id}/logs")
    List<Log> findLogsForUser(@PathVariable Long id){
        User user = this.userService.findUser(id);
        if(user == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return this.projectService.findLogsForUser(user);
    }

    @PostMapping("/projects")
    Project createProject(@RequestBody @Valid Project project, @AuthenticationPrincipal User currentUser) throws AccessDeniedException {

        RoleStatus managerRole = this.userService.findRoleStatus(Constants.ROLE_STATUS_MANAGER_ID);

        if (!currentUser.getRoleStatus().equals(managerRole)){
            throw new AccessDeniedException("Current user must have manager credentials.");
        }

        this.projectService.createProject(project);

        return this.projectService.affectUserToProject(project,currentUser);
    }

    @PostMapping("/logs")
    Log createLog(@RequestBody @Valid Log log, @AuthenticationPrincipal User currentUser) throws AccessDeniedException {

        RoleStatus userRole = this.userService.findRoleStatus(Constants.ROLE_STATUS_USER_ID);

        if (!currentUser.getRoleStatus().equals(userRole)){
            throw new AccessDeniedException("Current user must have basic user credentials.");
        }

        this.projectService.createLog(log);
        return this.projectService.affectLogToUser(log, currentUser);
    }

    @DeleteMapping("/projects/{id}")
    void deleteProject(@PathVariable Long id, @AuthenticationPrincipal User currentUser) throws AccessDeniedException {

        RoleStatus managerRole = this.userService.findRoleStatus(Constants.ROLE_STATUS_MANAGER_ID);

        if (!currentUser.getRoleStatus().equals(managerRole)){
            throw new AccessDeniedException("Current user must have manager credentials.");
        }

        Project project = this.projectService.findProject(id);

        if(project == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found");
        }

        if( !this.userService.findProjectsForUser(currentUser).contains(project)){
            throw new AccessDeniedException("Project not assigned to current manager.");
        }

        this.projectService.deleteProject(project);
    }

    @DeleteMapping("/logs/{id}")
    void deleteLog(@PathVariable Long id, @AuthenticationPrincipal User currentUser) throws AccessDeniedException {

        RoleStatus userRole = this.userService.findRoleStatus(Constants.ROLE_STATUS_USER_ID);

        if (!currentUser.getRoleStatus().equals(userRole)){
            throw new AccessDeniedException("Current user must have manager credentials.");
        }

        Log log = this.projectService.findLog(id);

        if(log == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Log not found");
        }

        if (!this.projectService.findLogsForUser(currentUser).contains(log)){
            throw new AccessDeniedException("User not assigned to current manager.");
        }

        this.projectService.deleteLog(log);
    }

    @PatchMapping("/projects/{id}/addLog")
    Project affectLogToProject(@RequestBody AddLogAction addLogAction, @PathVariable Long id, @AuthenticationPrincipal User currentUser) throws AccessDeniedException {

        RoleStatus userRole = this.userService.findRoleStatus(Constants.ROLE_STATUS_USER_ID);

        if (!currentUser.getRoleStatus().equals(userRole)){
            throw new AccessDeniedException("Current user must have basic user credentials.");
        }

        Project project = this.projectService.findProject(id);

        if(project == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found");
        }

        Log log = this.projectService.findLog(addLogAction.getId());

        if(project == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "log not found");
        }

        return this.projectService.affectLogToProject(project,log);
    }

    @PatchMapping("/projects/{id}/addUser")
    Project affectUserToProject(@RequestBody AddUserAction addUserAction, @PathVariable Long id, @AuthenticationPrincipal User currentUser) throws AccessDeniedException {

        RoleStatus managerRole = this.userService.findRoleStatus(Constants.ROLE_STATUS_MANAGER_ID);

        if (!currentUser.getRoleStatus().equals(managerRole)){
            throw new AccessDeniedException("Current user must have manager credentials.");
        }

        Project project = this.projectService.findProject(id);

        if(project == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found");
        }

        if( !this.userService.findProjectsForUser(currentUser).contains(project)){
            throw new AccessDeniedException("User not assigned to current manager.");
        }

        User user = this.userService.findUser(addUserAction.getId());

        if(project == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        return this.projectService.affectUserToProject(project,user);
    }

    @GetMapping("/exportPDF/{id}")
    ResponseEntity<InputStreamResource> getPDF(@PathVariable Long id, @AuthenticationPrincipal User currentUser,
                                               @RequestBody ExportPdfAction exportPdfAction) throws IOException {

        RoleStatus managerRole = this.userService.findRoleStatus(Constants.ROLE_STATUS_MANAGER_ID);

        if (!currentUser.getRoleStatus().equals(managerRole)){
            throw new AccessDeniedException("Current user must have manager credentials.");
        }

        User user = this.userService.findUser(id);
        if(user == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        LogPDFExporter logPDFExporter = new LogPDFExporter(user,this.projectService.findLogsForUser(user,
                exportPdfAction.getStartDate(), exportPdfAction.getEndDate()));
        ByteArrayInputStream bis = logPDFExporter.export();

        var headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=report.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    @GetMapping("/exportPDF")
    ResponseEntity<InputStreamResource> getPDF(@AuthenticationPrincipal User currentUser,
                                               @RequestBody ExportPdfAction exportPdfAction) throws IOException {

        RoleStatus managerRole = this.userService.findRoleStatus(Constants.ROLE_STATUS_USER_ID);

        if (!currentUser.getRoleStatus().equals(managerRole)){
            throw new AccessDeniedException("Current user must have basic user credentials.");
        }

        User foundUser = this.userService.findUser(currentUser.getId());

        LogPDFExporter logPDFExporter = new LogPDFExporter(foundUser,this.projectService.findLogsForUser(foundUser,
                exportPdfAction.getStartDate(), exportPdfAction.getEndDate()));
        ByteArrayInputStream bis = logPDFExporter.export();

        var headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=report.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

}
