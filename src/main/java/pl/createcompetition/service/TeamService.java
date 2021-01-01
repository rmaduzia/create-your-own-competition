package pl.createcompetition.service;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import pl.createcompetition.exception.BadRequestException;
import pl.createcompetition.exception.ResourceAlreadyExistException;
import pl.createcompetition.exception.ResourceNotFoundException;
import pl.createcompetition.model.*;
import pl.createcompetition.model.websockets.UserNotification;
import pl.createcompetition.payload.PaginationInfoRequest;
import pl.createcompetition.repository.*;
import pl.createcompetition.security.UserPrincipal;
import pl.createcompetition.service.query.GetQueryImplService;

import java.util.Optional;

//@AllArgsConstructor
@Service
public class TeamService extends VerifyMethodsForServices {

    private final TeamRepository teamRepository;
    private final UserDetailRepository userDetailRepository;
    private final TournamentRepository tournamentRepository;
    private final CompetitionRepository competitionRepository;
    private final NotificationMessagesToUsersService notificationMessagesToUsersService;
    private final GetQueryImplService<Team,?> queryTeamService;

    public TeamService(TeamRepository teamRepository, UserRepository userRepository, UserDetailRepository userDetailRepository, TournamentRepository tournamentRepository,
                       CompetitionRepository competitionRepository, NotificationMessagesToUsersService notificationMessagesToUsersService, GetQueryImplService<Team, ?> queryTeamService) {
        super(teamRepository);
        this.teamRepository = teamRepository;
        this.userDetailRepository = userDetailRepository;
        this.tournamentRepository = tournamentRepository;
        this.competitionRepository = competitionRepository;
        this.notificationMessagesToUsersService = notificationMessagesToUsersService;
        this.queryTeamService = queryTeamService;
    }

    public PagedResponseDto<?> searchTeam(String search, PaginationInfoRequest paginationInfoRequest) {

        return queryTeamService.execute(Team.class, search, paginationInfoRequest.getPageNumber(), paginationInfoRequest.getPageSize());
    }

    public ResponseEntity<?> addTeam (Team team, UserPrincipal userPrincipal) {

        Optional<Team> findTeam = teamRepository.findByTeamName(team.getTeamName());

        if (findTeam.isEmpty()) {
            Optional<UserDetail> userDetail = userDetailRepository.findById(userPrincipal.getId());
            team.setTeamOwner(userPrincipal.getUsername());
            userDetail.get().addUserToTeam(team);
            return ResponseEntity.ok(userDetailRepository.save(userDetail.get()));
        } else{
            throw new ResourceAlreadyExistException("Team", "Name", team.getTeamName());
        }
    }

    public ResponseEntity<?> updateTeam (String teamName, Team team, UserPrincipal userPrincipal) {

        if (!team.getTeamName().equals(teamName)) {
            throw new BadRequestException("Tean Name doesn't match with Team object");
        }

        Team foundTeam = shouldFindTeam(team.getTeamName(), userPrincipal.getUsername());
        checkIfTeamBelongToUser(foundTeam, userPrincipal);

        return ResponseEntity.ok(teamRepository.save(team));

    }

    public ResponseEntity<?> deleteTeam (String teamName, UserPrincipal userPrincipal) {

        Team foundTeam = shouldFindTeam(teamName, userPrincipal.getUsername());
        checkIfTeamBelongToUser(foundTeam, userPrincipal);

        teamRepository.deleteById(foundTeam.getId());

        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<?> addRecruitToTeam(String teamName, String recruitName, UserPrincipal userPrincipal) {

        Team foundTeam = shouldFindTeam(teamName, userPrincipal.getUsername());
        checkIfTeamBelongToUser(foundTeam, userPrincipal);

        Optional<UserDetail> findRecruit = Optional.ofNullable(userDetailRepository.findByUserName(recruitName).orElseThrow(() ->
                new ResourceNotFoundException("UserName not exists", "Name", recruitName)));

        foundTeam.addRecruitToTeam(findRecruit.get());
        teamRepository.save(foundTeam);

        notificationMessagesToUsersService.notificationMessageToUser(recruitName, "Team","invite", foundTeam.getTeamName());

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> deleteMemberFromTeam(String teamName, String userNameToDelete, UserPrincipal userPrincipal) {

        Team foundTeam = shouldFindTeam(teamName, userPrincipal.getUsername());
        checkIfTeamBelongToUser(foundTeam, userPrincipal);

        UserDetail findRecruit = userDetailRepository.findByUserName(userNameToDelete).orElseThrow(() ->
                new ResourceNotFoundException("UserName not exists", "Name", userNameToDelete));

        checkIfUserIsMemberOfTeam(foundTeam, findRecruit);

        foundTeam.deleteRecruitFromTeam(findRecruit);

        teamRepository.save(foundTeam);
        notificationMessagesToUsersService.notificationMessageToUser(userNameToDelete, "Have been","deleted", foundTeam.getTeamName());

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> teamJoinTournament(String teamName, String tournamentName,UserPrincipal userPrincipal) {

        Team foundTeam = shouldFindTeam(teamName, userPrincipal.getUsername());
        checkIfTeamBelongToUser(foundTeam, userPrincipal);

        Tournament findTournament = getTournament(tournamentName);

        if (findTournament.getMaxAmountOfTeams() == findTournament.getTeams().size()) {
            throw new BadRequestException("There is already the maximum number of teams");
        }

        foundTeam.addTeamToTournament(findTournament);


        // Send notification to Team Members
        for (UserDetail userDetail: foundTeam.getUserDetails()) {
            notificationMessagesToUsersService.notificationMessageToUser(userDetail.getUserName(), "Team","joined tournament: ", tournamentName);
        }


        return ResponseEntity.ok(teamRepository.save(foundTeam));
    }


    public ResponseEntity<?> teamLeaveTournament(String teamName, String tournamentName,UserPrincipal userPrincipal) {

        Team foundTeam = shouldFindTeam(teamName, userPrincipal.getUsername());
        checkIfTeamBelongToUser(foundTeam, userPrincipal);

        Tournament findTournament = getTournament(tournamentName);

        foundTeam.deleteTeamFromTournament(findTournament);

        // Send notification to Team Members
        for (UserDetail userDetail: foundTeam.getUserDetails()) {
            notificationMessagesToUsersService.notificationMessageToUser(userDetail.getUserName(), "Team","left tournament: ", tournamentName);
        }

        return ResponseEntity.ok(teamRepository.save(foundTeam));
    }

    public ResponseEntity<?> teamJoinCompetition(String teamName, String competitionName,UserPrincipal userPrincipal) {

        Team foundTeam = shouldFindTeam(teamName, userPrincipal.getUsername());
        checkIfTeamBelongToUser(foundTeam, userPrincipal);

        Competition findCompetition = getCompetition(competitionName);

        if (findCompetition.getMaxAmountOfTeams() == findCompetition.getTeams().size()) {
            throw new BadRequestException("There is already the maximum number of teams");
        }

        foundTeam.addTeamToCompetition(findCompetition);

        // Send notification to Team Members
        for (UserDetail userDetail: foundTeam.getUserDetails()) {
            notificationMessagesToUsersService.notificationMessageToUser(userDetail.getUserName(), "Team","joined competition: ", competitionName);
        }


        return ResponseEntity.ok(teamRepository.save(foundTeam));
    }


    public ResponseEntity<?> teamLeaveCompetition(String teamName, String competitionName,UserPrincipal userPrincipal) {

        Team foundTeam = shouldFindTeam(teamName, userPrincipal.getUsername());
        checkIfTeamBelongToUser(foundTeam, userPrincipal);

        Competition findCompetition = getCompetition(competitionName);

        foundTeam.deleteTeamFromCompetition(findCompetition);

        // Send notification to Team Members
        for (UserDetail userDetail: foundTeam.getUserDetails()) {
            notificationMessagesToUsersService.notificationMessageToUser(userDetail.getUserName(), "Team","left tournament: ", competitionName);
        }

        return ResponseEntity.ok(teamRepository.save(foundTeam));
    }

    public void checkIfTeamBelongToUser(Team team, UserPrincipal userPrincipal) {
            if (!team.getTeamOwner().equals(userPrincipal.getUsername())) {
                throw new ResourceNotFoundException("Team named: " + team.getTeamName(), "Owner", userPrincipal.getUsername());
            }
        }

    public void checkIfUserIsMemberOfTeam(Team team, UserDetail userDetail) {
        if(!team.getUserDetails().contains(userDetail)) {
            throw new ResourceNotFoundException("User named: " + userDetail.getUserName(), "Team", team.getTeamName());
        }
    }

    private Tournament getTournament(String tournamentName) {
        return tournamentRepository.findByTournamentName(tournamentName).orElseThrow(() ->
                new ResourceNotFoundException("Tournament not exists", "Name", tournamentName));
    }

    private Competition getCompetition(String competitionName) {
        return competitionRepository.findByCompetitionName(competitionName).orElseThrow(() ->
                new ResourceNotFoundException("Competition not exists", "Name", competitionName));
    }
}