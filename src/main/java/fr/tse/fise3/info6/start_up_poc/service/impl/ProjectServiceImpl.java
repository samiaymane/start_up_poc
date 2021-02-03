package fr.tse.fise3.info6.start_up_poc.service.impl;

import fr.tse.fise3.info6.start_up_poc.dao.LogRepository;
import fr.tse.fise3.info6.start_up_poc.dao.ProjectRepository;
import fr.tse.fise3.info6.start_up_poc.domain.Log;
import fr.tse.fise3.info6.start_up_poc.domain.Project;
import fr.tse.fise3.info6.start_up_poc.service.ProjectService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private LogRepository logRepository;

    @Override
    @Transactional(readOnly = true)
    public Collection<Project> findAllProjects() {
        return this.projectRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Project findProject(Long id){
        return this.projectRepository.findById(id).orElse(null);
    }

    @Override
    public Project createProject(Project project) {
        return this.projectRepository.save(project);
    }

    @Override
    public void deleteProject(Project project) {
        project.clearLogs();
        this.projectRepository.delete(project);
    }

    @Override
    public Project affectLogToProject(Project project, Log log){
        Project oldProject = log.getProject();

        if(oldProject != null){
            throw new IllegalStateException();
        }
        log.setProject(project);
        project.getLogs().add(log);
        return project;
    }

    @Override
    public Log createLog(Log log) {
        return this.logRepository.save(log);
    }

    @Override
    public void deleteLog(Log log) {
        log.getProject().getLogs().remove(log);
        log.setProject(null);
        this.logRepository.delete(log);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Log> findLogsForProject(Project project){

        Project foundProject = this.findProject(project.getId());

        if(foundProject != null){
            Hibernate.initialize(foundProject.getLogs());
            return foundProject.getLogs();
        }else{
            return new HashSet<>();
        }
    }
}
