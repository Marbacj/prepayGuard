-- 登录 MySQL 服务器
mysql -hlocalhost -root -Mabohv123

-- 创建数据库 imooc_coupon_data
CREATE DATABASE IF NOT EXISTS mapoh_ppg_data;

-- 登录 MySQL 服务器, 并进入到 imooc_coupon_data 数据库中
mysql -hlocalhost -root -Mabohv123 -Dmapoh_ppg_data