package fr.tse.fise3.info6.start_up_poc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.tse.fise3.info6.start_up_poc.utils.EmailValid;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class User implements UserDetails {

    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;

    @NotNull(message = "First name cannot be null")
    @NotEmpty(message = "First name cannot be empty")
    @NotBlank(message = "First name cannot be blank.")
    private String firstName;

    @NotNull(message = "Last Name cannot be null")
    @NotEmpty(message = "Last Name cannot be empty")
    @NotBlank(message = "Last Name cannot be blank.")
    private String lastName;

    @NotNull(message = "Email cannot be null")
    @NotEmpty(message = "Email cannot be empty")
    @NotBlank(message = "Email cannot be blank.")
    @EmailValid
    private String email;

    @NotNull(message = "Password cannot be null")
    @NotEmpty(message = "Title cannot be empty")
    @NotBlank(message = "Title cannot be blank.")
    @Size(min = 6)
    private String password;

    @ManyToOne
    @Valid
    private RoleStatus roleStatus;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy="users", fetch= FetchType.EAGER)
    private Set<Project> projects;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnoreProperties({"subordinates","manager"})
    @OneToMany(mappedBy = "manager")
    private Set<User> subordinates;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnoreProperties({"subordinates","manager"})
    @ManyToOne
    private User manager;

    public void addSubordinate(User user){
        User oldManager = user.getManager();
        if(oldManager != null) {
            oldManager.removeSubordinate(user);
        }
        this.subordinates.add(user);
        user.setManager(this);
    }

    public void removeSubordinate(User user){
        this.subordinates.remove(user);
    }

    public void clearSubordiantes(){
        for(User user : this.subordinates){
            user.setManager(null);
        }
        this.subordinates.clear();
    }

    public User(){
        this.projects = new HashSet<>();
        this.subordinates = new HashSet<>();

    }

    public void addProject(Project project){
        project.getUsers().add(this);
        this.projects.add(project);
    }

    public void removeProject(Project project){
        this.projects.remove(project);
    }

    public void clearProjects(){
        for (Project project : this.projects){
            project.removeUser(this);
        }
        this.projects.clear();
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        GrantedAuthority grantedAuthority = (GrantedAuthority) () -> roleStatus.getRoleTitle();
        return List.of(grantedAuthority);
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return null;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return false;
    }
}
