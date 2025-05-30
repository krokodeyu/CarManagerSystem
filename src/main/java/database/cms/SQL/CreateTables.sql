CREATE TABLE `appointments` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `appointment_id` varchar(255) DEFAULT NULL,
    `user_id` bigint NOT NULL,
    `vehicle_id` bigint NOT NULL,
    `technician_id` bigint DEFAULT NULL,
    `appointment_time` datetime NOT NULL,
    `status` enum('UNACCEPTED','ONGOING','CANCELLED','FINISHED') NOT NULL DEFAULT 'UNACCEPTED',
    `created_at` datetime NOT NULL,
    `updated_at` datetime NOT NULL,
    `total_cost` double NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_appointment_user` (`user_id`),
    KEY `fk_appointment_vehicle` (`vehicle_id`),
    KEY `fk_appointment_technician` (`technician_id`),
    CONSTRAINT `fk_appointment_technician` FOREIGN KEY (`technician_id`) REFERENCES `technicians` (`id`),
    CONSTRAINT `fk_appointment_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `fk_appointment_vehicle` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `feedbacks` (
     `id` bigint NOT NULL AUTO_INCREMENT,
     `user_id` bigint NOT NULL,
     `repair_order_id` bigint NOT NULL,
     `rating` int NOT NULL,
     `comment` text,
     `created_at` datetime NOT NULL,
     PRIMARY KEY (`id`),
     KEY `fk_feedback_user` (`user_id`),
     KEY `fk_feedback_repair_order` (`repair_order_id`),
     CONSTRAINT `fk_feedback_repair_order` FOREIGN KEY (`repair_order_id`) REFERENCES `repair_orders` (`id`),
     CONSTRAINT `fk_feedback_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `order_parts` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `repair_order_id` bigint NOT NULL,
    `spare_part_id` bigint NOT NULL,
    `quantity` int NOT NULL,
    `unit_price` decimal(10,2) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_order_part_repair_order` (`repair_order_id`),
    KEY `fk_order_part_spare_part` (`spare_part_id`),
    CONSTRAINT `fk_order_part_repair_order` FOREIGN KEY (`repair_order_id`) REFERENCES `repair_orders` (`id`),
    CONSTRAINT `fk_order_part_spare_part` FOREIGN KEY (`spare_part_id`) REFERENCES `spare_parts` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `repair_items` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `repair_order_id` bigint NOT NULL,
    `description` varchar(255) NOT NULL,
    `cost` decimal(10,2) NOT NULL,
    `created_at` datetime NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_repair_item_repair_order` (`repair_order_id`),
    CONSTRAINT `fk_repair_item_repair_order` FOREIGN KEY (`repair_order_id`) REFERENCES `repair_orders` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `repair_orders` (
     `id` bigint NOT NULL AUTO_INCREMENT,
     `user_id` bigint NOT NULL,
     `vehicle_id` bigint NOT NULL,
     `technician_id` bigint DEFAULT NULL,
     `status` enum('UNACCEPTED','ONGOING','FINISHED','CANCELLED') NOT NULL,
     `total_cost` decimal(10,2) NOT NULL,
     `created_at` datetime NOT NULL,
     `updated_at` datetime NOT NULL,
     PRIMARY KEY (`id`),
     KEY `fk_repair_order_user` (`user_id`),
     KEY `fk_repair_order_vehicle` (`vehicle_id`),
     KEY `fk_repair_order_technician` (`technician_id`),
     CONSTRAINT `fk_repair_order_technician` FOREIGN KEY (`technician_id`) REFERENCES `technicians` (`id`),
     CONSTRAINT `fk_repair_order_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
     CONSTRAINT `fk_repair_order_vehicle` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `spare_parts` (
   `id` bigint NOT NULL AUTO_INCREMENT,
   `name` varchar(255) NOT NULL,
   `description` text,
   `price` decimal(10,2) NOT NULL,
   `quantity` int NOT NULL,
   `created_at` datetime NOT NULL,
   PRIMARY KEY (`id`),
   UNIQUE KEY `uk_spare_part_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `technicians` (
   `id` bigint NOT NULL AUTO_INCREMENT,
   `role` enum('TECH','ADMIN','USER') DEFAULT 'TECH',
   `name` varchar(255) NOT NULL,
   `phone` varchar(20) NOT NULL,
   `specialization` varchar(255) NOT NULL,
   `created_at` datetime NOT NULL,
   `encrypted_password` varchar(255) NOT NULL,
   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `users` (
     `id` bigint NOT NULL AUTO_INCREMENT,
     `role` enum('USER','ADMIN','TECH') DEFAULT NULL,
     `name` varchar(255) NOT NULL,
     `email` varchar(255) NOT NULL,
     `created_at` datetime NOT NULL,
     `updated_at` datetime NOT NULL,
     `encrypted_password` varchar(255) NOT NULL,
     PRIMARY KEY (`id`),
     UNIQUE KEY `uk_user_name` (`name`),
     UNIQUE KEY `uk_user_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `vehicles` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `user_id` bigint NOT NULL,
    `make` varchar(50) NOT NULL,
    `model` varchar(50) NOT NULL,
    `license_plate` varchar(20) NOT NULL,
    `year` int NOT NULL,
    `created_at` datetime NOT NULL,
    `updated_at` datetime NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_vehicle_license_plate` (`license_plate`),
    KEY `fk_vehicle_user` (`user_id`),
    CONSTRAINT `fk_vehicle_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;