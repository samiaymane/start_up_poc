package fr.tse.fise3.info6.start_up_poc.utils;

import fr.tse.fise3.info6.start_up_poc.dao.LogRepository;
import fr.tse.fise3.info6.start_up_poc.dao.ProjectRepository;
import fr.tse.fise3.info6.start_up_poc.dao.RoleStatusRepository;
import fr.tse.fise3.info6.start_up_poc.dao.UserRepository;
import fr.tse.fise3.info6.start_up_poc.domain.Log;
import fr.tse.fise3.info6.start_up_poc.domain.Project;
import fr.tse.fise3.info6.start_up_poc.domain.RoleStatus;
import fr.tse.fise3.info6.start_up_poc.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
@Slf4j
public class LoadDatabase {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    @Profile("!test")
    CommandLineRunner initDatabase(LogRepository logRepository, ProjectRepository projectRepository,
                                   RoleStatusRepository roleStatusRepository, UserRepository userRepository){
        return args -> {
            initRoleStatus(roleStatusRepository);

            Project project1 = new Project();
            project1.setTitle("Projet intégré STARTUP POC");
            project1.setDescription("Une application pour gérer les temps passés sur un projet.");
            projectRepository.save(project1);
            log.info(project1 + " saved to database.");

            Project project2 = new Project();
            project2.setTitle("Kanban");
            project2.setDescription("Une application pour gérer des tasks de développeurs.");
            projectRepository.save(project2);
            log.info(project2 + " saved to database.");

            User admin = new User();
            admin.setEmail("admin@dev.dev");
            admin.setFirstName("admin");
            admin.setLastName("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRoleStatus(roleStatusRepository.findById(Constants.ROLE_STATUS_ADMIN_ID).orElse(null));
            userRepository.save(admin);
            log.info(admin + " saved to database.");

            User manager1 = new User();
            manager1.setEmail("manager1@dev.dev");
            manager1.setFirstName("manager1");
            manager1.setLastName("manager1");
            manager1.setPassword(passwordEncoder.encode("manager1"));
            manager1.setRoleStatus(roleStatusRepository.findById(Constants.ROLE_STATUS_MANAGER_ID).orElse(null));
            manager1.addProject(project1);
            userRepository.save(manager1);
            log.info(manager1 + " saved to database.");

            User manager2 = new User();
            manager2.setEmail("manager2@dev.dev");
            manager2.setFirstName("manager2");
            manager2.setLastName("manager2");
            manager2.setPassword(passwordEncoder.encode("manager2"));
            manager2.setRoleStatus(roleStatusRepository.findById(Constants.ROLE_STATUS_MANAGER_ID).orElse(null));
            userRepository.save(manager2);
            log.info(manager2 + " saved to database.");

            User user1 = new User();
            user1.setEmail("dev1@dev.dev");
            user1.setFirstName("dev1");
            user1.setLastName("dev1");
            user1.setPassword(passwordEncoder.encode("dev1"));
            user1.setRoleStatus(roleStatusRepository.findById(Constants.ROLE_STATUS_USER_ID).orElse(null));
            userRepository.save(user1);
            log.info(user1 + " saved to database.");

            User user2 = new User();
            user2.setEmail("dev2@dev.dev");
            user2.setFirstName("dev2");
            user2.setLastName("dev2");
            user2.setPassword(passwordEncoder.encode("dev2"));
            user2.setRoleStatus(roleStatusRepository.findById(Constants.ROLE_STATUS_USER_ID).orElse(null));
            userRepository.save(user2);
            log.info(user2 + " saved to database.");

            for(int i=1; i<=10; i++){
                Log log1 = new Log();
                log1.setStart(LocalDateTime.of(2021, 1, i, 8, 0));
                log1.setEnd(LocalDateTime.of(2021, 1, i+2, 18, 0));
                log1.setUser(user1);
                log1.setProject(project1);
                logRepository.save(log1);

                Log log2 = new Log();
                log2.setStart(LocalDateTime.of(2021, 1, i+1, 8, 0));
                log2.setEnd(LocalDateTime.of(2021, 1, i+5, 18, 0));
                log2.setUser(user2);
                log2.setProject(project2);
                logRepository.save(log2);
            }

            log.info("All seem to be OK for the app initialization!");
        };
    }

    private void initRoleStatus(RoleStatusRepository roleStatusRepository){

        RoleStatus userStatus = new RoleStatus(Constants.ROLE_STATUS_USER_ID, Constants.ROLE_STATUS_USER_ROLE_TITLE);
        roleStatusRepository.save(userStatus);
        log.info(userStatus + " saved to database.");

        RoleStatus managerStatus = new RoleStatus(Constants.ROLE_STATUS_MANAGER_ID, Constants.ROLE_STATUS_MANAGER_ROLE_TITLE);
        roleStatusRepository.save(managerStatus);
        log.info(managerStatus + " saved to database.");

        RoleStatus adminStatus = new RoleStatus(Constants.ROLE_STATUS_ADMIN_ID, Constants.ROLE_STATUS_ADMIN_ROLE_TITLE);
        roleStatusRepository.save(adminStatus);
        log.info(adminStatus + " saved to database.");
    }

    @Bean
    @Profile("test")
    CommandLineRunner initTestDatabase(LogRepository logRepository, ProjectRepository projectRepository,
                                       RoleStatusRepository roleStatusRepository, UserRepository userRepository){
        return args -> {
            initRoleStatus(roleStatusRepository);

            Project project1 = new Project();
            project1.setTitle("project1");
            project1.setDescription("description for project1.");
            projectRepository.save(project1);
            log.info(project1 + " saved to database.");

            Project project2 = new Project();
            project2.setTitle("project2");
            project2.setDescription("description for project2.");
            projectRepository.save(project2);
            log.info(project2 + " saved to database.");

            User admin = new User();
            admin.setEmail("admin@dev.dev");
            admin.setFirstName("admin");
            admin.setLastName("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRoleStatus(roleStatusRepository.findById(Constants.ROLE_STATUS_ADMIN_ID).orElse(null));
            userRepository.save(admin);
            log.info(admin + " saved to database.");

            User manager1 = new User();
            manager1.setEmail("manager1@dev.dev");
            manager1.setFirstName("manager1");
            manager1.setLastName("manager1");
            manager1.setPassword(passwordEncoder.encode("manager1"));
            manager1.setRoleStatus(roleStatusRepository.findById(Constants.ROLE_STATUS_MANAGER_ID).orElse(null));
            manager1.addProject(project1);
            userRepository.save(manager1);
            log.info(manager1 + " saved to database.");

            User manager2 = new User();
            manager2.setEmail("manager2@dev.dev");
            manager2.setFirstName("manager2");
            manager2.setLastName("manager2");
            manager2.setPassword(passwordEncoder.encode("manager2"));
            manager2.setRoleStatus(roleStatusRepository.findById(Constants.ROLE_STATUS_MANAGER_ID).orElse(null));
            userRepository.save(manager2);
            log.info(manager2 + " saved to database.");

            User user1 = new User();
            user1.setEmail("dev1@dev.dev");
            user1.setFirstName("dev1");
            user1.setLastName("dev1");
            user1.setPassword(passwordEncoder.encode("dev1"));
            user1.setRoleStatus(roleStatusRepository.findById(Constants.ROLE_STATUS_USER_ID).orElse(null));
            userRepository.save(user1);
            log.info(user1 + " saved to database.");

            User user2 = new User();
            user2.setEmail("dev2@dev.dev");
            user2.setFirstName("dev2");
            user2.setLastName("dev2");
            user2.setPassword(passwordEncoder.encode("dev2"));
            user2.setRoleStatus(roleStatusRepository.findById(Constants.ROLE_STATUS_USER_ID).orElse(null));
            userRepository.save(user2);
            log.info(user2 + " saved to database.");

            log.info("All seem to be OK for the app initialization!");
        };
    }

}
