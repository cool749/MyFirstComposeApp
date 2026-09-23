package com.example.myfirstcomposeapp.data

/**
 * 一条账单记录。列表页只用到其中几个字段，详情页展示全部字段，两边共用同一个模型。
 */
data class Bill(
    val id: Long,
    val amount: Double,
    val category: String,
    val note: String,
    val date: String
) {
    val amountText: String get() = "¥%.2f".format(amount)
}

/**
 * 内存示例数据源。实验阶段先不接数据库，用同一个函数生成 0 / 10 / 100 条数据，
 * 方便验证"空列表""正常滚动""长列表滚动"三种情况。
 */
object BillSampleData {

    private val categories = listOf("餐饮", "交通", "购物", "学习", "娱乐", "医疗")

    fun bills(count: Int): List<Bill> = List(count) { index ->
        val number = index + 1
        Bill(
            id = number.toLong(),
            amount = 8.5 + index * 3.7,
            category = categories[index % categories.size],
            note = "示例账单 $number",
            date = "09-%02d".format(index % 28 + 1)
        )
    }
}
