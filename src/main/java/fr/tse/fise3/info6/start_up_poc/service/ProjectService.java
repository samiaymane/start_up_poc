package fr.tse.fise3.info6.start_up_poc.service;

import fr.tse.fise3.info6.start_up_poc.domain.Log;
import fr.tse.fise3.info6.start_up_poc.domain.Project;
import fr.tse.fise3.info6.start_up_poc.domain.User;

import java.util.Collection;
import java.util.List;

public interface ProjectService {

    public List<Project> findAllProjects();

    public Project findProject(Long id);

    public Log findLog(Long id);

    public Project createProject(Project project);

    public Log createLog(Log log);

    public void deleteLog(Log log);

    public void deleteProject(Project project);

    public List<Log> findLogsForProject(Project project);

    public List<Log> findLogsForUser(User user);

    public Project affectLogToProject(Project project, Log log);

    public Project affectUserToProject(Project project, User user);

    public Log affectLogToUser(Log log, User user);

}
