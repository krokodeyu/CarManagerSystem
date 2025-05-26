## 车辆管理系统

需要实现的api：

## 用户管理 `/users`

| 方法       | 路径                         | 描述                    |
| -------- | -------------------------- | --------------------- |
| `POST`   | `/users/register`          | 用户注册                  |
| `POST`   | `/users/login`             | 用户登录（返回token/session） |
| `GET`    | `/users/{id}`              | 获取用户详情                |
| `PUT`    | `/users/{id}`              | 更新用户信息                |
| `DELETE` | `/users/{id}`              | 删除用户（管理员）             |
| `GET`    | `/users/{id}/vehicles`     | 查询用户名下所有车辆            |
| `GET`    | `/users/{id}/orders`       | 查询用户历史维修记录            |
| `GET`    | `/users/{id}/appointments` | 查询用户预约记录              |

---

## 车辆管理 `/vehicles`

| 方法       | 路径                      | 描述       |
| -------- | ----------------------- | -------- |
| `POST`   | `/vehicles`             | 添加新车辆    |
| `GET`    | `/vehicles/{id}`        | 获取车辆详情   |
| `PUT`    | `/vehicles/{id}`        | 更新车辆信息   |
| `DELETE` | `/vehicles/{id}`        | 删除车辆     |
| `GET`    | `/vehicles/{id}/orders` | 查询该车维修记录 |

---

## 维修员管理 `/technicians`

| 方法    | 路径                         | 描述           |
| ----- | -------------------------- | ------------ |
| `GET` | `/technicians`             | 获取所有维修员      |
| `GET` | `/technicians/{id}`        | 获取维修员详情      |
| `GET` | `/technicians/{id}/orders` | 查询维修员维修记录    |
| `GET` | `/technicians/{id}/income` | 查询维修员工时与收入统计 |
| `PUT` | `/technicians/{id}`        | 更新维修员信息（管理员） |

---

## 维修订单 `/orders`

| 方法       | 路径                    | 描述             |
| -------- | --------------------- | -------------- |
| `POST`   | `/orders`             | 创建维修订单（用户提交报修） |
| `GET`    | `/orders`             | 查询所有订单（管理员）    |
| `GET`    | `/orders/{id}`        | 查询订单详情         |
| `PUT`    | `/orders/{id}`        | 更新订单状态/分配维修员   |
| `DELETE` | `/orders/{id}`        | 删除订单           |
| `POST`   | `/orders/{id}/accept` | 维修员接受订单        |
| `POST`   | `/orders/{id}/reject` | 维修员拒绝订单        |
| `GET`    | `/orders/{id}/items`  | 查询订单维修项目       |
| `POST`   | `/orders/{id}/items`  | 添加维修项目         |
| `GET`    | `/orders/{id}/parts`  | 查询订单所用配件       |
| `POST`   | `/orders/{id}/parts`  | 添加订单配件         |

---

## 配件管理 `/parts`

| 方法       | 路径                 | 描述       |
| -------- | ------------------ | -------- |
| `POST`   | `/parts`           | 添加配件（入库） |
| `GET`    | `/parts`           | 查询配件列表   |
| `GET`    | `/parts/{id}`      | 查看配件详情   |
| `PUT`    | `/parts/{id}`      | 更新配件信息   |
| `DELETE` | `/parts/{id}`      | 删除配件     |
| `GET`    | `/parts/low-stock` | 查询库存不足配件 |

---

## 预约管理 `/appointments`

| 方法       | 路径                           | 描述          |
| -------- | ---------------------------- | ----------- |
| `POST`   | `/appointments`              | 创建预约        |
| `GET`    | `/appointments`              | 查询全部预约（管理员） |
| `GET`    | `/appointments/{id}`         | 查看预约详情      |
| `PUT`    | `/appointments/{id}`         | 修改预约信息      |
| `DELETE` | `/appointments/{id}`         | 取消预约        |
| `POST`   | `/appointments/{id}/confirm` | 确认预约并生成订单   |

---

## 评价反馈 `/feedbacks`

| 方法     | 路径                           | 描述        |
| ------ | ---------------------------- | --------- |
| `POST` | `/feedbacks`                 | 提交评价（用户）  |
| `GET`  | `/feedbacks`                 | 所有评价（管理员） |
| `GET`  | `/feedbacks/order/{orderId}` | 查看某订单评价   |
| `GET`  | `/feedbacks/negative`        | 查询差评记录    |

---

## 数据分析与统计 `/stats`

| 方法    | 路径                              | 描述         |
| ----- | ------------------------------- | ---------- |
| `GET` | `/stats/vehicle-repairs`        | 按车型统计维修次数  |
| `GET` | `/stats/costs`                  | 月/季度维修费用分析 |
| `GET` | `/stats/technician-performance` | 不同工种维修量统计  |
| `GET` | `/stats/unresolved-orders`      | 当前未完成订单列表  |
