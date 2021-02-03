package fr.tse.fise3.info6.start_up_poc.service;

import fr.tse.fise3.info6.start_up_poc.domain.Log;
import fr.tse.fise3.info6.start_up_poc.domain.Project;

import java.util.Collection;

public interface ProjectService {

    public Collection<Project> findAllProjects();

    public Project findProject(Long id);

    public Project createProject(Project project);

    public void deleteLog(Log log);

    public void deleteProject(Project project);

    public Project affectLogToProject(Project project, Log log);

    public Log createLog(Log log);

    public Collection<Log> findLogsForProject(Project project);

}
