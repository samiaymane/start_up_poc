package fr.tse.fise3.info6.start_up_poc;

import fr.tse.fise3.info6.start_up_poc.dao.ProjectRepository;
import fr.tse.fise3.info6.start_up_poc.domain.Log;
import fr.tse.fise3.info6.start_up_poc.domain.Project;
import fr.tse.fise3.info6.start_up_poc.domain.User;
import fr.tse.fise3.info6.start_up_poc.service.ProjectService;
import fr.tse.fise3.info6.start_up_poc.service.UserService;
import fr.tse.fise3.info6.start_up_poc.utils.Constants;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Collection;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = "test")
public class ProjectTest {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @Test
    public void testFindAllProjects(){

        Collection<Project> projects = this.projectService.findAllProjects();
        Assert.assertEquals(2, projects.size());
    }

    @Test
    public void testFindLogsForUser(){
        User user1 = new User();
        user1.setEmail("dev1@dev.dev");
        user1.setFirstName("dev1");
        user1.setLastName("dev1");
        user1.setPassword("dev1");
        userService.createUser(user1);

        User user2 = new User();
        user2.setEmail("dev2@dev.dev");
        user2.setFirstName("dev2");
        user2.setLastName("dev2");
        user2.setPassword("dev2");
        userService.createUser(user2);

        Project project = new Project();
        project.setTitle("project");
        project.setDescription("description for project.");
        projectService.createProject(project);

        Assert.assertEquals(0,this.userService.findProjectsForUser(user1).size());
        projectService.affectUserToProject(project,user1);
        Assert.assertEquals(1,this.userService.findProjectsForUser(user1).size());
        Assert.assertEquals(0,this.userService.findProjectsForUser(user2).size());
        projectService.affectUserToProject(project,user2);
        Assert.assertEquals(1,this.userService.findProjectsForUser(user2).size());

        Log log1 = new Log();
        log1.setStart(LocalDateTime.of(2017, 1, 14, 10, 34));
        log1.setEnd(LocalDateTime.of(2017, 1, 14, 15, 16));
        this.projectService.createLog(log1);

        Log log2 = new Log();
        log2.setStart(LocalDateTime.of(2017, 1, 14, 10, 34));
        log2.setEnd(LocalDateTime.of(2017, 1, 14, 15, 16));
        this.projectService.createLog(log2);

        Assert.assertEquals(0,this.projectService.findLogsForUser(user1).size());
        projectService.affectLogToUser(log1, user1);
        Assert.assertEquals(1,this.projectService.findLogsForUser(user1).size());
        Assert.assertEquals(0,this.projectService.findLogsForUser(user2).size());
        projectService.affectLogToUser(log2, user2);
        Assert.assertEquals(1,this.projectService.findLogsForUser(user2).size());

        projectService.deleteLog(log1);
        projectService.deleteLog(log2);
        projectService.deleteProject(project);
        userService.deleteUser(user1);
        userService.deleteUser(user2);
    }

    @Test
    public void affectLogToProject(){

        Log log = new Log();
        log.setStart(LocalDateTime.of(2017, 1, 14, 10, 34));
        log.setEnd(LocalDateTime.of(2017, 1, 14, 15, 16));
        this.projectService.createLog(log);

        Project project = new Project();
        project.setTitle("project1");
        project.setDescription("description for project1.");

        Assert.assertEquals(2, this.projectService.findAllProjects().size());

        this.projectService.createProject(project);

        Assert.assertEquals(3, this.projectService.findAllProjects().size());
        Assert.assertEquals(0, this.projectService.findLogsForProject(project).size());

        this.projectService.affectLogToProject(project, log);

        Assert.assertEquals(1, this.projectService.findLogsForProject(project).size());

        this.projectService.deleteLog(log);

        Assert.assertEquals(0, this.projectService.findLogsForProject(project).size());

        this.projectService.deleteProject(project);

        Assert.assertEquals(2, this.projectService.findAllProjects().size());

    }



}
