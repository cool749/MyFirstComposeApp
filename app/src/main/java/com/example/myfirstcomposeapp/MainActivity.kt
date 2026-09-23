package com.example.myfirstcomposeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myfirstcomposeapp.data.Bill
import com.example.myfirstcomposeapp.data.BillSampleData
import com.example.myfirstcomposeapp.ui.add.AddScreen
import com.example.myfirstcomposeapp.ui.detail.BillDetailScreen
import com.example.myfirstcomposeapp.ui.home.HomeScreen
import com.example.myfirstcomposeapp.ui.navigation.AddDestination
import com.example.myfirstcomposeapp.ui.navigation.BillDetailDestination
import com.example.myfirstcomposeapp.ui.navigation.HomeDestination
import com.example.myfirstcomposeapp.ui.navigation.StatDestination
import com.example.myfirstcomposeapp.ui.navigation.topLevelDestinations
import com.example.myfirstcomposeapp.ui.stat.StatScreen
import com.example.myfirstcomposeapp.ui.theme.MyFirstComposeAppTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyFirstComposeAppTheme { AccountingApp() }
        }
    }
}

/**
 * 应用入口：持有导航控制器、持有账单数据，再把两者分发给各个页面。
 */
@Composable
fun AccountingApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    // 账单状态提升到这一层：底部导航来回切换、进详情再返回，列表数据都不会丢。
    val bills = remember {
        mutableStateListOf<Bill>().apply { addAll(BillSampleData.bills(10)) }
    }

    Scaffold(
        bottomBar = {
            // 详情页不是顶层页面，进详情后隐藏底栏，避免详情下面还挂着三个 Tab。
            val isTopLevel = topLevelDestinations.any { it.route == currentRoute }
            if (isTopLevel) {
                NavigationBar {
                    topLevelDestinations.forEach { destination ->
                        NavigationBarItem(
                            selected = currentRoute == destination.route,
                            onClick = {
                                navController.navigate(destination.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Text(destination.icon) },
                            label = { Text(destination.label) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = HomeDestination.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // 列表页不知道 NavController 的存在，它只把被点击的 id 交给上层。
            composable(HomeDestination.route) {
                HomeScreen(
                    bills = bills,
                    onOpenBill = { billId ->
                        navController.navigate(BillDetailDestination.createRoute(billId))
                    },
                    onLoadSample = { count ->
                        bills.clear()
                        if (count > 0) bills.addAll(BillSampleData.bills(count))
                    }
                )
            }
            composable(AddDestination.route) {
                AddScreen(onSave = { amount, category, note ->
                    bills.add(
                        0,
                        Bill(
                            id = (bills.maxOfOrNull { it.id } ?: 0L) + 1,
                            amount = amount,
                            category = category,
                            note = note.ifBlank { "手动添加" },
                            date = SimpleDateFormat("MM-dd", Locale.getDefault()).format(Date())
                        )
                    )
                    navController.navigate(HomeDestination.route) {
                        popUpTo(HomeDestination.route) { inclusive = false }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
            }
            composable(StatDestination.route) {
                StatScreen(bills = bills)
            }
            composable(
                route = BillDetailDestination.route,
                arguments = listOf(
                    navArgument(BillDetailDestination.ARG_BILL_ID) { type = NavType.LongType }
                )
            ) { entry ->
                val billId = entry.arguments?.getLong(BillDetailDestination.ARG_BILL_ID) ?: -1L
                BillDetailScreen(
                    billId = billId,
                    bill = bills.firstOrNull { it.id == billId },
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AccountingAppPreview() {
    MyFirstComposeAppTheme { AccountingApp() }
}
