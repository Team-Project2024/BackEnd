package Hoseo.GraduationProject.Domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "school_event")
@NoArgsConstructor
public class SchoolEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "event_name")
    private String eventName;

    @Column(name = "event_period")
    private String eventPeriod;

    @Column(name = "description")
    private String description;

    @Column(name = "is_cancled")
    private boolean isCancled;

    @Column(name = "modified")
    private boolean modified;

    @Builder
    SchoolEvent(Long id, String eventName, String eventPeriod
    , String description, boolean isCancled, boolean modified){
        this.id = id;
        this.eventName = eventName;
        this.eventPeriod = eventPeriod;
        this.description = description;
        this.isCancled = isCancled;
        this.modified = modified;
    }

}
