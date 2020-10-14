package pl.createcompetition.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompetitionTags {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true)
    private String tag;

    @ManyToMany(mappedBy = "tags")
    @JsonManagedReference
    private Set<Competition> competitions = new HashSet<>();

}
