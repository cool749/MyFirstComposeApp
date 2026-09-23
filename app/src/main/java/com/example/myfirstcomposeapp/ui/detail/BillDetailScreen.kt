package com.example.myfirstcomposeapp.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myfirstcomposeapp.data.Bill

/**
 * 账单详情页。
 * 只接收一个 billId（导航参数），不持有 NavController，返回交给 onBack。
 */
@Composable
fun BillDetailScreen(
    billId: Long,
    bill: Bill?,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TextButton(onClick = onBack, contentPadding = PaddingValues(0.dp)) {
            Text("← 返回列表")
        }
        Text(
            text = "账单详情",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        // 把参数直接显示在页面上，方便确认导航带过来的 id 是否正确。
        Text(
            text = "导航参数 billId = $billId",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (bill == null) {
            Text(
                text = "没有找到 id = $billId 的账单",
                style = MaterialTheme.typography.bodyLarge
            )
        } else {
            HorizontalDivider()
            DetailRow("金额", bill.amountText)
            DetailRow("分类", bill.category)
            DetailRow("备注", bill.note)
            DetailRow("日期", bill.date)
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}
