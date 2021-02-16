package pl.createcompetition.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(of = {"id", "tag"})
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tags {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true)
    @NotBlank(message = "Tag name can't be empty")
    private String tag;

    @ManyToMany(mappedBy = "tags")
    @JsonBackReference
    @Builder.Default
    private Set<Competition> competitions = new HashSet<>();

    @ManyToMany(mappedBy = "tags")
    @JsonBackReference
    @Builder.Default
    private Set<Tournament> tournaments = new HashSet<>();

    @ManyToMany(mappedBy = "tags")
    @JsonBackReference
    @Builder.Default
    private Set<Team> teams = new HashSet<>();

    @Data
    @AllArgsConstructor
    public static class TagsDto {
        private String tag;
        private Set<Competition> competitions;
        private Set<Tournament> tournaments;
        private Set<Team> teams;
    }
}



