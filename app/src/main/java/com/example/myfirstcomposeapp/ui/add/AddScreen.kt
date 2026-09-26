package com.example.myfirstcomposeapp.ui.add

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myfirstcomposeapp.data.BillCategories

/**
 * 记账页（改造后的 UDF 写法）：
 *   State -> AccountViewModel.uiState，页面唯一的数据来源
 *   Event -> 输入和点击原样上报给 ViewModel，页面自己不改状态、不做校验
 *
 * onSaved 交给上层处理"跳回列表 + 弹提示"，页面本身依旧不碰 NavController。
 */
@Composable
fun AddScreen(
    onSaved: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AccountViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val errorMessage = uiState.errorMessage

    // 保存成功是一次性事件：通知 ViewModel 复位标记，再交给上层跳转 + 提示。
    LaunchedEffect(uiState.isSavedSuccess) {
        if (uiState.isSavedSuccess) {
            viewModel.onSavedSuccessShown()
            onSaved()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "添加账单",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "页面只负责显示状态、上报事件，金额校验和保存都在 AccountViewModel 里。",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        OutlinedTextField(
            value = uiState.amount,
            onValueChange = viewModel::onAmountChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("金额") },
            placeholder = { Text("例如 12.5") },
            singleLine = true,
            isError = errorMessage != null,
            supportingText = errorMessage?.let { message ->
                { Text(text = message, color = MaterialTheme.colorScheme.error) }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )

        CategorySelector(
            selected = uiState.category,
            onSelect = viewModel::onCategoryChange
        )

        OutlinedTextField(
            value = uiState.note,
            onValueChange = viewModel::onNoteChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("备注") },
            placeholder = { Text("选填") },
            singleLine = true
        )

        // 按钮保持可点：让 ViewModel 统一校验并给出错误提示，而不是在界面里提前禁用。
        Button(
            onClick = viewModel::saveRecord,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("保存")
        }

        Text(
            text = "保存成功后自动回到账单列表，新记录排在最前面。",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/** 分类选择器：横向可滚动的标签，选中哪一项完全由 State 里的 category 决定。 */
@Composable
private fun CategorySelector(
    selected: String,
    onSelect: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "分类",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BillCategories.forEach { category ->
                FilterChip(
                    selected = selected == category,
                    onClick = { onSelect(category) },
                    label = { Text(category) }
                )
            }
        }
    }
}
