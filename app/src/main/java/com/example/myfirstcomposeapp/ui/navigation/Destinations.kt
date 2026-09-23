package com.example.myfirstcomposeapp.ui.navigation

/**
 * 页面地址集中定义，避免在各处散落路由字符串。
 * 页面组件自己不拼路由，只通过回调把 id 交给上层，由上层调用这里的方法生成地址。
 */
object HomeDestination {
    const val route = "home"
}

object AddDestination {
    const val route = "add"
}

object StatDestination {
    const val route = "stat"
}

/** 带参数的详情页地址：bill/{billId}。 */
object BillDetailDestination {
    const val ARG_BILL_ID = "billId"
    const val route = "bill/{$ARG_BILL_ID}"

    fun createRoute(billId: Long) = "bill/$billId"
}

/** 底部导航栏上的顶层页面。 */
data class TopLevelDestination(val route: String, val label: String, val icon: String)

val topLevelDestinations = listOf(
    TopLevelDestination(HomeDestination.route, "账单", "⌂"),
    TopLevelDestination(AddDestination.route, "记账", "＋"),
    TopLevelDestination(StatDestination.route, "统计", "▥")
)
