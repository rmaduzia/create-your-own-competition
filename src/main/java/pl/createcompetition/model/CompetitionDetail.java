package pl.createcompetition.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;


@Entity
@Getter
@Setter
public class CompetitionDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="competition_detail_id")
    private Long id;

    @NotBlank(message="Competition can't be empty")
    private int maxAmountUsers;

    @Column(columnDefinition="DATE")
    private java.sql.Date competitionStart;
//java.sql.Date = "RRRR-MM-DD"
    @Column(columnDefinition = "DATE")
    private java.sql.Date competitionEnd;

    @MapsId
    @OneToOne
    Competition competition;



}
