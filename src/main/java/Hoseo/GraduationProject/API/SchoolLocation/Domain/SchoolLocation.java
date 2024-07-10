package Hoseo.GraduationProject.API.SchoolLocation.Domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "school_location")
@NoArgsConstructor
public class SchoolLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "category")
    private String category;

    @Column(name = "location_name")
    private String locationName;

    @Column(name = "lat")
    private String lat;

    @Column(name = "lon")
    private String lon;
}
