package com.example.myfirstcomposeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myfirstcomposeapp.data.BillRepository
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
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyFirstComposeAppTheme { AccountingApp() }
        }
    }
}

/**
 * 应用入口：只持有导航控制器。
 *
 * 账单数据从 BillRepository 的 StateFlow 读，不再放在这里的 remember 里；
 * 记账页保存成功后 StateFlow 一变，列表页和统计页就会自动重组，不需要额外同步代码。
 */
@Composable
fun AccountingApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val bills by BillRepository.bills.collectAsStateWithLifecycle()

    // Snackbar 挂在最外层 Scaffold 上：记账页保存后立刻跳回列表，提示也还显示得出来。
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
                    onLoadSample = { count -> BillRepository.loadSample(count) }
                )
            }
            // 记账页的 ViewModel 由 NavHost 提供（作用域是这个页面的返回栈条目），
            // 保存的活儿交给它，这里只负责"跳回列表 + 弹提示"。
            composable(AddDestination.route) {
                AddScreen(
                    onSaved = {
                        navController.navigate(HomeDestination.route) {
                            popUpTo(HomeDestination.route) { inclusive = false }
                            launchSingleTop = true
                            restoreState = true
                        }
                        scope.launch {
                            snackbarHostState.showSnackbar("保存成功，新记录已排在最前面")
                        }
                    }
                )
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
