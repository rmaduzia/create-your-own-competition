package pl.createcompetition.util;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.createcompetition.model.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MatchTeamsInTournamentTest {


    Team team1;
    Team team2;
    Team team3;


    @BeforeEach
    public void setUp() {

        team1 = Team.builder()
                .id(1L)
                .teamOwner("test1@mail.com")
                .teamName("team1")
                .isOpenRecruitment(true)
                .city("Gdynia").build();


        team2 = Team.builder()
                .id(2L)
                .teamOwner("test2@mail.com")
                .teamName("team2")
                .isOpenRecruitment(true)
                .city("Gdynia").build();

        team3 = Team.builder()
                .id(3L)
                .teamOwner("test3@mail.com")
                .teamName("team3")
                .isOpenRecruitment(true)
                .city("Gdynia").build();

    }


    @Test
    public void matchTeamsInTournament() {

        List<String> listOfTeams = new ArrayList<>();
      //  List<String> listOfTeamName = Arrays.asList("Team1", "Team2", "Team3");
        listOfTeams.add("Team1");
        listOfTeams.add("Team2");
        listOfTeams.add("Team3");




        Map<String,String> matchedTeams = MatchTeamsInTournament.matchTeamsInTournament(listOfTeams);

        System.out.println(matchedTeams);





    }







}
