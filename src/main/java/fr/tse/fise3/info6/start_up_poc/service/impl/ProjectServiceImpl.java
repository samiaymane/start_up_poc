package fr.tse.fise3.info6.start_up_poc.service.impl;

import fr.tse.fise3.info6.start_up_poc.dao.LogRepository;
import fr.tse.fise3.info6.start_up_poc.dao.ProjectRepository;
import fr.tse.fise3.info6.start_up_poc.dao.UserRepository;
import fr.tse.fise3.info6.start_up_poc.domain.Log;
import fr.tse.fise3.info6.start_up_poc.domain.Project;
import fr.tse.fise3.info6.start_up_poc.domain.User;
import fr.tse.fise3.info6.start_up_poc.service.ProjectService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Project> findAllProjects() {
        return this.projectRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Project findProject(Long id){
        return this.projectRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Log findLog(Long id){
        return this.logRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Project createProject(Project project) {
        return this.projectRepository.save(project);
    }

    @Override
    @Transactional
    public void deleteProject(Project project) {
        project = this.findProject(project.getId());
        project.clearLogs();
        project.clearUsers();
        this.projectRepository.delete(project);
    }

    @Override
    @Transactional
    public Project affectLogToProject(Project project, Log log){

        Project oldProject = log.getProject();

        if(oldProject != null){
            throw new IllegalStateException("Log is already assigned to another project.");
        }
        project.addLog(log);
        this.logRepository.save(log);
        return project;
    }

    @Override
    @Transactional
    public Log affectLogToUser(Log log, User user){

        User foundUser = this.userRepository.findById(user.getId()).orElse(null);
        Log foundLog = this.findLog(log.getId());

        User oldUser = foundLog.getUser();

        if(oldUser != null){
            throw new IllegalStateException("Log is already assigned to another user.");
        }
        foundLog.setUser(foundUser);
        return foundLog;
    }

    @Override
    @Transactional
    public Project affectUserToProject(Project project, User user){

        User foundUser = this.userRepository.findById(user.getId()).orElse(null);
        Project foundProject = this.findProject(project.getId());

        foundUser.addProject(foundProject);
        return foundProject;
    }

    @Override
    @Transactional
    public Log createLog(Log log) {
        return this.logRepository.save(log);
    }

    @Override
    @Transactional
    public void deleteLog(Log log) {
        Project project = log.getProject();
        if(project != null){
            project.getLogs().remove(log);
        }
        log.setProject(null);
        log.setUser(null);
        this.logRepository.save(log);
        this.logRepository.delete(log);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Log> findLogsForProject(Project project){

        Project foundProject = this.findProject(project.getId());

        if(foundProject != null){
            Hibernate.initialize(foundProject.getLogs());
            return new ArrayList<>(foundProject.getLogs());
        }else{
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Log> findLogsForUser(User user, LocalDate startDate, LocalDate endDate) {
        List<Log> logs = logRepository.findAll(Sort.by("start").ascending());
        if (startDate == null || endDate == null){
            logs = logs.stream()
                    .filter(log -> user.equals(log.getUser()))
                    .collect(Collectors.toList());
        }else{
            logs = logs.stream()
                    .filter(log -> user.equals(log.getUser())
                            && log.getStart().isAfter(startDate.atStartOfDay())
                            && log.getEnd().isBefore(endDate.atStartOfDay()))
                    .collect(Collectors.toList());
        }
        return logs;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Log> findLogsForUser(User user){
        return findLogsForUser(user, null, null);
    }
}
