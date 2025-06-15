-- 插入用户数据
INSERT INTO `users` (`role`, `name`, `email`, `created_at`, `updated_at`, `encrypted_password`) VALUES
                                                                                                    ('USER', '张三', 'zhangsan@example.com', NOW(), NOW(), '$2a$10$ttV0jIAP97.qljkbbqMkauC4RC.cwU3cW0ul5sbrgbHc5an.c8x1C'),
                                                                                                    ('USER', '李四', 'lisi@example.com', NOW(), NOW(), '$2a$10$ttV0jIAP97.qljkbbqMkauC4RC.cwU3cW0ul5sbrgbHc5an.c8x1C'),
                                                                                                    ('ADMIN', '王五', 'wangwu@example.com', NOW(), NOW(), '$2a$10$ttV0jIAP97.qljkbbqMkauC4RC.cwU3cW0ul5sbrgbHc5an.c8x1C');

-- 插入技术人员数据
INSERT INTO `technicians` (`role`, `name`, `phone`, `specialization`, `created_at`, `encrypted_password`) VALUES
                                                                                                              ('TECH', '赵师傅', '13800138001', 'ELECTRICIAN', NOW(), '$2a$10$ttV0jIAP97.qljkbbqMkauC4RC.cwU3cW0ul5sbrgbHc5an.c8x1C'),
                                                                                                              ('TECH', '钱师傅', '13800138002', 'BODY_REPAIRER', NOW(), '$2a$10$ttV0jIAP97.qljkbbqMkauC4RC.cwU3cW0ul5sbrgbHc5an.c8x1C'),
                                                                                                              ('TECH', '孙师傅', '13800138003', 'PAINTER', NOW(), '$2a$10$ttV0jIAP97.qljkbbqMkauC4RC.cwU3cW0ul5sbrgbHc5an.c8x1C');

-- 插入车辆数据
INSERT INTO `vehicles` (`expired`, `user_id`, `model`, `created_at`, `license_plate`) VALUES
                                                                                          (FALSE, 1, '丰田卡罗拉', NOW(), '京A12345'),
                                                                                          (FALSE, 1, '本田雅阁', NOW(), '京B67890'),
                                                                                          (FALSE, 2, '大众高尔夫', NOW(), '沪C54321');

-- 插入预约数据
INSERT INTO `appointments` (`appointment_id`, `user_id`, `vehicle_id`, `technician_id`, `status`, `created_at`, `updated_at`, `total_hours`, `total_cost`) VALUES
                                                                                                                                                               ('APP20230001', 1, 1, 1, 'FINISHED', NOW(), NOW(), 2, 500),
                                                                                                                                                               ('APP20230002', 1, 2, 2, 'ONGOING', NOW(), NOW(), 1, 300),
                                                                                                                                                               ('APP20230003', 2, 3, 3, 'UNACCEPTED', NOW(), NOW(), NULL, NULL);

-- 插入维修项目数据
INSERT INTO `repair_items` (`repair_appointment_id`, `description`, `cost`, `created_at`) VALUES
                                                                                              (1, '更换刹车片', 300, NOW()),
                                                                                              (1, '更换机油', 200, NOW()),
                                                                                              (2, '车身喷漆', 300, NOW());

-- 插入备件数据
INSERT INTO `spare_parts` (`name`, `price`, `quantity`, `created_at`) VALUES
                                                                          ('刹车片', 150, 20, NOW()),
                                                                          ('机油', 100, 50, NOW()),
                                                                          ('车漆', 200, 30, NOW());

-- 插入预约备件关联数据
INSERT INTO `order_parts` (`appointment_id`, `spare_part_id`, `quantity`, `unit_price`) VALUES
                                                                                            (1, 1, 2, 150),
                                                                                            (1, 2, 1, 100),
                                                                                            (2, 3, 1, 200);

-- 插入反馈数据
INSERT INTO `feedbacks` (`user_id`, `appointment_id`, `rating`, `comment`, `created_at`) VALUES
                                                                                             (1, 1, 5, '服务很好，师傅很专业', NOW());

-- 插入薪资记录数据
INSERT INTO `salary_record` (`technician_id`, `amount`, `created_at`) VALUES
                                                                          (1, 5000, NOW()),
                                                                          (2, 4500, NOW()),
                                                                          (3, 4800, NOW());
-- 插入通知数据
INSERT INTO `notification` (`appointment_id`, `user`, `technician`, `content`, `is_read`) VALUES
                                                                                              (1, 1, 1, '您的预约已完成', TRUE),
                                                                                              (2, 1, 2, '您的预约正在进行中', FALSE);
