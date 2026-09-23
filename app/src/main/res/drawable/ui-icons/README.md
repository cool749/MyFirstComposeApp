# 记账 App 图标集

22 个 24dp 矢量图标。手写路径，没有位图、没有外部依赖、不需要网络。

预览图见 `preview.png`。

## 怎么用

把 `drawable/` 里的 `.xml` 复制到工程的：

```
app/src/main/res/drawable/
```

## Compose 里的用法

```kotlin
import androidx.compose.material3.Icon
import androidx.compose.ui.res.painterResource

Icon(
    painter = painterResource(R.drawable.ic_cat_food),
    contentDescription = "餐饮",
    tint = MaterialTheme.colorScheme.primary,
)
```

`Icon` 的 `tint` 会自动染色，所以同一个文件在不同位置可以是不同颜色，
不需要为每个颜色单独出一份图。

## 如果是 View 体系

```xml
<ImageView
    android:src="@drawable/ic_expense"
    android:tint="@color/expense_red" />
```

## 清单

**核心界面**

| 文件 | 用途 |
|---|---|
| `ic_receipt` | 账单列表 |
| `ic_add_circle` | 记一笔 |
| `ic_chart` | 统计 |
| `ic_wallet` | 钱包 / 账户 |
| `ic_income` | 收入（箭头向下入托盘） |
| `ic_expense` | 支出（箭头向上出托盘） |
| `ic_calendar` | 日期 |
| `ic_search` | 搜索 / 筛选 |
| `ic_delete` | 删除 |
| `ic_edit` | 编辑 |

**记账分类**

| 文件 | 用途 |
|---|---|
| `ic_cat_food` | 餐饮 |
| `ic_cat_transport` | 交通 |
| `ic_cat_shopping` | 购物 |
| `ic_cat_home` | 居住 |
| `ic_cat_entertainment` | 娱乐 |
| `ic_cat_medical` | 医疗 |
| `ic_cat_education` | 教育 |
| `ic_cat_other` | 其他 |

**导航与操作**

| 文件 | 用途 |
|---|---|
| `ic_chevron_left` | 上一月 |
| `ic_chevron_right` | 下一月 |
| `ic_check` | 确认 / 保存 |
| `ic_settings` | 设置 |

## 想改颜色

图标默认是纯黑（`#FF000000`），因为 Compose 的 `Icon(tint = ...)` 会覆盖它，
留着黑色最灵活。如果想改默认色，把文件里的 `android:strokeColor`
和 `android:fillColor` 换掉即可。

## 规格

- viewport 24×24，内容留 2 单位边距（符合 Material 的 24dp 网格习惯）
- 描边宽度 1.8，圆头（`strokeLineCap="round"`）圆角（`strokeLineJoin="round"`）
- `ic_cat_other` 是填充式（三个点），其余都是描边式

## 来源

全部路径为本项目手写，没有从图标库复制，可自由用于课程作业。
