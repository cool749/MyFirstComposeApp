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

/** 分类选项：记账页的分类选择器和示例数据共用这一份，避免两处各写一遍。 */
val BillCategories = listOf("餐饮", "交通", "购物", "学习", "娱乐", "医疗", "其他")

/**
 * 内存示例数据源。实验阶段先不接数据库，用同一个函数生成 0 / 10 / 100 条数据，
 * 方便验证"空列表""正常滚动""长列表滚动"三种情况。
 */
object BillSampleData {

    fun bills(count: Int): List<Bill> = List(count) { index ->
        val number = index + 1
        Bill(
            id = number.toLong(),
            amount = 8.5 + index * 3.7,
            category = BillCategories[index % BillCategories.size],
            note = "示例账单 $number",
            date = "09-%02d".format(index % 28 + 1)
        )
    }
}
