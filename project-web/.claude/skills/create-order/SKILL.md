---
name: create-order
description: 为用户创建订单（调用 Dubbo MCP 工具）
tags: [order, dubbo, business]
version: 1.0.0
mcp-server: dubbo-mcp-server   # 可选，指定要使用的 MCP Server
---

# 创建订单 Skill

**触发条件**：
- 用户说“帮我下单”“创建订单”“给用户xxx下单”等关键词

**执行流程**（严格按顺序）：
1. 先调用 `get_user_info` 工具确认用户是否存在（必须传 userId）。
2. 如果用户存在，再调用 `create_order` 工具创建订单。
3. 最后把订单号、金额、状态返回给用户。

**必传参数说明**：
- userId：用户ID（数字）
- productIds：商品ID列表，用逗号分隔（如 "1001,1002"）
- totalAmount：订单总金额（数字）

**示例调用**：
用户说：帮用户 123 下单买商品 1001 和 1002，总价 299 元

**预期输出格式**：
订单创建成功！订单号：ORD-20260402-001
用户ID：123
总金额：299.00 元