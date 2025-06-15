-- 1. 用户表数据（增加更多用户）
INSERT INTO users (id, role, name, email, created_at, updated_at, encrypted_password) VALUES
                                                                                          (1, 'USER', '张三', 'zhangsan@example.com', NOW(), NOW(), '$2a$10$ttV0jIAP97.qljkbbqMkauC4RC.cwU3cW0ul5sbrgbHc5an.c8x1C'),
                                                                                          (2, 'USER', '李四', 'lisi@example.com', NOW(), NOW(), '$2a$10$ttV0jIAP97.qljkbbqMkauC4RC.cwU3cW0ul5sbrgbHc5an.c8x1C'),
                                                                                          (3, 'USER', '王五', 'wangwu@example.com', NOW(), NOW(), '$2a$10$ttV0jIAP97.qljkbbqMkauC4RC.cwU3cW0ul5sbrgbHc5an.c8x1C'),
                                                                                          (4, 'USER', '赵六', 'zhaoliu@example.com', NOW(), NOW(), '$2a$10$ttV0jIAP97.qljkbbqMkauC4RC.cwU3cW0ul5sbrgbHc5an.c8x1C'),
                                                                                          (5, 'TECH', '钱七', 'qianqi@example.com', NOW(), NOW(), '$2a$10$ttV0jIAP97.qljkbbqMkauC4RC.cwU3cW0ul5sbrgbHc5an.c8x1C'),
                                                                                          (6, 'TECH', '孙八', 'sunba@example.com', NOW(), NOW(), '$2a$10$ttV0jIAP97.qljkbbqMkauC4RC.cwU3cW0ul5sbrgbHc5an.c8x1C'),
                                                                                          (7, 'ADMIN', '周九', 'zhoujiu@example.com', NOW(), NOW(), '$2a$10$ttV0jIAP97.qljkbbqMkauC4RC.cwU3cW0ul5sbrgbHc5an.c8x1C'),
                                                                                          (8, 'USER', '吴十', 'wushi@example.com', NOW(), NOW(), '$2a$10$ttV0jIAP97.qljkbbqMkauC4RC.cwU3cW0ul5sbrgbHc5an.c8x1C'),
                                                                                          (9, 'USER', '郑十一', 'zhengshiyi@example.com', NOW(), NOW(), '$2a$10$ttV0jIAP97.qljkbbqMkauC4RC.cwU3cW0ul5sbrgbHc5an.c8x1C'),
                                                                                          (10, 'TECH', '王十二', 'wangshier@example.com', NOW(), NOW(), '$2a$10$ttV0jIAP97.qljkbbqMkauC4RC.cwU3cW0ul5sbrgbHc5an.c8x1C');

-- 2. 技术人员表数据（从用户表中选取TECH角色用户）
INSERT INTO technicians (id, role, name, phone, specialization, created_at, encrypted_password) VALUES
                                                                                                    (5, 'TECH', '钱七', '13800138000', '发动机维修', NOW(), '$2a$10$ttV0jIAP97.qljkbbqMkauC4RC.cwU3cW0ul5sbrgbHc5an.c8x1C'),
                                                                                                    (6, 'TECH', '孙八', '13900139000', '车身修复', NOW(), '$2a$10$ttV0jIAP97.qljkbbqMkauC4RC.cwU3cW0ul5sbrgbHc5an.c8x1C'),
                                                                                                    (10, 'TECH', '王十二', '13700137000', '电气系统', NOW(), '$2a$10$ttV0jIAP97.qljkbbqMkauC4RC.cwU3cW0ul5sbrgbHc5an.c8x1C'),
                                                                                                    (11, 'TECH', '李十三', '13600136000', '底盘维修', NOW(), '$2a$10$ttV0jIAP97.qljkbbqMkauC4RC.cwU3cW0ul5sbrgbHc5an.c8x1C');

-- 3. 车辆表数据（关联用户）
INSERT INTO vehicles (id, expired, user_id, model, created_at, license_plate) VALUES
                                                                                  (1, FALSE, 1, '丰田卡罗拉', NOW(), '京A12345'),
                                                                                  (2, FALSE, 2, '本田雅阁', NOW(), '京B67890'),
                                                                                  (3, FALSE, 3, '大众帕萨特', NOW(), '京C11223'),
                                                                                  (4, FALSE, 4, '宝马3系', NOW(), '京D33445'),
                                                                                  (5, TRUE, 5, '奔驰C级', NOW(), '京E55667'),
                                                                                  (6, FALSE, 6, '奥迪A4L', NOW(), '京F77889'),
                                                                                  (7, FALSE, 7, '特斯拉Model3', NOW(), '京G99001'),
                                                                                  (8, FALSE, 8, '比亚迪汉', NOW(), '京H11223'),
                                                                                  (9, FALSE, 9, '吉利帝豪', NOW(), '京J33445'),
                                                                                  (10, FALSE, 10, '五菱宏光', NOW(), '京K55667');

-- 4. 预约表数据（关联用户和车辆）
INSERT INTO appointments (id, appointment_id, user_id, vehicle_id, technician_id, status, created_at, updated_at, total_hours, total_cost) VALUES
                                                                                                                                               (1, 'APT20230001', 1, 1, 5, 'FINISHED', NOW() - INTERVAL 30 DAY, NOW() - INTERVAL 29 DAY, 2, 500.0),
                                                                                                                                               (2, 'APT20230002', 2, 2, NULL, 'UNACCEPTED', NOW(), NOW(), NULL, NULL),
                                                                                                                                               (3, 'APT20230003', 3, 3, 6, 'ONGOING', NOW() - INTERVAL 15 DAY, NOW() - INTERVAL 14 DAY, 3, 750.0),
                                                                                                                                               (4, 'APT20230004', 4, 4, 10, 'CANCELLED', NOW() - INTERVAL 10 DAY, NOW() - INTERVAL 10 DAY, 1, 300.0),
                                                                                                                                               (5, 'APT20230005', 5, 5, 5, 'FINISHED', NOW() - INTERVAL 5 DAY, NOW() - INTERVAL 4 DAY, 4, 1000.0),
                                                                                                                                               (6, 'APT20230006', 6, 6, 6, 'ONGOING', NOW() - INTERVAL 2 DAY, NOW() - INTERVAL 1 DAY, 2, 600.0),
                                                                                                                                               (7, 'APT20230007', 7, 7, 10, 'UNACCEPTED', NOW(), NOW(), NULL, NULL),
                                                                                                                                               (8, 'APT20230008', 8, 8, 5, 'FINISHED', NOW() - INTERVAL 1 DAY, NOW(), 1, 400.0),
                                                                                                                                               (9, 'APT20230009', 9, 9, 6, 'ONGOING', NOW(), NOW(), 3, 900.0),
                                                                                                                                               (10, 'APT20230010', 10, 10, 10, 'UNACCEPTED', NOW(), NOW(), NULL, NULL);

-- 5. 备件表数据
INSERT INTO spare_parts (id, name, price, quantity, created_at) VALUES
                                                                    (1, '机油', 100.0, 100, NOW()),
                                                                    (2, '空气滤清器', 50.0, 50, NOW()),
                                                                    (3, '刹车片', 200.0, 30, NOW()),
                                                                    (4, '火花塞', 80.0, 40, NOW()),
                                                                    (5, '轮胎', 500.0, 20, NOW()),
                                                                    (6, '电瓶', 300.0, 15, NOW()),
                                                                    (7, '雨刮器', 30.0, 50, NOW()),
                                                                    (8, '变速箱油', 150.0, 25, NOW());

-- 6. 预约备件表数据（关联预约和备件）
INSERT INTO appointment_parts (id, appointment_id, spare_part_id, quantity, unit_price) VALUES
                                                                                            (1, 1, 1, 1, 100.0),
                                                                                            (2, 1, 2, 1, 50.0),
                                                                                            (3, 3, 1, 1, 100.0),
                                                                                            (4, 3, 3, 1, 200.0),
                                                                                            (5, 5, 1, 2, 100.0),
                                                                                            (6, 5, 4, 1, 80.0),
                                                                                            (7, 6, 5, 1, 500.0),
                                                                                            (8, 6, 6, 1, 300.0),
                                                                                            (9, 9, 2, 1, 50.0),
                                                                                            (10, 9, 7, 1, 30.0);

-- 7. 维修项目表数据（关联预约）
INSERT INTO repair_items (id, repair_appointment_id, description, cost, created_at) VALUES
                                                                                        (1, 1, '更换机油', 150.0, NOW() - INTERVAL 30 DAY),
                                                                                        (2, 1, '更换空气滤清器', 70.0, NOW() - INTERVAL 30 DAY),
                                                                                        (3, 3, '更换刹车片', 300.0, NOW() - INTERVAL 15 DAY),
                                                                                        (4, 3, '检查制动系统', 150.0, NOW() - INTERVAL 15 DAY),
                                                                                        (5, 5, '更换火花塞', 200.0, NOW() - INTERVAL 5 DAY),
                                                                                        (6, 5, '检查点火系统', 100.0, NOW() - INTERVAL 5 DAY),
                                                                                        (7, 6, '更换轮胎', 800.0, NOW() - INTERVAL 2 DAY),
                                                                                        (8, 6, '四轮定位', 200.0, NOW() - INTERVAL 2 DAY),
                                                                                        (9, 9, '更换雨刮器', 60.0, NOW()),
                                                                                        (10, 9, '检查玻璃水系统', 40.0, NOW());


-- 9. 反馈表数据（关联用户和预约）
INSERT INTO feedbacks (id, user_id, appointment_id, rating, comment, created_at) VALUES
                                                                                     (1, 1, 1, 5, '服务很好，师傅很专业', NOW() - INTERVAL 29 DAY),
                                                                                     (2, 3, 3, 2, '维修质量一般，而且等待时间较长', NOW() - INTERVAL 14 DAY),
                                                                                     (3, 5, 5, 1, '？？？？？', NOW() - INTERVAL 4 DAY),
                                                                                     (4, 6, 6, 3, '一般般，希望能更快完成', NOW() - INTERVAL 1 DAY),
                                                                                     (5, 8, 9, 4, '师傅态度很好，解释详细', NOW());

-- 10. 工资记录表数据（关联技术人员）
INSERT INTO salary_record (id, technician_id, amount, created_at) VALUES
                                                                      (1, 5, 5000.0, NOW() - INTERVAL 30 DAY),
                                                                      (2, 6, 4500.0, NOW() - INTERVAL 30 DAY),
                                                                      (3, 10, 5500.0, NOW() - INTERVAL 30 DAY),
                                                                      (4, 5, 5200.0, NOW() - INTERVAL 15 DAY),
                                                                      (5, 6, 4700.0, NOW() - INTERVAL 15 DAY),
                                                                      (6, 10, 5700.0, NOW() - INTERVAL 15 DAY),
                                                                      (7, 5, 5300.0, NOW() - INTERVAL 5 DAY),
                                                                      (8, 6, 4800.0, NOW() - INTERVAL 5 DAY),
                                                                      (9, 10, 5800.0, NOW() - INTERVAL 5 DAY);

-- 11. 通知表数据（关联用户和/或技术人员）
INSERT INTO notification (id, appointment_id, user_id, technician_id, content) VALUES
                                                                                   (1, 1, 1, 5, '您的预约已接受，钱七师傅将为您服务'),
                                                                                   (2, 2, 2, NULL, '您的预约正在等待技师确认'),
                                                                                   (3, 3, 3, 6, '您的预约已安排，孙八师傅将为您服务'),
                                                                                   (4, 4, 4, 5, '您的预约已取消'),
                                                                                   (5, 5, 5, 10, '您的预约已接受，王十二师傅将为您服务'),
                                                                                   (6, 6, 6, 6, '您的预约已确认，孙八师傅将为您服务'),
                                                                                   (7, 7, 7, NULL, '您的预约正在等待技师确认'),
                                                                                   (8, 8, 8, 5, '您的预约已接受，钱七师傅将为您服务'),
                                                                                   (9, 9, 9, 6, '您的预约已确认，孙八师傅将为您服务'),
                                                                                   (10, 10, 10, 10, '您的预约正在等待技师确认');

-- 12. 提醒表数据（关联预约和技术人员）
INSERT INTO reminder (id, appointment_id, technician_id, created_at, is_read) VALUES
                                                                                  (1, 1, 5, NOW() - INTERVAL 30 DAY, FALSE),
                                                                                  (2, 3, 6, NOW() - INTERVAL 15 DAY, FALSE),
                                                                                  (3, 5, 10, NOW() - INTERVAL 5 DAY, FALSE),
                                                                                  (4, 6, 6, NOW() - INTERVAL 2 DAY, FALSE),
                                                                                  (5, 9, 6, NOW(), FALSE);