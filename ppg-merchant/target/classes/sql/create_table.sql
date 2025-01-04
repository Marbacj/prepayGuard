CREATE TABLE merchants (
       merchant_id INT AUTO_INCREMENT PRIMARY KEY,
       merchant_name VARCHAR(50) NOT NULL,
       password VARCHAR(255) NOT NULL, -- 增加密码字段长度，建议存储哈希值
       id_card_number VARCHAR(18) NOT NULL,
       phone_number VARCHAR(20) NOT NULL,
       amount DECIMAL(10,2) DEFAULT 0.00, -- 格式化默认值
       create_at DATETIME DEFAULT CURRENT_TIMESTAMP, -- 确保 MySQL 版本支持
       UNIQUE(merchant_name, id_card_number)
);