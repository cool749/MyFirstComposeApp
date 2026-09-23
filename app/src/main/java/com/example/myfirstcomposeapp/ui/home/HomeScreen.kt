package com.example.myfirstcomposeapp.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.myfirstcomposeapp.data.Bill

/**
 * 账单列表页。
 * 这个组件不持有 NavController：点击卡片只把 id 交给 onOpenBill，往哪走由上层决定。
 */
@Composable
fun HomeScreen(
    bills: List<Bill>,
    onOpenBill: (Long) -> Unit,
    onLoadSample: (Int) -> Unit
) {
    var query by rememberSaveable { mutableStateOf("") }

    // 拓展任务：搜索框只做本地过滤，不查数据库、不发请求。
    val keyword = query.trim()
    val visibleBills = if (keyword.isEmpty()) {
        bills
    } else {
        bills.filter { bill ->
            bill.category.contains(keyword) || bill.note.contains(keyword)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.padding(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 8.dp)
        ) {
            Text(
                text = "账单列表",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = if (keyword.isEmpty()) {
                    "共 ${bills.size} 条 · 点卡片查看详情"
                } else {
                    "匹配 ${visibleBills.size} 条"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            label = { Text("搜索分类或备注") },
            singleLine = true
        )

        SampleDataBar(onLoadSample = onLoadSample)

        when {
            bills.isEmpty() -> EmptyContent(tip = "点击底部「记账」添加第一笔")
            visibleBills.isEmpty() -> EmptyContent(tip = "没有匹配「$keyword」的账单")
            else -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = visibleBills, key = { bill -> bill.id }) { bill ->
                    BillCard(bill = bill, onOpenBill = onOpenBill)
                }
            }
        }
    }
}

/** 空状态占位内容：列表为空和搜索无结果共用同一套排版，只是提示文字不同。 */
@Composable
private fun EmptyContent(tip: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "还没有账单",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = tip,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/** 单条账单卡片。点击时不跳转、不拿导航控制器，只上报自己的 id。 */
@Composable
private fun BillCard(bill: Bill, onOpenBill: (Long) -> Unit) {
    Card(
        onClick = { onOpenBill(bill.id) },
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = bill.category,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "${bill.date} · ${bill.note}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text(
                text = bill.amountText,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/** 演示用的数据量切换按钮，方便验收"空数据"和"100 条"两种情况。 */
@Composable
private fun SampleDataBar(onLoadSample: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "测试数据",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        TextButton(onClick = { onLoadSample(0) }) { Text("空") }
        TextButton(onClick = { onLoadSample(10) }) { Text("10 条") }
        TextButton(onClick = { onLoadSample(100) }) { Text("100 条") }
    }
}
