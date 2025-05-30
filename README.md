# 车辆管理系统project

需要实现的api：

## 用户管理 `/users`

| 方法     | 路径                         | 描述                    | 权限         |
| ------ | -------------------------- | --------------------- | ---------- |
| `POST` | `/users/register`          | 用户注册                  | user       |
| `POST` | `/users/login`             | 用户登录（返回token/session） | user       |
| `GET`  | `/users/{id}`              | 获取用户详情                | user/admin |
| `GET`  | `/users/{id}/vehicles`     | 查询用户名下所有车辆            | user/admin |
| `GET`  | `/users/{id}/orders`       | 查询用户历史维修记录            | user/admin |
| `GET`  | `/users/{id}/appointments` | 查询用户预约记录              | user/admin |

---

## 车辆管理 `/vehicles`

| 方法       | 路径                      | 描述       | 权限         |
| -------- | ----------------------- | -------- | ---------- |
| `POST`   | `/vehicles`             | 添加新车辆    | user       |
| `GET`    | `/vehicles/{id}`        | 获取车辆详情   | all        |
| `PUT`    | `/vehicles/{id}`        | 更新车辆信息   | user       |
| `DELETE` | `/vehicles/{id}`        | 删除车辆     | user/admin |
| `GET`    | `/vehicles/{id}/orders` | 查询该车维修记录 | all        |

---

## 维修员管理 `/technicians`

| 方法     | 路径                         | 描述           | 权限         |
| ------ | -------------------------- | ------------ | ---------- |
| `POST` | `/technicians`             | 新增维修员        | admin      |
| `GET`  | `/technicians`             | 获取所有维修员      | admin      |
| `PUT`  | `/technicians/{id}`        | 更新维修员信息      | admin      |
| `GET`  | `/technicians/{id}`        | 获取维修员详情      | tech/admin |
| `GET`  | `/technicians/{id}/orders` | 查询维修员维修记录    | tech/admin |
| `GET`  | `/technicians/{id}/income` | 查询维修员工时与收入统计 | tech/admin |

---

## 维修订单 `/orders`

| 方法       | 路径                            | 描述                    | 权限         |
| -------- | ----------------------------- | --------------------- | ---------- |
| `POST`   | `/orders`                     | 创建维修订单（用户）（系统自动分配维修员） | user       |
| `GET`    | `/orders`                     | 查询所有订单（管理员）           | admin      |
| `GET`    | `/orders/{id}`                | 查询订单详情                | all        |
| `PUT`    | `/orders/{id}`                | 更新订单状态（用户取消）          | user/admin |
| `DELETE` | `/orders/{id}`                | 删除订单（管理员）             | admin      |
| `POST`   | `/orders/{id}/accept`         | 接受订单（维修员）             | tech       |
| `POST`   | `/orders/{id}/reject`         | 拒绝订单（维修员）             | tech       |
| `GET`    | `/orders/{id}/items`          | 查询订单维修项目              | all        |
| `POST`   | `/orders/{id}/items`          | 添加维修项目                | tech       |
| `DELETE` | `/orders/{id}/items/{itemId}` | 删去维修项目                | tech       |
| `GET`    | `/orders/{id}/parts`          | 查询订单所用配件              | all        |
| `POST`   | `/orders/{id}/parts`          | 添加订单配件                | tech       |
| `DELETE` | `orders/{id}/parts/{partId}`  | 删去订单配件                | tech       |

---

## 配件管理 `/parts`

| 方法       | 路径                 | 描述       | 权限    |
| -------- | ------------------ | -------- | ----- |
| `POST`   | `/parts`           | 添加配件（入库） | admin |
| `GET`    | `/parts`           | 查询配件列表   | admin |
| `GET`    | `/parts/{id}`      | 查看配件详情   | admin |
| `PUT`    | `/parts/{id}`      | 更新配件信息   | admin |
| `DELETE` | `/parts/{id}`      | 删除配件     | admin |
| `GET`    | `/parts/low-stock` | 查询库存不足配件 | admin |

---

## 预约管理 `/appointments`

| 方法       | 路径                           | 描述        | 权限    |
| -------- | ---------------------------- | --------- | ----- |
| `POST`   | `/appointments`              | 创建预约      | user  |
| `GET`    | `/appointments`              | 查询全部预约    | admin |
| `GET`    | `/appointments/{id}`         | 查看预约详情    | all   |
| `PUT`    | `/appointments/{id}`         | 修改预约信息    | user  |
| `DELETE` | `/appointments/{id}`         | 取消预约      | user  |
| `POST`   | `/appointments/{id}/confirm` | 确认预约并生成订单 | tech  |

---

## 评价反馈 `/feedbacks`

| 方法     | 路径                           | 描述        | 权限    |
| ------ | ---------------------------- | --------- | ----- |
| `POST` | `/feedbacks`                 | 提交评价（用户）  | user  |
| `GET`  | `/feedbacks`                 | 所有评价（管理员） | admin |
| `GET`  | `/feedbacks/order/{orderId}` | 查看某订单评价   | all   |
| `GET`  | `/feedbacks/negative`        | 查询差评记录    | all   |
| `GET`  | `feedbacks/reminder`         | 催单        | user  |

---

## 数据分析与统计 `/stats`

| 方法    | 路径                               | 描述            | 权限    |
| ----- | -------------------------------- | ------------- | ----- |
| `GET` | `/stats/vehicle-repairs`         | 按车型统计维修次数     | admin |
| `GET` | `/stats/average-repair-fees`     | 按车型同济平均维修费用   | admin |
| `GET` | `/stats/repair-frequencies`      | 所有车型的维修频率     | admin |
| `GET` | `/stats/most-frequent-failures`  | 特定车型最长出现的故障   | admin |
| `GET` | `/stats/fee-proportions`         | 按月份统计维修费用构成   | admin |
| `GET` | `/stats/negative-comment-orders` | 筛选负面反馈工单及涉及员工 | admin |
| `GET` | `/stats/costs`                   | 月/季度维修费用分析    | admin |
| `GET` | `/stats/technician-performance`  | 不同工种维修量统计     | admin |
| `GET` | `/stats/unresolved-orders`       | 当前未完成订单列表     | admin |

---

## 管理员控制`/admin`

| 方法     | 路径                   | 描述          | 权限    |
| ------ | -------------------- | ----------- | ----- |
| `GET`  | `/admin/users`       | 查询用户        | admin |
| `GET`  | `/admin/technicians` | 查询维修人员      | admin |
| `GET`  | `/admin/salaries`    | 查询所有工时费发放记录 | admin |
| `GET`  | `/admin/vehicles`    | 查询车辆        | admin |
| `GET`  | `/admin/orders`      | 查询特定维修工单信息  | admin |
| `GET`  | `/admin/records`     | 查询所有历史维修记录  | admin |
| `POST` | `/admin/salaries`    | 发放工时费       | admin |
