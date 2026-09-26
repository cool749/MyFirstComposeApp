package com.example.myfirstcomposeapp.ui.add

import com.example.myfirstcomposeapp.data.BillCategories

/**
 * 记账页的界面状态（UDF 里的 State）。
 *
 * 页面只读这个对象，任何改动都必须由 AccountViewModel 的方法产生，
 * 于是"界面显示的内容"和"状态里的内容"永远一致，不会出现两处各存一份的情况。
 */
data class AddRecordUiState(
    /** 金额输入框的原始文本。用 String 而不是 Double：输入过程中会有 "12." 这种中间态。 */
    val amount: String = "",
    /** 选中的分类，默认选第一个。 */
    val category: String = BillCategories.first(),
    /** 备注，选填。 */
    val note: String = "",
    /** 校验失败时的提示，null 表示没有错误。 */
    val errorMessage: String? = null,
    /** 保存成功标记，界面提示过一次后由 onSavedSuccessShown() 复位。 */
    val isSavedSuccess: Boolean = false
)
