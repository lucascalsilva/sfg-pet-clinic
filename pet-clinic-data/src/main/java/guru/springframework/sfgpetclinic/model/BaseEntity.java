package guru.springframework.sfgpetclinic.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@MappedSuperclass
public class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime creationDate;

    private LocalDateTime lastUpdateDate;

    public BaseEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @PrePersist
    public void setCreationDate(){
        this.creationDate = LocalDateTime.now();
    }

    @PreUpdate
    public void setLastUpdateDate(){
        this.lastUpdateDate = LocalDateTime.now();
    }
}
