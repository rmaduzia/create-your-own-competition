package pl.createcompetition.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Getter
@Setter
public class CompetitionDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="competition_detail_id")
    private Long id;


    @ManyToOne
    @JoinColumn(name="competition_id")
    Competition competition;

    private int max_amount_users;











}
