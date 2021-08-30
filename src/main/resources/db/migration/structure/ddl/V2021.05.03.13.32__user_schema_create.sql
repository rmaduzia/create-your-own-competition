CREATE TABLE users
(
    id             BIGINT                     NOT NULL PRIMARY KEY AUTO_INCREMENT,
    email          VARCHAR(255)               NOT NULL UNIQUE,
    email_verified BOOLEAN                    DEFAULT FALSE NOT NULL,
    image_url      VARCHAR(255)               DEFAULT NULL,
    password       VARCHAR(255)               NOT NULL,
    provider       VARCHAR(255),
    provider_id    VARCHAR(255)


);

CREATE TABLE user_detail
(
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    age          INT,
    city         VARCHAR(255),
    GENDER       VARCHAR(255),
    user_name    VARCHAR(255) UNIQUE,

    CONSTRAINT FK_USER_DETAIL_USER_ID
        FOREIGN KEY (id)
            REFERENCES users (id)
);


CREATE TABLE user_detail_opinion_about_users
(
    user_detail_id BIGINT       NOT NULL,
    user_name      VARCHAR(255) NOT NULL,
    opinion        VARCHAR(255) DEFAULT NULL,

    PRIMARY KEY (user_detail_id, user_name),

    CONSTRAINT FK_USER_DETAIL_OPINION_ABOUT_USERS_USER_DETAIL_ID
        FOREIGN KEY (user_detail_id)
            REFERENCES user_detail (id)
);