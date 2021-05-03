CREATE TABLE users
(
    user_id        BIGINT                     NOT NULL PRIMARY KEY,
    email          VARCHAR(255)               NOT NULL UNIQUE,
    email_verified BOOLEAN      DEFAULT FALSE NOT NULL,
    image_url      VARCHAR(255) DEFAULT NULL,
    password       VARCHAR(255)               NOT NULL,
    provider       VARCHAR(255),
    provider_id    VARCHAR(255)


);

CREATE TABLE user_detail
(
    user_user_id BIGINT NOT NULL PRIMARY KEY,
    age          INT,
    city         VARCHAR(255),
    GENDER       VARCHAR(255),
    user_name    VARCHAR(255) UNIQUE,

    CONSTRAINT FK_USER_DETAIL_USER_ID
        FOREIGN KEY (user_user_id)
            REFERENCES users (user_id)
);


CREATE TABLE user_detail_opinion_about_users(
                                                user_detail_id BIGINT NOT NULL,
                                                user_name VARCHAR(255) NOT NULL,
                                                opinion VARCHAR(255) DEFAULT NULL,

                                                PRIMARY KEY(user_detail_id, user_name),

                                                CONSTRAINT FK_USER_DETAIL_OPINION_ABOUT_USERS_USER_DETAIL_ID
                                                    FOREIGN KEY(user_detail_id)
                                                        REFERENCES user_detail(user_user_id)

);




CREATE TABLE user_teams
(
    user_id BIGINT NOT NULL,
    team_id BIGINT NOT NULL,

    PRIMARY KEY (user_id, team_id),

    CONSTRAINT FK_USER_TEAMS_USER_ID
        FOREIGN KEY (user_id)
            REFERENCES user_detail (user_user_id),

    CONSTRAINT FK_USER_TEAMS_TEAM_ID
        FOREIGN KEY (team_id)
            REFERENCES team (id)

);

CREATE TABLE user_competitions
(
    user_id        BIGINT NOT NULL,
    competition_id BIGINT NOT NULL,

    PRIMARY KEY (user_id, competition_id),

    CONSTRAINT FK_USER_COMPETITIONS_USER_ID
        FOREIGN KEY (user_id)
            REFERENCES user_detail (user_user_id),

    CONSTRAINT FK_USER_COMPETITIONS_COMPETITION_ID
        FOREIGN KEY (competition_id)
            REFERENCES competitions (id)

);