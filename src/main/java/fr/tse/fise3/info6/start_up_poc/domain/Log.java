package fr.tse.fise3.info6.start_up_poc.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Log {

    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
    private LocalDateTime start;
    private LocalDateTime end;

    @OneToOne
    private User user;

}
