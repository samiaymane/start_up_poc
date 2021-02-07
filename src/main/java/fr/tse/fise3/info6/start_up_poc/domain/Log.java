package fr.tse.fise3.info6.start_up_poc.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class Log {

    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
    private LocalDateTime start;
    private LocalDateTime end;

    @OneToOne
    @JsonIgnoreProperties({"subordinates","manager"})
    private User user;

    @ManyToOne
    @JsonIgnoreProperties("logs")
    @ToString.Exclude
    private Project project;

}
