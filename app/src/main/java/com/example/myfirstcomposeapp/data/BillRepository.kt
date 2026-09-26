package com.example.myfirstcomposeapp.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * 内存版账单仓库 —— 实验 3 的临时数据层，替代实验 2 里放在 MainActivity 的状态。
 *
 * 对外只暴露只读的 StateFlow，写入必须走 add() / loadSample()，
 * 于是"谁改了数据"只有一处入口，列表页和统计页都会自动跟着变。
 * 实验 4 接入 Room 时只要把这里的实现换掉，ViewModel 和界面代码不用动。
 */
object BillRepository {

    private val _bills = MutableStateFlow(BillSampleData.bills(10))

    /** 当前账单列表，界面用 collectAsStateWithLifecycle() 订阅。 */
    val bills: StateFlow<List<Bill>> = _bills.asStateFlow()

    /** 新增一条记录并返回它（带新生成的 id）。新记录排在最前面。 */
    fun add(amount: Double, category: String, note: String, date: String): Bill {
        val record = Bill(
            id = (_bills.value.maxOfOrNull { it.id } ?: 0L) + 1,
            amount = amount,
            category = category,
            note = note,
            date = date
        )
        _bills.update { listOf(record) + it }
        return record
    }

    /** 测试数据切换：0 条用于验证空列表，10 / 100 条用于验证滚动。 */
    fun loadSample(count: Int) {
        _bills.value = if (count > 0) BillSampleData.bills(count) else emptyList()
    }
}
