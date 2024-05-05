package Hoseo.GraduationProject.Admin.Event.Domain;

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

    @Column(name = "is_canceled")
    private boolean isCanceled;

    @Column(name = "modified")
    private boolean modified;

    public void cancelEvent(){
        this.isCanceled = true;
    }

    public void modifiedEvent(){
        this.modified = true;
    }

    @Builder
    SchoolEvent(Long id, String eventName, String eventPeriod
    , String description, boolean isCanceled, boolean modified){
        this.id = id;
        this.eventName = eventName;
        this.eventPeriod = eventPeriod;
        this.description = description;
        this.isCanceled = isCanceled;
        this.modified = modified;
    }

}
