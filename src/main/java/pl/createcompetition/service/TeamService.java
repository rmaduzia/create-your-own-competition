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
        super(userRepository, teamRepository);
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

        verifyUserExists(userPrincipal);

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

    public ResponseEntity<?> updateTeam (Team team, UserPrincipal userPrincipal) {

        verifyUserExists(userPrincipal);
        Optional<Team> foundTeam = shouldFindTeam(team.getTeamName(), userPrincipal.getUsername());
        checkIfTeamBelongToUser(foundTeam.get(), userPrincipal);

        return ResponseEntity.ok(teamRepository.save(team));

    }

    public ResponseEntity<?> deleteTeam (String teamName, UserPrincipal userPrincipal) {

        verifyUserExists(userPrincipal);
        Optional<Team> foundTeam = shouldFindTeam(teamName, userPrincipal.getUsername());
        checkIfTeamBelongToUser(foundTeam.get(), userPrincipal);

        teamRepository.deleteById(foundTeam.get().getId());

        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<?> addRecruitToTeam(String teamName, String recruitName, UserPrincipal userPrincipal) {

        verifyUserExists(userPrincipal);
        Optional<Team> foundTeam = shouldFindTeam(teamName, userPrincipal.getUsername());
        checkIfTeamBelongToUser(foundTeam.get(), userPrincipal);

        Optional<UserDetail> findRecruit = Optional.ofNullable(userDetailRepository.findByUserName(recruitName).orElseThrow(() ->
                new ResourceNotFoundException("UserName not exists", "Name", recruitName)));

        foundTeam.get().addRecruitToTeam(findRecruit.get());
        teamRepository.save(foundTeam.get());

        notificationMessagesToUsersService.notificationMessageToUser(recruitName, "Team","invite", foundTeam.get().getTeamName());

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> deleteMemberFromTeam(String teamName, String userNameToDelete, UserPrincipal userPrincipal) {

        verifyUserExists(userPrincipal);
        Optional<Team> foundTeam = shouldFindTeam(teamName, userPrincipal.getUsername());
        checkIfTeamBelongToUser(foundTeam.get(), userPrincipal);

        Optional<UserDetail> findRecruit = Optional.ofNullable(userDetailRepository.findByUserName(userNameToDelete).orElseThrow(() ->
                new ResourceNotFoundException("UserName not exists", "Name", userNameToDelete)));

        checkIfUserIsMemberOfTeam(foundTeam.get(), findRecruit.get());

        foundTeam.get().deleteRecruitFromTeam(findRecruit.get());

        teamRepository.save(foundTeam.get());
        notificationMessagesToUsersService.notificationMessageToUser(userNameToDelete, "Have been","deleted", foundTeam.get().getTeamName());

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> teamJoinTournament(String teamName, String tournamentName,UserPrincipal userPrincipal) {

        verifyUserExists(userPrincipal);
        Optional<Team> foundTeam = shouldFindTeam(teamName, userPrincipal.getUsername());
        checkIfTeamBelongToUser(foundTeam.get(), userPrincipal);

        Optional<Tournament> findTournament = getTournament(tournamentName);

        if (findTournament.get().getMaxAmountOfTeams() == findTournament.get().getTeams().size()) {
            throw new BadRequestException("There is already the maximum number of teams");
        }

        foundTeam.get().addTeamToTournament(findTournament.get());


        // Send notification to Team Members
        for (UserDetail userDetail: foundTeam.get().getUserDetails()) {
            notificationMessagesToUsersService.notificationMessageToUser(userDetail.getUserName(), "Team","joined tournament: ", tournamentName);
        }


        return ResponseEntity.ok(teamRepository.save(foundTeam.get()));
    }


    public ResponseEntity<?> teamLeaveTournament(String teamName, String tournamentName,UserPrincipal userPrincipal) {

        verifyUserExists(userPrincipal);
        Optional<Team> foundTeam = shouldFindTeam(teamName, userPrincipal.getUsername());
        checkIfTeamBelongToUser(foundTeam.get(), userPrincipal);

        Optional<Tournament> findTournament = getTournament(tournamentName);

        foundTeam.get().deleteTeamFromTournament(findTournament.get());

        // Send notification to Team Members
        for (UserDetail userDetail: foundTeam.get().getUserDetails()) {
            notificationMessagesToUsersService.notificationMessageToUser(userDetail.getUserName(), "Team","left tournament: ", tournamentName);
        }

        return ResponseEntity.ok(teamRepository.save(foundTeam.get()));
    }

    public ResponseEntity<?> teamJoinCompetition(String teamName, String competitionName,UserPrincipal userPrincipal) {

        verifyUserExists(userPrincipal);
        Optional<Team> foundTeam = shouldFindTeam(teamName, userPrincipal.getUsername());
        checkIfTeamBelongToUser(foundTeam.get(), userPrincipal);

        Optional<Competition> findCompetition = getCompetition(competitionName);

        if (findCompetition.get().getMaxAmountOfTeams() == findCompetition.get().getTeams().size()) {
            throw new BadRequestException("There is already the maximum number of teams");
        }

        foundTeam.get().addTeamToCompetition(findCompetition.get());

        // Send notification to Team Members
        for (UserDetail userDetail: foundTeam.get().getUserDetails()) {
            notificationMessagesToUsersService.notificationMessageToUser(userDetail.getUserName(), "Team","joined competition: ", competitionName);
        }


        return ResponseEntity.ok(teamRepository.save(foundTeam.get()));
    }


    public ResponseEntity<?> teamLeaveCompetition(String teamName, String competitionName,UserPrincipal userPrincipal) {

        verifyUserExists(userPrincipal);
        Optional<Team> foundTeam = shouldFindTeam(teamName, userPrincipal.getUsername());
        checkIfTeamBelongToUser(foundTeam.get(), userPrincipal);

        Optional<Competition> findCompetition = getCompetition(competitionName);

        foundTeam.get().deleteTeamFromCompetition(findCompetition.get());

        // Send notification to Team Members
        for (UserDetail userDetail: foundTeam.get().getUserDetails()) {
            notificationMessagesToUsersService.notificationMessageToUser(userDetail.getUserName(), "Team","left tournament: ", competitionName);
        }

        return ResponseEntity.ok(teamRepository.save(foundTeam.get()));
    }

/*
    public void notificationMessageToUser(String recipientUserName, String subject, String action, String event) {

        String content = notificationBuilderContent(subject, action, event);

        UserNotification userNotification = UserNotification.builder().recipient(recipientUserName).content(content).build();
        notificationRepository.save(userNotification);

        simpMessagingTemplate.convertAndSendToUser(recipientUserName, "/queue/notifications", userNotification);
    }

    public String notificationBuilderContent(String Subject,String action,  String event) {
        return Subject +" "+ action + " "+ event;
    }
 */

    public void checkIfTeamBelongToUser(Team team, UserPrincipal userPrincipal) {
            if (!team.getTeamOwner().equals(userPrincipal.getUsername())) {
                throw new ResourceNotFoundException("Team named: " + team.getTeamName(), "Owner", userPrincipal.getUsername());
            }
        }

    public Optional<Team> shouldFindTeam(String teamName, String teamOwner) {
        return Optional.ofNullable(teamRepository.findByTeamNameAndTeamOwner(teamName, teamOwner).orElseThrow(() ->
                new ResourceNotFoundException("Team not exists", "Name", teamName)));
    }

    public void checkIfUserIsMemberOfTeam(Team team, UserDetail userDetail) {
        if(!team.getUserDetails().contains(userDetail)) {
            throw new ResourceNotFoundException("User named: " + userDetail.getUserName(), "Team", team.getTeamName());
        }
    }

    private Optional<Tournament> getTournament(String tournamentName) {
        return Optional.ofNullable(tournamentRepository.findByTournamentName(tournamentName).orElseThrow(() ->
                new ResourceNotFoundException("Tournament not exists", "Name", tournamentName)));
    }

    private Optional<Competition> getCompetition(String competitionName) {
        return Optional.ofNullable(competitionRepository.findByCompetitionName(competitionName).orElseThrow(() ->
                new ResourceNotFoundException("Competition not exists", "Name", competitionName)));
    }
}