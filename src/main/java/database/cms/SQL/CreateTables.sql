-- 用户表
CREATE TABLE `users` (
                         `id` bigint NOT NULL AUTO_INCREMENT,
                         `role` enum('USER','TECH','ADMIN') DEFAULT 'USER',
                         `name` varchar(255) NOT NULL,
                         `email` varchar(255) NOT NULL,
                         `created_at` datetime NOT NULL,
                         `updated_at` datetime NOT NULL,
                         `encrypted_password` varchar(255) NOT NULL,
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `UK_name` (`name`),
                         UNIQUE KEY `UK_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 技术人员表
CREATE TABLE `technicians` (
                               `id` bigint NOT NULL AUTO_INCREMENT,
                               `role` enum('USER','TECH','ADMIN') NOT NULL DEFAULT 'TECH',
                               `name` varchar(255) NOT NULL,
                               `phone` varchar(255) NOT NULL,
                               `specialization` enum('ELECTRICIAN','BODY_REPAIRER','PAINTER','TIRE_TECH','APPRENTICE') NOT NULL,
                               `created_at` datetime NOT NULL,
                               `encrypted_password` varchar(255) NOT NULL,
                               PRIMARY KEY (`id`),
                               UNIQUE KEY `UK_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 车辆表
CREATE TABLE `vehicles` (
                            `id` bigint NOT NULL AUTO_INCREMENT,
                            `expired` tinyint(1) NOT NULL DEFAULT '0',
                            `user_id` bigint NOT NULL,
                            `model` varchar(255) NOT NULL,
                            `created_at` datetime NOT NULL,
                            `license_plate` varchar(255) NOT NULL,
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `UK_license_plate` (`license_plate`),
                            KEY `FK_user_id` (`user_id`),
                            CONSTRAINT `FK_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 预约表
CREATE TABLE `appointments` (
                                `id` bigint NOT NULL AUTO_INCREMENT,
                                `appointment_id` varchar(255) DEFAULT NULL,
                                `user_id` bigint NOT NULL,
                                `vehicle_id` bigint NOT NULL,
                                `technician_id` bigint DEFAULT NULL,
                                `status` enum('UNACCEPTED','ONGOING','CANCELLED','FINISHED') NOT NULL DEFAULT 'UNACCEPTED',
                                `created_at` datetime NOT NULL,
                                `updated_at` datetime NOT NULL,
                                `total_hours` int DEFAULT NULL,
                                `total_cost` double DEFAULT NULL,
                                PRIMARY KEY (`id`),
                                KEY `FK_user_id_appointment` (`user_id`),
                                KEY `FK_vehicle_id` (`vehicle_id`),
                                KEY `FK_technician_id` (`technician_id`),
                                CONSTRAINT `FK_user_id_appointment` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
                                CONSTRAINT `FK_vehicle_id` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicles` (`id`),
                                CONSTRAINT `FK_technician_id` FOREIGN KEY (`technician_id`) REFERENCES `technicians` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 维修项目表
CREATE TABLE `repair_items` (
                                `id` bigint NOT NULL AUTO_INCREMENT,
                                `repair_appointment_id` bigint NOT NULL,
                                `description` varchar(255) NOT NULL,
                                `cost` double NOT NULL,
                                `created_at` datetime NOT NULL,
                                PRIMARY KEY (`id`),
                                KEY `FK_repair_appointment_id` (`repair_appointment_id`),
                                CONSTRAINT `FK_repair_appointment_id` FOREIGN KEY (`repair_appointment_id`) REFERENCES `appointments` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 备件表
CREATE TABLE `spare_parts` (
                               `id` bigint NOT NULL AUTO_INCREMENT,
                               `name` varchar(255) NOT NULL,
                               `price` double NOT NULL,
                               `quantity` int NOT NULL,
                               `created_at` datetime NOT NULL,
                               PRIMARY KEY (`id`),
                               UNIQUE KEY `UK_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 预约备件关联表
CREATE TABLE `order_parts` (
                               `id` bigint NOT NULL AUTO_INCREMENT,
                               `appointment_id` bigint NOT NULL,
                               `spare_part_id` bigint NOT NULL,
                               `quantity` int NOT NULL,
                               `unit_price` double NOT NULL,
                               PRIMARY KEY (`id`),
                               KEY `FK_appointment_id` (`appointment_id`),
                               KEY `FK_spare_part_id` (`spare_part_id`),
                               CONSTRAINT `FK_appointment_id` FOREIGN KEY (`appointment_id`) REFERENCES `appointments` (`id`),
                               CONSTRAINT `FK_spare_part_id` FOREIGN KEY (`spare_part_id`) REFERENCES `spare_parts` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 反馈表
CREATE TABLE `feedbacks` (
                             `id` bigint NOT NULL AUTO_INCREMENT,
                             `user_id` bigint NOT NULL,
                             `appointment_id` bigint NOT NULL,
                             `rating` int NOT NULL,
                             `comment` varchar(255) DEFAULT NULL,
                             `created_at` datetime NOT NULL,
                             PRIMARY KEY (`id`),
                             KEY `FK_user_id_feedback` (`user_id`),
                             KEY `FK_appointment_id_feedback` (`appointment_id`),
                             CONSTRAINT `FK_appointment_id_feedback` FOREIGN KEY (`appointment_id`) REFERENCES `appointments` (`id`),
                             CONSTRAINT `FK_user_id_feedback` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
-- 薪资记录表
CREATE TABLE `salary_record` (
                                 `id` bigint NOT NULL AUTO_INCREMENT,
                                 `technician_id` bigint DEFAULT NULL,
                                 `amount` double DEFAULT NULL,
                                 `created_at` datetime DEFAULT NULL,
                                 PRIMARY KEY (`id`),
                                 KEY `FK_technician_id_salary` (`technician_id`),
                                 CONSTRAINT `FK_technician_id_salary` FOREIGN KEY (`technician_id`) REFERENCES `technicians` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 通知表
CREATE TABLE `notification` (
                                `id` bigint NOT NULL AUTO_INCREMENT,
                                `appointment_id` bigint DEFAULT NULL,
                                `user` bigint DEFAULT NULL,
                                `technician` bigint DEFAULT NULL,
                                `content` varchar(255) DEFAULT NULL,
                                `is_read` tinyint(1) DEFAULT NULL,
                                PRIMARY KEY (`id`),
                                KEY `FK_user_notification` (`user`),
                                KEY `FK_technician_notification` (`technician`),
                                CONSTRAINT `FK_technician_notification` FOREIGN KEY (`technician`) REFERENCES `technicians` (`id`),
                                CONSTRAINT `FK_user_notification` FOREIGN KEY (`user`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 订单表
CREATE TABLE `order` (
                         `id` bigint NOT NULL AUTO_INCREMENT,
                         `order_id` varchar(255) DEFAULT NULL,
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;