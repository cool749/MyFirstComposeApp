package com.example.myfirstcomposeapp.ui.stat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myfirstcomposeapp.data.Bill

/**
 * 统计页。数据来自和列表页同一份状态，新增账单后这里的数字会立刻跟着变。
 */
@Composable
fun StatScreen(bills: List<Bill>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "月度统计",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "账单笔数：${bills.size}",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "支出合计：¥%.2f".format(bills.sumOf { it.amount }),
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "这里的数字和列表页读的是同一份状态，不需要额外的同步代码。",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
