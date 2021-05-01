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
    user_user_id PRIMARY KEY CONSTRAINT FK_USER_DETAIL_USER_ID REFERENCES users
    age INT,
    city VARCHAR(255),
    GENDER VARCHAR(255),
    user_name VARCHAR(255) UNIQUE ,


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
                            competition_owner VARCHAR(255),


);




CREATE TABLE tags(
    id BIGINT PRIMARY KEY,
    tag VARCHAR(255)



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
    is_match_was_played BOOLEAN,


);


CREATE TABLE matches_in_competition(
    id BIGINT PRIMARY KEY,
    first_team_name VARCHAR(255),
    second_team_name VARCHAR(255),
    is_winner_confirmed BOOLEAN,
    is_match_was_played BOOLEAN,
    match_date DATE,
    winner_team(VARCHAR255),
    competition_id BIGINT


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












