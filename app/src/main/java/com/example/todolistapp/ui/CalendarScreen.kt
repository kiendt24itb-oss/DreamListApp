package com.example.todolistapp.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.graphics.graphicsLayer
import com.example.todolistapp.LocalWindowSizeClass
import com.example.todolistapp.R
import com.example.todolistapp.model.Task
import com.example.todolistapp.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun CalendarScreen(
    navController: NavHostController,
    accountId: Int
) {
    val windowSize = LocalWindowSizeClass.current
    val widthClass = windowSize.widthSizeClass

    // --- CẤU HÌNH THÔNG SỐ ADAPTIVE ---
    val horizontalPadding = if (widthClass == WindowWidthSizeClass.Compact) 20.dp else 45.dp
    val headerFontSize = if (widthClass == WindowWidthSizeClass.Compact) 26.sp else 36.sp
    val sectionTitleSize = if (widthClass == WindowWidthSizeClass.Compact) 17.sp else 22.sp

    val taskViewModel: TaskViewModel = viewModel()
    val tasks by taskViewModel.tasks.collectAsState()
    val loading by taskViewModel.loading.collectAsState()

    val today = remember { Calendar.getInstance() }
    var currentMonth by remember { mutableStateOf(Calendar.getInstance()) }
    var selectedDate by remember { mutableStateOf(Calendar.getInstance()) }

    LaunchedEffect(accountId) {
        taskViewModel.loadTasks(accountId)
    }

    val selectedDateLabel = remember(selectedDate.timeInMillis) {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selectedDate.time)
    }

    val currentMonthTasks = tasks.filter { task -> taskInMonth(task, currentMonth) }
    val totalTasks = currentMonthTasks.size
    val doneTasks = currentMonthTasks.count {
        it.status.equals("completed", true) || it.status.contains("hoàn", true)
    }
    val progress = if (totalTasks == 0) 0f else doneTasks.toFloat() / totalTasks

    Box(modifier = Modifier.fillMaxSize().background(PurpleBg)) {
        Image(
            painter = painterResource(id = R.drawable.bg_h),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(if (widthClass == WindowWidthSizeClass.Compact) 0.4f else 0.5f)
        )
        Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.42f).background(
            Brush.verticalGradient(
                colors = listOf(Color.Transparent, PurpleBg.copy(alpha = 0.7f), PurpleBg),
                startY = 200f
            )
        ))

        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(start = horizontalPadding, end = horizontalPadding, top = 20.dp, bottom = 10.dp)
                    .statusBarsPadding()
            ) {
                Text("Lịch ước mơ ✨", fontSize = headerFontSize, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Column {
                Surface(
                    modifier = Modifier
                        .padding(horizontal = horizontalPadding)
                        .shadow(8.dp, RoundedCornerShape(24.dp)),
                    color = Color.White
                ) {
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                        MonthSelectorMini(
                            widthClass = widthClass,
                            currentMonth = currentMonth,
                            onPrevMonth = {
                                currentMonth = (currentMonth.clone() as Calendar).apply {
                                    add(Calendar.MONTH, -1)
                                    set(Calendar.DAY_OF_MONTH, 1)
                                }
                                selectedDate = (currentMonth.clone() as Calendar).apply { set(Calendar.DAY_OF_MONTH, 1) }
                            },
                            onNextMonth = {
                                currentMonth = (currentMonth.clone() as Calendar).apply {
                                    add(Calendar.MONTH, 1)
                                    set(Calendar.DAY_OF_MONTH, 1)
                                }
                                selectedDate = (currentMonth.clone() as Calendar).apply { set(Calendar.DAY_OF_MONTH, 1) }
                            },
                            onToday = {
                                currentMonth = today.clone() as Calendar
                                selectedDate = today.clone() as Calendar
                            }
                        )
                        WeekHeaderMini(widthClass)
                        CalendarGridMini(
                            widthClass = widthClass,
                            currentMonth = currentMonth,
                            selectedDate = selectedDate,
                            onSelectDay = { day ->
                                selectedDate = (currentMonth.clone() as Calendar).apply { set(Calendar.DAY_OF_MONTH, day) }
                            }
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = horizontalPadding + 8.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CompactStatCard(totalTasks.toString(), "Tổng CV tháng", Icons.Default.MenuBook, DeepPurple, widthClass)
                    Box(modifier = Modifier.width(1.dp).height(24.dp).background(Color.LightGray.copy(alpha = 0.5f)))
                    CompactProgressCard(progress, "Tiến độ tháng", Color(0xFF03DAC6), widthClass)
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    "Kế hoạch ngày $selectedDateLabel",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = sectionTitleSize,
                    color = DeepPurple,
                    modifier = Modifier.padding(horizontal = horizontalPadding, vertical = 8.dp)
                )

                if (loading) {
                    Box(modifier = Modifier.fillMaxWidth().padding(horizontal = horizontalPadding, vertical = 20.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = DeepPurple)
                    }
                } else {
                    val taskItems = tasks.filter { task ->
                        val taskDate = parseTaskDate(task.due_date)
                        taskDate != null && taskDate == selectedDateLabel
                    }

                    if (taskItems.isEmpty()) {
                        Box(modifier = Modifier.fillMaxWidth().padding(horizontal = horizontalPadding, vertical = 24.dp), contentAlignment = Alignment.Center) {
                            Text(
                                "Không có công việc trong ngày này.",
                                color = SoftPurple,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth(0.8f)
                            )
                        }
                    } else {
                        taskItems.forEach { task ->
                            FinalTaskItem(
                                task = task,
                                hPadding = horizontalPadding
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            Column(modifier = Modifier.background(PurpleBg)) {
                CalendarModernBanner(widthClass, horizontalPadding)
                SquaredBottomNav(
                    navController = navController,
                    accountId = accountId
                )
            }
        }
    }
}

// --- CÁC COMPONENT CON ĐÃ ĐƯỢC TỐI ƯU WINDOWSIZE ---

@Composable
fun CalendarModernBanner(widthClass: WindowWidthSizeClass, hPadding: androidx.compose.ui.unit.Dp) {
    val bannerHeight = when (widthClass) {
        WindowWidthSizeClass.Compact -> 85.dp
        WindowWidthSizeClass.Medium -> 105.dp
        else -> 125.dp
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(bannerHeight)
            .padding(horizontal = hPadding, vertical = 6.dp)
            .clip(RoundedCornerShape(24.dp))
    ) {
        Image(painter = painterResource(id = R.drawable.banner), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
        Box(modifier = Modifier.fillMaxSize().background(Brush.horizontalGradient(listOf(Color.Black.copy(alpha = 0.4f), Color.Transparent))))
        Box(modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp), contentAlignment = Alignment.CenterStart) {
            Column {
                Text(
                    "Lập kế hoạch thông minh",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = if (widthClass == WindowWidthSizeClass.Compact) 15.sp else 20.sp
                )
                Text(
                    "Sắp xếp thời gian, gặt hái thành công ✨",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = if (widthClass == WindowWidthSizeClass.Compact) 11.sp else 14.sp
                )
            }
        }
    }
}

@Composable
fun CompactStatCard(value: String, label: String, icon: ImageVector, color: Color, widthClass: WindowWidthSizeClass) {
    val iconSize = if (widthClass == WindowWidthSizeClass.Compact) 30.dp else 38.dp
    val valSize = if (widthClass == WindowWidthSizeClass.Compact) 20.sp else 26.sp

    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = color, modifier = Modifier.size(iconSize).graphicsLayer(alpha = 0.9f))
        Spacer(Modifier.width(12.dp))
        Column {
            Text(text = value, fontSize = valSize, fontWeight = FontWeight.Black, color = DeepPurple, lineHeight = valSize)
            Text(text = label, fontSize = 11.sp, color = Color.Gray.copy(alpha = 0.8f), fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun CompactProgressCard(percentage: Float, label: String, color: Color, widthClass: WindowWidthSizeClass) {
    val indicatorSize = if (widthClass == WindowWidthSizeClass.Compact) 30.dp else 38.dp
    val valSize = if (widthClass == WindowWidthSizeClass.Compact) 20.sp else 26.sp

    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(horizontalAlignment = Alignment.End) {
            Text(text = "${(percentage * 100).toInt()}%", fontSize = valSize, fontWeight = FontWeight.Black, color = color, lineHeight = valSize)
            Text(text = label, fontSize = 11.sp, color = Color.Gray.copy(alpha = 0.8f), fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.width(12.dp))
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(progress = 1f, modifier = Modifier.size(indicatorSize), color = color.copy(alpha = 0.15f), strokeWidth = 3.5.dp)
            CircularProgressIndicator(progress = percentage, modifier = Modifier.size(indicatorSize), color = color, strokeWidth = 3.5.dp, strokeCap = StrokeCap.Round)
        }
    }
}

@Composable
fun DailyTaskItemSync(title: String, time: String, status: String, statusColor: Color, icon: ImageVector, hPadding: androidx.compose.ui.unit.Dp) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = hPadding, vertical = 5.dp),
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        shadowElevation = 0.8.dp
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(42.dp).background(PurpleBg, CircleShape), contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = DeepPurple, modifier = Modifier.size(20.dp))
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(status, color = statusColor, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
                    Text(" • ", color = Color.LightGray)
                    Icon(Icons.Default.AccessTime, null, tint = SoftPurple, modifier = Modifier.size(13.dp))
                    Text(" $time", color = SoftPurple, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
            Icon(Icons.Default.ChevronRight, null, tint = Color.LightGray, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
fun MonthSelectorMini(
    widthClass: WindowWidthSizeClass,
    currentMonth: Calendar,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onToday: () -> Unit
) {
    val fontSize = if (widthClass == WindowWidthSizeClass.Compact) 16.sp else 20.sp
    val monthLabel = remember(currentMonth.timeInMillis) {
        "Tháng ${currentMonth.get(Calendar.MONTH) + 1}, ${currentMonth.get(Calendar.YEAR)}"
    }

    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
        Text(monthLabel, fontWeight = FontWeight.ExtraBold, fontSize = fontSize, color = DeepPurple)
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onPrevMonth, modifier = Modifier.size(28.dp)) {
                Icon(Icons.Default.ChevronLeft, null, tint = DeepPurple)
            }
            Text(
                "Hôm nay",
                color = DeepPurple,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 6.dp).clickable { onToday() }
            )
            IconButton(onClick = onNextMonth, modifier = Modifier.size(28.dp)) {
                Icon(Icons.Default.ChevronRight, null, tint = DeepPurple)
            }
        }
    }
}

@Composable
fun WeekHeaderMini(widthClass: WindowWidthSizeClass) {
    val days = listOf("T2", "T3", "T4", "T5", "T6", "T7", "CN")
    val fontSize = if (widthClass == WindowWidthSizeClass.Compact) 11.sp else 13.sp
    Row(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        days.forEach { Text(it, modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontSize = fontSize, color = Color.Gray, fontWeight = FontWeight.Medium) }
    }
}

private fun parseTaskDate(dueDate: String): String? {
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        parser.parse(dueDate)?.let { formatter.format(it) }
    } catch (e: Exception) {
        null
    }
}

private fun parseTaskTime(dueDate: String): String {
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        parser.parse(dueDate)?.let { formatter.format(it) } ?: ""
    } catch (e: Exception) {
        ""
    }
}

private fun taskInMonth(task: Task, month: Calendar): Boolean {
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        parser.parse(task.due_date)?.let {
            val taskCal = Calendar.getInstance().apply { time = it }
            taskCal.get(Calendar.YEAR) == month.get(Calendar.YEAR) &&
                taskCal.get(Calendar.MONTH) == month.get(Calendar.MONTH)
        } ?: false
    } catch (e: Exception) {
        false
    }
}

private fun statusColor(status: String): Color {
    return when {
        status.equals("completed", true) || status.contains("hoàn", true) -> GreenStatus
        status.equals("pending", true) || status.contains("chưa", true) -> OrangeStatus
        status.equals("in progress", true) || status.contains("đang", true) -> BlueStatus
        else -> SoftPurple
    }
}

private fun statusIcon(status: String): ImageVector {
    return when {
        status.equals("completed", true) || status.contains("hoàn", true) -> Icons.Default.CheckCircle
        status.equals("pending", true) || status.contains("chưa", true) -> Icons.Default.Schedule
        status.equals("in progress", true) || status.contains("đang", true) -> Icons.Default.PlayArrow
        else -> Icons.Default.List
    }
}

@Composable
fun CalendarGridMini(
    widthClass: WindowWidthSizeClass,
    currentMonth: Calendar,
    selectedDate: Calendar,
    onSelectDay: (Int) -> Unit
) {
    val dayCircleSize = if (widthClass == WindowWidthSizeClass.Expanded) 40.dp else 30.dp
    val fontSize = if (widthClass == WindowWidthSizeClass.Compact) 12.sp else 14.sp

    val firstDay = Calendar.getInstance().apply {
        time = currentMonth.time
        set(Calendar.DAY_OF_MONTH, 1)
    }
    val monthDays = currentMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
    val offset = (firstDay.get(Calendar.DAY_OF_WEEK) + 5) % 7

    Column {
        for (row in 0 until 6) {
            Row(Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
                for (col in 0 until 7) {
                    val cellIndex = row * 7 + col
                    val dayNumber = cellIndex - offset + 1
                    Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        if (dayNumber in 1..monthDays) {
                            val isSelected = selectedDate.get(Calendar.YEAR) == currentMonth.get(Calendar.YEAR) &&
                                selectedDate.get(Calendar.MONTH) == currentMonth.get(Calendar.MONTH) &&
                                selectedDate.get(Calendar.DAY_OF_MONTH) == dayNumber
                            Surface(
                                modifier = Modifier
                                    .size(dayCircleSize)
                                    .clickable { onSelectDay(dayNumber) },
                                shape = CircleShape,
                                color = if (isSelected) DeepPurple else Color.Transparent
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        "$dayNumber",
                                        color = if (isSelected) Color.White else Color.Black,
                                        fontSize = fontSize,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}