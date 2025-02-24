CREATE TABLE notifications (
           id BIGINT AUTO_INCREMENT PRIMARY KEY,
           user_id BIGINT NOT NULL,        -- 接收通知的用户ID
           message VARCHAR(255) NOT NULL,   -- 通知内容
           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 消息发送时间
           is_read BOOLEAN DEFAULT FALSE,  -- 是否已读
           type VARCHAR(50)                -- 消息类型，如支付提醒、系统通知等
);