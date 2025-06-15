-- 用户表
CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       role VARCHAR(20) NOT NULL DEFAULT 'USER',
                       name VARCHAR(255) NOT NULL UNIQUE,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       created_at DATETIME NOT NULL,
                       updated_at DATETIME NOT NULL,
                       encrypted_password VARCHAR(255) NOT NULL
);

-- 反馈表
CREATE TABLE feedbacks (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           user_id BIGINT NOT NULL,
                           appointment_id BIGINT NOT NULL,
                           rating INT NOT NULL,
                           comment TEXT,
                           created_at DATETIME NOT NULL,
                           FOREIGN KEY (user_id) REFERENCES users(id),
                           FOREIGN KEY (appointment_id) REFERENCES appointments(id)
);

-- 维修项目表
CREATE TABLE repair_items (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              repair_appointment_id BIGINT NOT NULL,
                              description TEXT NOT NULL,
                              cost DOUBLE NOT NULL,
                              created_at DATETIME NOT NULL,
                              FOREIGN KEY (repair_appointment_id) REFERENCES appointments(id)
);

-- 工资记录表
CREATE TABLE salary_record (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               technician_id BIGINT,
                               amount DOUBLE NOT NULL,
                               created_at DATETIME NOT NULL,
                               FOREIGN KEY (technician_id) REFERENCES technicians(id)
);

-- 通知表
CREATE TABLE notification (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              appointment_id BIGINT,
                              user_id BIGINT,
                              technician_id BIGINT,
                              content TEXT NOT NULL,
                              FOREIGN KEY (user_id) REFERENCES users(id),
                              FOREIGN KEY (technician_id) REFERENCES technicians(id)
);

-- 提醒表
CREATE TABLE reminder (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          appointment_id BIGINT,
                          technician_id BIGINT,
                          created_at DATETIME NOT NULL,
                          is_read BOOLEAN NOT NULL DEFAULT FALSE,
                          FOREIGN KEY (appointment_id) REFERENCES appointments(id),
                          FOREIGN KEY (technician_id) REFERENCES technicians(id)
);

-- 备件表
CREATE TABLE spare_parts (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             name VARCHAR(255) NOT NULL UNIQUE,
                             price DOUBLE NOT NULL,
                             quantity INT NOT NULL,
                             created_at DATETIME NOT NULL
);

-- 技术人员表
CREATE TABLE technicians (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             role VARCHAR(20) NOT NULL DEFAULT 'TECH',
                             name VARCHAR(255) NOT NULL,
                             phone VARCHAR(20) NOT NULL UNIQUE,
                             specialization VARCHAR(255) NOT NULL,
                             created_at DATETIME NOT NULL,
                             encrypted_password VARCHAR(255) NOT NULL
);

-- 车辆表
CREATE TABLE vehicles (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          expired BOOLEAN NOT NULL DEFAULT FALSE,
                          user_id BIGINT NOT NULL,
                          model VARCHAR(255) NOT NULL,
                          created_at DATETIME NOT NULL,
                          license_plate VARCHAR(20) NOT NULL UNIQUE,
                          FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 预约表
CREATE TABLE appointments (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              appointment_id VARCHAR(255) NOT NULL,
                              user_id BIGINT NOT NULL,
                              vehicle_id BIGINT NOT NULL,
                              technician_id BIGINT,
                              status VARCHAR(20) NOT NULL DEFAULT 'UNACCEPTED',
                              created_at DATETIME NOT NULL,
                              updated_at DATETIME NOT NULL,
                              total_hours INT,
                              total_cost DOUBLE,
                              FOREIGN KEY (user_id) REFERENCES users(id),
                              FOREIGN KEY (vehicle_id) REFERENCES vehicles(id),
                              FOREIGN KEY (technician_id) REFERENCES technicians(id)
);

-- 预约备件表
CREATE TABLE appointment_parts (
                                   id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                   appointment_id BIGINT NOT NULL,
                                   spare_part_id BIGINT NOT NULL,
                                   quantity INT NOT NULL,
                                   unit_price DOUBLE NOT NULL,
                                   FOREIGN KEY (appointment_id) REFERENCES appointments(id),
                                   FOREIGN KEY (spare_part_id) REFERENCES spare_parts(id)
);