package com.example.myfirstcomposeapp.ui.add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

/**
 * 记账页。输入内容用 rememberSaveable 保存，切到别的 Tab 再回来不会被清空。
 * 保存动作只把三个值交给 onSave，数据写到哪里由上层决定。
 */
@Composable
fun AddScreen(onSave: (amount: Double, category: String, note: String) -> Unit) {
    var amount by rememberSaveable { mutableStateOf("") }
    var category by rememberSaveable { mutableStateOf("") }
    var note by rememberSaveable { mutableStateOf("") }

    val amountValue = amount.toDoubleOrNull()
    val canSave = amountValue != null && amountValue > 0 && category.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "添加账单",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("金额") },
            placeholder = { Text("请输入金额") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )
        OutlinedTextField(
            value = category,
            onValueChange = { category = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("分类") },
            placeholder = { Text("例如：餐饮") },
            singleLine = true
        )
        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("备注") },
            placeholder = { Text("选填") },
            singleLine = true
        )
        Button(
            onClick = {
                onSave(amountValue ?: 0.0, category.trim(), note.trim())
                amount = ""
                category = ""
                note = ""
            },
            enabled = canSave,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("保存")
        }
        Text(
            text = "保存后自动回到账单列表，新记录排在最前面。",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
