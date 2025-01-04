CREATE TABLE users(
       user_id INT AUTO_INCREMENT PRIMARY KEY,
       user_name VARCHAR(50) NOT NULL,
       password VARCHAR(20) NOT NULL ,
       id_card_number VARCHAR(18) NOT NULL,
       phone_number VARCHAR(11) NOT NULL,
       account_name VARCHAR(20) NOT NULL,
       email VARCHAR(50),
       balance DECIMAL(10,2) DEFAULT 0,
       create_at DATETIME DEFAULT CURRENT_TIMESTAMP,
       UNIQUE (user_name, id_card_number)
)