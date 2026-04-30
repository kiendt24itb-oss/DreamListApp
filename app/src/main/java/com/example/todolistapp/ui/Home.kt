package com.example.todolistapp.ui

// ===== THÊM IMPORT NÀY =====
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.UnfoldMore
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.todolistapp.LocalWindowSizeClass
import com.example.todolistapp.R
import com.example.todolistapp.model.Task
import com.example.todolistapp.viewmodel.TaskViewModel

@Composable
fun Home(
    navController: NavHostController,
    accountId: Int,
    userAvatarUri: Uri? = null
) {

    val windowSize = LocalWindowSizeClass.current
    val widthClass = windowSize.widthSizeClass

    val taskViewModel: TaskViewModel = viewModel()

    val tasks by taskViewModel.tasks.collectAsState()
    val loading by taskViewModel.loading.collectAsState()

    LaunchedEffect(Unit) {
        taskViewModel.loadTasks(accountId)
    }

    var selectedFilter by remember {
        mutableStateOf("Tất cả")
    }

    val filteredTasks = when (selectedFilter) {

        "Chưa làm" -> {
            tasks.filter {
                it.status == "pending"
            }
        }

        "Đang làm" -> {
            tasks.filter {
                it.status == "in_progress"
            }
        }

        "Hoàn thành" -> {
            tasks.filter {
                it.status == "completed"
            }
        }

        else -> tasks
    }

    val dateFormat = SimpleDateFormat(
        "yyyy-MM-dd HH:mm:ss",
        Locale.getDefault()
    )

    val upcomingTasks = tasks.filter { task ->

        try {

            val dueDate =
                dateFormat.parse(task.due_date)

            val currentDate = Date()

            val diffMillis =
                dueDate.time - currentDate.time

            val hours =
                TimeUnit.MILLISECONDS.toHours(diffMillis)

            hours in 0..24

        } catch (e: Exception) {

            false
        }
    }

    val hPadding =
        if (widthClass == WindowWidthSizeClass.Compact)
            24.dp
        else
            40.dp

    val headerFontSize =
        if (widthClass == WindowWidthSizeClass.Compact)
            26.sp
        else
            34.sp

    val upcomingHeight =
        if (widthClass == WindowWidthSizeClass.Expanded)
            180.dp
        else
            145.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PurpleBg)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(
                    if (widthClass == WindowWidthSizeClass.Compact)
                        0.55f
                    else
                        0.45f
                )
        ) {

            Image(
                painter = painterResource(id = R.drawable.bg_h),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.85f)
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                PurpleBg.copy(alpha = 0.5f),
                                PurpleBg
                            ),
                            startY = 600f
                        )
                    )
            )
        }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            HeaderSection(
                hPadding = hPadding,
                fontSize = headerFontSize,
                avatarUri = userAvatarUri
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(
                    horizontal = hPadding,
                    vertical = 8.dp
                )
            ) {

                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .background(
                            DeepPurple,
                            CircleShape
                        )
                        .border(
                            2.dp,
                            Color.White.copy(alpha = 0.5f),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.HourglassEmpty,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Spacer(
                    modifier = Modifier.width(12.dp)
                )

                Text(
                    text = "Sắp đến hạn",
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    fontSize =
                        if (widthClass == WindowWidthSizeClass.Compact)
                            20.sp
                        else
                            24.sp,
                    style = TextStyle(
                        shadow = Shadow(
                            Color.Black.copy(alpha = 0.4f),
                            blurRadius = 8f
                        )
                    )
                )
            }

            LazyRow(
                contentPadding = PaddingValues(
                    horizontal = hPadding,
                    vertical = 12.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.height(upcomingHeight)
            ) {

                items(upcomingTasks.take(5)) { task ->

                    MinimalUpcomingCard(
                        widthClass = widthClass,
                        task = task
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(
                        RoundedCornerShape(
                            topStart = 32.dp,
                            topEnd = 32.dp
                        )
                    )
                    .background(PurpleBg)
                    .padding(top = 12.dp)
            ) {

                SectionTitleWithSort(
                    title = "Công việc",
                    hPadding = hPadding,
                    selectedFilter = selectedFilter,
                    onFilterChange = {
                        selectedFilter = it
                    }
                )
            }

            if (loading) {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "Đang tải dữ liệu...",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }

            } else {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(
                        bottom = 100.dp
                    )
                ) {

                    items(filteredTasks) { task ->

                        FinalTaskItem(
                            task = task,
                            hPadding = hPadding
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier.align(
                Alignment.BottomCenter
            )
        ) {

            SquaredBottomNav(
                navController = navController,
                accountId = accountId
            )
        }
    }
}

@Composable
fun HeaderSection(
    hPadding: Dp,
    fontSize: TextUnit,
    avatarUri: Uri?
) {

    Row(
        modifier = Modifier
            .padding(
                horizontal = hPadding,
                vertical = 24.dp
            )
            .statusBarsPadding(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(
                    Color.White.copy(alpha = 0.2f)
                )
                .border(
                    2.dp,
                    Color.White,
                    CircleShape
                )
        ) {

            if (avatarUri != null) {

                AsyncImage(
                    model = avatarUri,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

            } else {

                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                )
            }
        }

        Spacer(
            modifier = Modifier.width(16.dp)
        )

        Column {

            Text(
                text = "Xin chào,",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp
            )

            Text(
                text = "Kiên ✨",
                fontSize = fontSize,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
        }
    }
}

@Composable
fun MinimalUpcomingCard(
    widthClass: WindowWidthSizeClass,
    task: Task
) {

    val cardWidth =
        if (widthClass == WindowWidthSizeClass.Expanded)
            220.dp
        else
            160.dp

    Box(
        modifier = Modifier
            .width(cardWidth)
            .fillMaxHeight()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Color.Black.copy(alpha = 0.42f)
            )
            .border(
                1.5.dp,
                Color.White.copy(alpha = 0.25f),
                RoundedCornerShape(24.dp)
            )
    ) {

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Surface(
                color = Color(0xFFE0B0FF)
                    .copy(alpha = 0.3f),
                shape = RoundedCornerShape(8.dp)
            ) {

                Text(
                    text = "SẮP TỚI",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    modifier = Modifier.padding(
                        horizontal = 8.dp,
                        vertical = 4.dp
                    )
                )
            }

            Column {

                Text(
                    text = task.title,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize =
                        if (widthClass == WindowWidthSizeClass.Expanded)
                            22.sp
                        else
                            18.sp,
                    color = Color.White,
                    maxLines = 2
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Default.Event,
                        contentDescription = null,
                        tint = Color(0xFFFFCC80),
                        modifier = Modifier.size(14.dp)
                    )

                    Spacer(
                        modifier = Modifier.width(6.dp)
                    )

                    Text(
                        text = task.due_date,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun FinalTaskItem(
    task: Task,
    hPadding: Dp
) {

    val status = when (task.status) {

        "completed" -> Triple(
            "Hoàn thành",
            Color(0xFF4CAF50),
            Icons.Default.CheckCircle
        )

        "in_progress" -> Triple(
            "Đang làm",
            Color(0xFF2196F3),
            Icons.Default.Sync
        )

        else -> Triple(
            "Chưa làm",
            Color(0xFFFF9800),
            Icons.Default.RadioButtonUnchecked
        )
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = hPadding,
                vertical = 6.dp
            ),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 1.dp
    ) {

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = status.third,
                contentDescription = null,
                tint = status.second,
                modifier = Modifier.size(26.dp)
            )

            Spacer(
                modifier = Modifier.width(16.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = task.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color(0xFF1A1A1A)
                )

                task.description?.let {

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = it,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Row(
                    modifier = Modifier.padding(top = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = status.first,
                        color = status.second,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Text(
                        text = "  •  ",
                        color = Color.LightGray
                    )

                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = Color(0xFF6200EE),
                        modifier = Modifier.size(14.dp)
                    )

                    Text(
                        text = task.due_date,
                        color = Color(0xFF6200EE),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun SectionTitleWithSort(
    title: String,
    hPadding: Dp,
    selectedFilter: String,
    onFilterChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val filters = listOf(
        "Tất cả",
        "Chưa làm",
        "Đang làm",
        "Hoàn thành"
    )

    val menuWidth = 110.dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = hPadding, vertical = 10.dp),

        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = title,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp,
            color = Color(0xFF6200EE)
        )

        Box {

            // Button filter
            Row(
                modifier = Modifier
                    .width(menuWidth)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFF6200EE),
                                Color(0xFFBB86FC)
                            )
                        )
                    )
                    .clickable { expanded = true }
                    .padding(horizontal = 12.dp, vertical = 8.dp),

                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = selectedFilter,
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )

                Icon(
                    imageVector = Icons.Default.UnfoldMore,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }

            // Dropdown Menu
            androidx.compose.material3.MaterialTheme(

                shapes = androidx.compose.material3.MaterialTheme.shapes.copy(
                    extraSmall = RoundedCornerShape(16.dp)
                )

            ) {

                DropdownMenu(
                    expanded = expanded,

                    onDismissRequest = {
                        expanded = false
                    },

                    offset = DpOffset(
                        x = 0.dp,
                        y = 4.dp
                    ),

                    modifier = Modifier
                        .width(menuWidth)
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color(0xFF6200EE),
                                    Color(0xFF8A2BE2)
                                )
                            )
                        )
                        .border(
                            1.dp,
                            Color.White.copy(alpha = 0.2f),
                            RoundedCornerShape(16.dp)
                        )
                ) {

                    filters.forEach { filter ->

                        val isSelected = filter == selectedFilter

                        DropdownMenuItem(

                            text = {

                                Text(
                                    text = filter,

                                    color =
                                        if (isSelected)
                                            Color.Yellow
                                        else
                                            Color.White,

                                    fontSize = 14.sp,

                                    fontWeight =
                                        if (isSelected)
                                            FontWeight.ExtraBold
                                        else
                                            FontWeight.Medium
                                )
                            },

                            onClick = {

                                onFilterChange(filter)
                                expanded = false
                            },

                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 4.dp,
                                    vertical = 2.dp
                                )
                                .clip(RoundedCornerShape(8.dp))
                                .background(

                                    if (isSelected)
                                        Color.White.copy(alpha = 0.15f)
                                    else
                                        Color.Transparent
                                )
                        )
                    }
                }
            }
        }
    }
}