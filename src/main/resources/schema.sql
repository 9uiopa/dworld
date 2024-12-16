CREATE TABLE IF NOT EXISTS board_type
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT unique_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS users
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    email      VARCHAR(255) UNIQUE NOT NULL,
    password   VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS article
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    title         VARCHAR(50) NOT NULL,
    content       TEXT NULL,
    user_id       BIGINT      NOT NULL,
    board_type_id BIGINT      NOT NULL,
    created       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated       TIMESTAMP DEFAULT null ON UPDATE CURRENT_TIMESTAMP,
    enabled       BIT       DEFAULT true,
    downvotes     INT       DEFAULT 0,
    upvotes       INT       DEFAULT 0,
    FOREIGN KEY (board_type_id) REFERENCES board_type (id) ,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS comment
(
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    article_id        BIGINT,
    user_id           BIGINT,
    parent_comment_id BIGINT NULL,
    content           TEXT NOT NULL,
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    enabled           BIT       DEFAULT true,

    FOREIGN KEY (article_id) REFERENCES article (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (parent_comment_id) REFERENCES comment (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS vote
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT,
    article_id BIGINT,
    vote_type  ENUM('upvote', 'downvote') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY unique_vote (user_id, article_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (article_id) REFERENCES article (id) ON DELETE CASCADE
);