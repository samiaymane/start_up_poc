package fr.tse.fise3.info6.start_up_poc.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Project {

    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;

    @NotNull(message = "Title cannot be null")
    @NotEmpty(message = "Title cannot be empty")
    private String title;

    private String description;
    private LocalDate creationDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnoreProperties("projects")
    @EqualsAndHashCode.Exclude
    @NotEmpty(message = "Developers cannot be empty")
    private Set<User> users;

    @OneToMany(mappedBy="project", cascade={CascadeType.ALL}, orphanRemoval=true)
    @EqualsAndHashCode.Exclude
    private Set<Log> logs;

    public Project(){
        this.logs = new HashSet<>();
        this.users = new HashSet<>();
    }

    public void addUser(User user){
        user.getProjects().add(this);
        this.users.add(user);
    }

    public void addLog(Log log){
        this.logs.add(log);
    }

    public void clearLogs(){
        for (Log log :  this.logs) {
            log.setProject(null);
        }
        this.logs.clear();
    }

}
