CREATE TABLE IF NOT EXISTS contract_templates (
    template_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL COMMENT '模板名称',
    description TEXT DEFAULT NULL COMMENT '模板描述',
    fields VARCHAR(1024) NOT NULL COMMENT '模板相关字段配置',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE IF NOT EXISTS contracts (
     contract_id BIGINT NOT NULL PRIMARY KEY COMMENT '雪花 ID 作为合同主键',
     user_id BIGINT NOT NULL,
     merchant_id BIGINT NOT NULL,
     template_id INT NOT NULL COMMENT '合同模板 ID',
     contract_code BIGINT NOT NULL UNIQUE COMMENT '雪花 ID 合同编号',
     unit_amount DECIMAL(10, 2) NOT NULL,
     total_units INT NOT NULL,
     total_amount DECIMAL(10, 2) GENERATED ALWAYS AS (unit_amount * total_units) STORED,
     activation_method ENUM('首次消费生效', '具体日期', '最迟生效日期') NOT NULL,
     activation_date DATE DEFAULT NULL,
     latest_activation_date DATE DEFAULT NULL,
     validity_period INT NOT NULL,
     validity_unit ENUM('周', '月', '年') NOT NULL,
     refundable BOOLEAN DEFAULT TRUE,
     refund_fee DECIMAL(10, 2) DEFAULT 0,
     qr_code VARCHAR(255) DEFAULT NULL,
     location_info VARCHAR(255) DEFAULT NULL,
     created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
     status ENUM('待签订', '执行中', '已完成', '已取消') DEFAULT '待签订',
     FOREIGN KEY (user_id) REFERENCES users(user_id),
     FOREIGN KEY (merchant_id) REFERENCES merchants(merchant_id),
     FOREIGN KEY (template_id) REFERENCES contract_templates(template_id)
);