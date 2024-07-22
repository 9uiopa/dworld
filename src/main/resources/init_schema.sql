CREATE TABLE board_type
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT unique_name UNIQUE (name)
);

CREATE TABLE user
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(255) UNIQUE NOT NULL,
    password   VARCHAR(255)        NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE article
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    title         VARCHAR(50) NOT NULL,
    content       TEXT NULL,
    user_id       BIGINT      NOT NULL,
    board_type_id BIGINT      NOT NULL,
    created       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated       TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (board_type_id) REFERENCES board_type (id),
    FOREIGN KEY (user_id) REFERENCES user (id)
);

CREATE TABLE comment
(
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    article_id        BIGINT,
    user_id           BIGINT,
    parent_comment_id BIGINT NULL,
    content           TEXT NOT NULL,
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (article_id) REFERENCES article (id),
    FOREIGN KEY (user_id) REFERENCES user (id),
    FOREIGN KEY (parent_comment_id) REFERENCES comment (id) ON DELETE CASCADE
);

CREATE TABLE vote
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT,
    article_id BIGINT,
    vote_type  ENUM('upvote', 'downvote') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY unique_vote (user_id, article_id),
    FOREIGN KEY (user_id) REFERENCES user (id),
    FOREIGN KEY (article_id) REFERENCES article (id)
);