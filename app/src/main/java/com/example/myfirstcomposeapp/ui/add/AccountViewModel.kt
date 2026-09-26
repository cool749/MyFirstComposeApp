package com.example.myfirstcomposeapp.ui.add

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.myfirstcomposeapp.data.BillRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * 记账页的 ViewModel：唯一持有界面状态的地方。
 *
 * 单向数据流（UDF）：
 *   页面事件 --> ViewModel 方法 --> 更新 _uiState --> 页面重组渲染新状态
 * 页面自己不改状态，校验规则也只写一遍，都在 saveRecord() 里。
 */
class AccountViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AddRecordUiState())

    /** 界面订阅的状态流（只读）。 */
    val uiState: StateFlow<AddRecordUiState> = _uiState.asStateFlow()

    /** 金额输入。用户重新输入时顺手清掉上一次的错误提示，避免错误一直挂在输入框下面。 */
    fun onAmountChange(amount: String) {
        _uiState.update { it.copy(amount = amount, errorMessage = null) }
    }

    fun onCategoryChange(category: String) {
        _uiState.update { it.copy(category = category, errorMessage = null) }
    }

    fun onNoteChange(note: String) {
        _uiState.update { it.copy(note = note) }
    }

    /**
     * 保存一条账单。
     * 校验不通过时只改 errorMessage，界面靠这个字段在金额输入框下面显示提示；
     * 校验通过才真正写数据，然后把表单清空并置上成功标记。
     */
    fun saveRecord() {
        val current = _uiState.value
        val amountText = current.amount.trim()
        val amountValue = amountText.toDoubleOrNull()

        if (amountText.isEmpty()) {
            showError("请输入金额")
            return
        }
        if (amountValue == null) {
            showError("金额格式不正确，请输入数字，例如 12.5")
            return
        }
        if (amountValue <= 0.0) {
            showError("金额必须大于 0")
            return
        }
        if (current.category.isBlank()) {
            showError("请选择分类")
            return
        }

        // 实验 3 先用内存仓库代替 Room，保存动作的写法和以后接数据库时一样。
        val record = BillRepository.add(
            amount = amountValue,
            category = current.category,
            note = current.note.trim().ifBlank { "手动添加" },
            date = SimpleDateFormat("MM-dd", Locale.getDefault()).format(Date())
        )
        Log.d(TAG, "saveRecord: 已保存 $record")

        // 回到初始状态并把成功标记置为 true：输入框清空 + 界面知道要弹提示。
        _uiState.value = AddRecordUiState(isSavedSuccess = true)
    }

    /** 界面提示过"保存成功"之后调用，把一次性标记复位。 */
    fun onSavedSuccessShown() {
        _uiState.update { it.copy(isSavedSuccess = false) }
    }

    private fun showError(message: String) {
        _uiState.update { it.copy(errorMessage = message, isSavedSuccess = false) }
    }

    private companion object {
        const val TAG = "AccountViewModel"
    }
}
