CREATE TABLE IF NOT EXISTS USERS
(
    ID                    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    USER_NAME             VARCHAR(255)                            NOT NULL,
    USER_EMAIL            VARCHAR(255)                            NOT NULL,
    CONSTRAINT PK_USERS PRIMARY KEY (ID)
    );

CREATE TABLE IF NOT EXISTS CATEGORYS
(
    ID                        BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    CATEGORY_NAME             VARCHAR(255)                            NOT NULL,
    CONSTRAINT PK_CATEGORYS   PRIMARY KEY (ID)
    );

CREATE TABLE IF NOT EXISTS EVENTS
(
    ID                        BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    ANNOTATION                VARCHAR(255)                            NOT NULL,
    CATEGORY_ID               INTEGER                                 NOT NULL,
    CONFIMED_REQUESTS         INTEGER                                 NOT NULL,
    CREATED_ON                TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    DESCRIPTION               VARCHAR(255)                            NOT NULL,
    EVENT_DATE                TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    USER_ID                   INTEGER                                 NOT NULL,
    LAT                       FLOAT                                   NOT NULL,
    LON                       FLOAT                                   NOT NULL,
    PAID                      BOOLEAN                                 NOT NULL,
    PARTICIPANT_LIMIT         INTEGER                                 NOT NULL,
    PUBLISHED_ON              TIMESTAMP WITHOUT TIME ZONE             ,
    REQUEST_MODERATION        BOOLEAN                                 NOT NULL,
    EVENTS_STATE              VARCHAR(255)                            NOT NULL,
    TITLE                     VARCHAR(255)                            NOT NULL,
    VIEWS                     VARCHAR(255)                            NOT NULL,

    CONSTRAINT PK_EVENTS   PRIMARY KEY (ID)
    );


