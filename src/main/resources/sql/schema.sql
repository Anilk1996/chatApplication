CREATE TABLE `chat-app`.users (
	id INT auto_increment NOT NULL,
	first_name varchar(50) NOT NULL,
	last_name varchar(50) NULL,
	phone varchar(10) NULL,
	email varchar(100) NULL,
	password varchar(100) NULL,
	CONSTRAINT users_PK PRIMARY KEY (id),
	CONSTRAINT users_UN UNIQUE KEY (phone,email)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci
AUTO_INCREMENT=1;
