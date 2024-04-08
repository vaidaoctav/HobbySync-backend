CREATE TABLE IF NOT EXISTS hobby_group (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    image VARCHAR(255),
    created TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS app_user (
    id UUID PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    bio TEXT,
    profile_picture VARCHAR(255),
    x_coord DOUBLE PRECISION,
    y_coord DOUBLE PRECISION,
    user_type VARCHAR(50) NOT NULL,
    created TIMESTAMP NOT NULL
    );

CREATE TABLE IF NOT EXISTS hobby_event (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    date_time TIMESTAMP NOT NULL,
    description VARCHAR(255) NOT NULL,
    capacity INT,
    fee INT,
    x_coord DOUBLE PRECISION,
    y_coord DOUBLE PRECISION,
    hobby_group_id UUID NOT NULL,
    created TIMESTAMP NOT NULL,
    FOREIGN KEY (hobby_group_id) REFERENCES hobby_group(id)
    );

    CREATE TABLE IF NOT EXISTS review (
        id UUID PRIMARY KEY,
        comment VARCHAR(255),
        rating INT NOT NULL,
        user_id UUID NOT NULL,
        hobby_event_id UUID NOT NULL,
        created TIMESTAMP NOT NULL,
        FOREIGN KEY (user_id) REFERENCES app_user(id),
        FOREIGN KEY (hobby_event_id) REFERENCES hobby_event(id)
        );

CREATE TABLE IF NOT EXISTS token (
    id UUID PRIMARY KEY,
    token VARCHAR(255) UNIQUE,
    token_type VARCHAR(50) NOT NULL,
    revoked BOOLEAN NOT NULL,
    expired BOOLEAN NOT NULL,
    user_id UUID NOT NULL,
    created TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES app_user(id)
    );

CREATE TABLE IF NOT EXISTS user_hobby_group (
    user_id UUID NOT NULL,
    hobby_group_id UUID NOT NULL,
    PRIMARY KEY (user_id, hobby_group_id),
    FOREIGN KEY (user_id) REFERENCES app_user(id),
    FOREIGN KEY (hobby_group_id) REFERENCES hobby_group(id)
    );

CREATE TABLE IF NOT EXISTS user_hobby_event (
    user_id UUID NOT NULL,
    hobby_event_id UUID NOT NULL,
    PRIMARY KEY (user_id, hobby_event_id),
    FOREIGN KEY (user_id) REFERENCES app_user(id),
    FOREIGN KEY (hobby_event_id) REFERENCES hobby_event(id)
    );


