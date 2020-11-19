package pl.createcompetition.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MatchTeamsInTournament {

    public static HashMap<String, String> matchTeamsInTournament(List<String> teamsName) {

        char[] alphabetChars = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
        HashMap<String, String> matchedTeams = new HashMap<>();
        String team1;
        String team2;
        int charIndex = 0;

        do {
            Collections.shuffle(teamsName);
            team1 = teamsName.get(0);
            teamsName.remove(0);
            Collections.shuffle(teamsName);
            team2 = teamsName.get(0);
            teamsName.remove(0);
            matchedTeams.put(String.valueOf(alphabetChars[charIndex]), team1 + "&&" + team2);
            charIndex += 1;
        } while(teamsName.size()!=0);

        return matchedTeams;
    }

    public static HashMap<Integer, String> matchTeamsWithEachOtherInTournament(List<String> teamsName) {
         int index =0;
        HashMap<Integer, String> matchedTeams = new HashMap<>();
        for (int i=0; i<teamsName.size(); i++) {
            if (teamsName.size() %2 ==0) {
                matchedTeams.put(index, teamsName.get(i));
                index += 1;
                if (i == teamsName.size()-1) {
                    teamsName.remove(i);
                }
            }
        }
        return matchedTeams;
    }


}
