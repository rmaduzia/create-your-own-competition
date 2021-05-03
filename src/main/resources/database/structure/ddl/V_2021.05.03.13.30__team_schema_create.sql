CREATE TABLE team
(
    id                  BIGINT PRIMARY KEY,
    city                VARCHAR(255),
    is_open_recruitment BOOLEAN,
    max_amount_members  INT,
    team_name           VARCHAR(30) NOT NULL,
    team_owner          VARCHAR(255)

);


