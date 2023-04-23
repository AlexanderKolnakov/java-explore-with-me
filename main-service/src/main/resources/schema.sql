CREATE TABLE IF NOT EXISTS HITS
(
    ID              BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    APP             VARCHAR(255)                            NOT NULL,
    URI             VARCHAR(255)                            NOT NULL,
    IP              VARCHAR(255)                            NOT NULL,
    TIME_STAMP      TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    CONSTRAINT PK_HIT PRIMARY KEY (ID)
    );

