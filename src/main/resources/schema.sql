CREATE TABLE costamtest (
    id          INTEGER PRIMARY KEY,
    description VARCHAR(64) NOT NULL,
    completed   BIT NOT NULL);



CREATE TABLE users(
    user_id BIGINT NOT NULL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    email_verified BOOLEAN DEFAULT FALSE NOT NULL,
    password VARCHAR(255) NOT NULL,
    provider VARCHAR(255),
    PROVIDER_ID VARCHAR(255)


);

CREATE TABLE user_detail(
    user_user_id BIGINT NOT NULL PRIMARY KEY,
    age INT,
    city VARCHAR(255),
    GENDER VARCHAR(255),
    user_name VARCHAR(255) UNIQUE ,

    CONSTRAINT FK_USER_DETAIL_USER_ID
        FOREIGN KEY (user_user_id)
            REFERENCES users(user_id)
);



CREATE TABLE team(
                     id BIGINT PRIMARY KEY,
                     city VARCHAR(255),
                     is_open_recruitment BOOLEAN,
                     max_amount_members INT,
                     team_name VARCHAR(30) NOT NULL,
                     team_owner VARCHAR(255)

);


CREATE TABLE tournament(
    id BIGINT NOT NULL PRIMARY KEY,
    city VARCHAR(255),
    max_amount_of_teams INT,
    street VARCHAR(255) DEFAULT NULL,
    street_number INT,
    tournament_name VARCHAR(255),
    tournament_owner VARCHAR(255),
    is_finished BOOLEAN,
    is_started BOOLEAN,
    tournament_start TIMESTAMP


);



CREATE TABLE competition(
                            id BIGINT PRIMARY KEY,
                            city VARCHAR(255),
                            competition_end DATE,
                            competition_name VARCHAR(255),
                            competition_start DATE,
                            is_open_recruitment BOOLEAN,
                            max_amount_users INT,
                            owner VARCHAR(255),
                            street VARCHAR(255),
                            street_number INT,
                            max_amount_of_teams INT,
                            competition_owner VARCHAR(255)


);




CREATE TABLE tags(
    id BIGINT PRIMARY KEY,
    tag VARCHAR(255)



);

CREATE TABLE competition_tag(
    competition_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    PRIMARY KEY(competition_id, tag_id),

    KEY FK_COMPETITION_TAG_TAGS_ID (tag_id),

    CONSTRAINT FK_COMPETITION_TAG_TAGS_ID
        FOREIGN KEY(tag_id)
            REFERENCES tags(id),

    CONSTRAINT FK_COMPETITION_TAG_COMPETITION_ID
        FOREIGN KEY(competition_id)
            REFERENCES competition(id)

);

CREATE TABLE competition_team(
    competition_id BIGINT NOT NULL,
    team_id BIGINT NOT NULL,

    PRIMARY KEY(competition_id, team_id),

    KEY FK_COMPETITION_TEAM_TEAM_ID(team_id),

    CONSTRAINT FK_COMPETITION_TEAM_TEAM_ID
        FOREIGN KEY (team_id)
            REFERENCES team(id),

    CONSTRAINT FK_COMPETITION_TEAM_COMPETITION_ID
        FOREIGN KEY(competition_id)
            REFERENCES competition(id)
);


CREATE TABLE teams_tags(
    team_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    PRIMARY KEY(team_id, tag_id),

    CONSTRAINT FK_TEAM_TAGS_TEAM_ID
        FOREIGN KEY(team_id)
            REFERENCES team(id),


    CONSTRAINT FK_TEAM_TAGS_TAG_ID
        FOREIGN KEY(tag_id)
            REFERENCES tags(id)

);


CREATE TABLE tournament_tags(
    tournament_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,

    PRIMARY KEY (tournament_id, tag_id),

    CONSTRAINT FK_TOURNAMENT_TAGS_TOURNAMENT_ID
        FOREIGN KEY(tournament_id)
            REFERENCES tournament(id),

    CONSTRAINT FK_TOURNAMENT_TAGS_TAG_ID
        FOREIGN KEY(tag_id)
            REFERENCES tags(id)

);

CREATE TABLE tournament_teams(
    tournament_id BIGINT NOT NULL,
    team_id BIGINT NOT NULL,

    PRIMARY KEY(tournament_id, team_id),

    CONSTRAINT FK_TOURNAMENT_TEAMS_TOURNAMENT_ID
        FOREIGN KEY(tournament_id)
            REFERENCES tournament(id),

    CONSTRAINT FK_TOURNAMENT_TEAMS_TEAM_ID
        FOREIGN KEY(team_id)
            REFERENCES team(id)

);

CREATE TABLE matches_in_tournament(
    id BIGINT PRIMARY KEY,
    confirming_winner_counter INT,
    first_team_name VARCHAR(255),
    second_team_name VARCHAR(255),
    is_winner_confirmed BOOLEAN,
    match_date DATE,
    winner_team VARCHAR(255),
    tournament_id BIGINT,
    is_match_was_played BOOLEAN


);


CREATE TABLE drawed_teams_in_tournament(
    tournament_id BIGINT NOT NULL,
    id VARCHAR(255) NOT NULL,
    duel VARCHAR(255),

    PRIMARY KEY(tournament_id, id),

    CONSTRAINT FK_DRAWED_TEAMS_IN_TOURNAMENT_TOURNAMENT_ID
        FOREIGN KEY(tournament_id)
            REFERENCES tournament(id)


);


CREATE TABLE matches_in_competition(
                                       id BIGINT PRIMARY KEY,
                                       first_team_name VARCHAR(255),
                                       second_team_name VARCHAR(255),
                                       is_winner_confirmed BOOLEAN,
                                       is_match_was_played BOOLEAN,
                                       match_date DATE,
                                       winner_team VARCHAR(255),
                                       competition_id BIGINT


);


CREATE TABLE votes_for_winning_team_in_competition_matches(

    match_in_competition_id BIGINT NOT NULL,
    user_name VARCHAR(255) NOT NULL,
    team_name VARCHAR(255) DEFAULT NULL,

    PRIMARY KEY(match_in_competition_id, user_name),

    CONSTRAINT FK_VOTES_FOR_WINNING_TEAM_IN_COMPETITION_MATCHES_COMPETITION_ID
        FOREIGN KEY (match_in_competition_id)
            REFERENCES matches_in_competition(id)

);

CREATE TABLE match_in_tournament(
    id BIGINT PRIMARY KEY,
    first_team_name VARCHAR(255),
    second_team_name VARCHAR(255),
    is_closed BOOLEAN,
    is_match_was_played BOOLEAN,
    is_winner_confirmed BOOLEAN,
    match_date DATE,
    winner_team VARCHAR(255),
    tournament_id BIGINT


);


CREATE TABLE match_in_competition(
    id BIGINT PRIMARY KEY,
    first_team_name VARCHAR(255),
    second_team_name VARCHAR(255),
    is_closed BOOLEAN,
    is_match_was_played BOOLEAN,
    is_winner_confirmed BOOLEAN,
    match_date DATE,
    winner_team VARCHAR(255),
    competition_id BIGINT


);












