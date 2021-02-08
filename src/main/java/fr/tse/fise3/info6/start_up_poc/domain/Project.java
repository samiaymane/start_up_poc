package fr.tse.fise3.info6.start_up_poc.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
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
    @NotBlank(message = "Title cannot be blank.")
    private String title;

    private String description;
    private LocalDate creationDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"subordinates","manager","projects"})
    @EqualsAndHashCode.Exclude
    private Set<User> users;

    @OneToMany(mappedBy="project", cascade={CascadeType.ALL}, orphanRemoval=true)
    @JsonIgnoreProperties({"user"})
    @EqualsAndHashCode.Exclude
    private Set<Log> logs;

    public Project(){
        this.logs = new HashSet<>();
        this.users = new HashSet<>();
        this.creationDate = LocalDate.now();
    }

    public void addLog(Log log){
        this.logs.add(log);
        log.setProject(this);
    }

    public void clearLogs(){
        for (Log log :  this.logs) {
            log.setProject(null);
        }
        this.logs.clear();
    }

    public void removeUser(User user){
        this.users.remove(user);
    }

    public void clearUsers(){
        for (User user : this.users){
            user.removeProject(this);
        }
        this.users.clear();
    }

}
