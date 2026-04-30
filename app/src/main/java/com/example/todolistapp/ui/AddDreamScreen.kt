package com.example.todolistapp.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AttachFile
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.EventAvailable
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.todolistapp.LocalWindowSizeClass
import com.example.todolistapp.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDreamScreen(
    navController: NavHostController,
    accountId: Int
) {
    val windowSize = LocalWindowSizeClass.current
    val widthClass = windowSize.widthSizeClass
    val hPadding = if (widthClass == WindowWidthSizeClass.Compact) 20.dp else 45.dp

    // --- STATE ---
    var dreamTitle by remember { mutableStateOf("") }
    var dreamDesc by remember { mutableStateOf("") }
    var selectedDateTime by remember { mutableStateOf("Chọn ngày và giờ") }

    var showLinkDialog by remember { mutableStateOf(false) }
    var linkInput by remember { mutableStateOf("") }
    var currentLinkTarget by remember { mutableStateOf("") }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState()

    // --- LOGIC CHỌN NGÀY & GIỜ ---
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showDatePicker = false; showTimePicker = true }) {
                    Text("Tiếp theo", color = DeepPurple, fontWeight = FontWeight.Bold)
                }
            }
        ) { DatePicker(state = datePickerState) }
    }

    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val date = datePickerState.selectedDateMillis?.let {
                        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(it))
                    } ?: ""
                    // FIX LỖI Ở ĐÂY: Sửa lại cú pháp lấy phút
                    val minute = String.format("%02d", timePickerState.minute)
                    selectedDateTime = "$date - ${timePickerState.hour}:$minute"
                    showTimePicker = false
                }) { Text("Xong", color = DeepPurple, fontWeight = FontWeight.Bold) }
            },
            title = { Text("Chọn giờ hoàn thành", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
            text = { TimePicker(state = timePickerState) }
        )
    }

    // --- DIALOG DÁN LINK ---
    if (showLinkDialog) {
        AlertDialog(
            onDismissRequest = { showLinkDialog = false },
            title = { Text("Thêm $currentLinkTarget", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = linkInput,
                    onValueChange = { linkInput = it },
                    placeholder = { Text("Dán liên kết vào đây...") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            },
            confirmButton = {
                Button(onClick = { showLinkDialog = false }, colors = ButtonDefaults.buttonColors(DeepPurple)) {
                    Text("Lưu liên kết")
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize().background(PurpleBg)) {
        // 1. XỬ LÝ ẢNH MỜ DẦN (Scrim Gradient) - Xóa bỏ ranh giới "âm dương"
        Box(modifier = Modifier.fillMaxWidth().height(380.dp)) {
            Image(
                painter = painterResource(id = R.drawable.bg_h),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { alpha = 0.8f }
                    .drawWithContent {
                        drawContent()
                        drawRect(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color.Black, Color.Transparent),
                                startY = size.height * 0.4f, // Bắt đầu mờ từ giữa ảnh trở xuống
                                endY = size.height
                            ),
                            blendMode = BlendMode.DstIn
                        )
                    }
            )
        }

        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Column(modifier = Modifier.statusBarsPadding().padding(horizontal = hPadding, vertical = 20.dp)) {
                Text("Thêm ước mơ ✨", fontSize = 30.sp, fontWeight = FontWeight.Black, color = DeepPurple)
                Text("Hành động nhỏ hôm nay là bước ngoặt lớn mai sau.", fontSize = 14.sp, color = DeepPurple.copy(0.6f))
            }

            // Body (Vùng nhập liệu cuộn)
            Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = hPadding)) {

                ProfessionalInput(
                    label = "Tiêu đề ước mơ *",
                    value = dreamTitle,
                    onValueChange = { dreamTitle = it },
                    placeholder = "Bạn muốn đạt được điều gì...",
                    icon = Icons.Rounded.AutoAwesome
                )

                ProfessionalInput(
                    label = "Chi tiết dự định",
                    value = dreamDesc,
                    onValueChange = { dreamDesc = it },
                    placeholder = "Mô tả ngắn gọn kế hoạch...",
                    icon = Icons.Rounded.Description,
                    isLarge = true
                )

                ProfessionalAction(
                    label = "Hạn hoàn thành (Ngày & Giờ) *",
                    value = selectedDateTime,
                    icon = Icons.Rounded.EventAvailable,
                    onClick = { showDatePicker = true }
                )

                Spacer(modifier = Modifier.height(20.dp))
                Text("Tiện ích kèm theo", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DeepPurple)

                Row(modifier = Modifier.padding(top = 10.dp).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    SmallFeatureCard("Địa điểm", Icons.Rounded.LocationOn, Modifier.weight(1f)) {
                        currentLinkTarget = "Địa điểm (Google Map)"; showLinkDialog = true
                    }
                    SmallFeatureCard("Tài liệu", Icons.Rounded.AttachFile, Modifier.weight(1f)) {
                        currentLinkTarget = "Tài liệu / Link"; showLinkDialog = true
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))
            }

            // --- BOTTOM FIXED (Nút Lưu & Banner) ---
            Column(modifier = Modifier.background(PurpleBg)) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = hPadding, vertical = 10.dp)
                        .fillMaxWidth()
                        .height(60.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Brush.horizontalGradient(listOf(Color(0xFF8E7CFF), Color(0xFF6C5CE7))))
                        .clickable { /* Logic lưu toàn bộ */ },
                    contentAlignment = Alignment.Center
                ) {
                    Text("Lưu ước mơ", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                }

                // Banner coppy style từ Calendar
                DreamQuoteBannerFixed(widthClass, hPadding)
                SquaredBottomNav(
                    navController = navController,
                    accountId = accountId
                )            }
        }
    }
}

// --- COMPONENTS TỐI ƯU ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfessionalInput(label: String, value: String, onValueChange: (String) -> Unit, placeholder: String, icon: androidx.compose.ui.graphics.vector.ImageVector, isLarge: Boolean = false) {
    Column(modifier = Modifier.padding(vertical = 10.dp)) {
        Text(label, fontSize = 12.sp, color = DeepPurple.copy(0.7f), fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 8.dp, bottom = 6.dp))
        Surface(shape = RoundedCornerShape(24.dp), color = Color.White.copy(alpha = 0.9f), shadowElevation = 2.dp, border = BorderStroke(1.dp, Color.White)) {
            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
                Box(modifier = Modifier.size(44.dp).background(PurpleBg, RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                    Icon(icon, null, tint = DeepPurple, modifier = Modifier.size(22.dp))
                }
                TextField(
                    value = value, onValueChange = onValueChange,
                    placeholder = { Text(placeholder, color = Color.LightGray, fontSize = 15.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent),
                    textStyle = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Medium),
                    maxLines = if(isLarge) 5 else 1
                )
            }
        }
    }
}

@Composable
fun ProfessionalAction(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Column(modifier = Modifier.padding(vertical = 10.dp).clickable { onClick() }) {
        Text(label, fontSize = 12.sp, color = DeepPurple.copy(0.7f), fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 8.dp, bottom = 6.dp))
        Surface(shape = RoundedCornerShape(24.dp), color = Color.White.copy(alpha = 0.9f), shadowElevation = 2.dp, border = BorderStroke(1.dp, Color.White)) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, null, tint = DeepPurple, modifier = Modifier.size(20.dp))
                Text(value, modifier = Modifier.padding(start = 12.dp).weight(1f), fontSize = 15.sp, color = DeepPurple, fontWeight = FontWeight.Medium)
                Icon(Icons.Rounded.ChevronRight, null, tint = Color.LightGray)
            }
        }
    }
}

@Composable
fun SmallFeatureCard(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier, onClick: () -> Unit) {
    Surface(modifier = modifier.clickable { onClick() }, shape = RoundedCornerShape(20.dp), color = Color.White.copy(alpha = 0.7f), border = BorderStroke(1.dp, Color.White)) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = DeepPurple, modifier = Modifier.size(18.dp))
            Text(text, modifier = Modifier.padding(start = 8.dp), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DeepPurple)
        }
    }
}

@Composable
fun DreamQuoteBannerFixed(widthClass: WindowWidthSizeClass, hPadding: androidx.compose.ui.unit.Dp) {
    val bannerHeight = if (widthClass == WindowWidthSizeClass.Compact) 85.dp else 110.dp
    Box(modifier = Modifier.fillMaxWidth().height(bannerHeight).padding(horizontal = hPadding, vertical = 6.dp).clip(RoundedCornerShape(22.dp))) {
        Image(painter = painterResource(id = R.drawable.banner), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
        Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Black.copy(0.4f), Color.Transparent))))
        Text("Ước mơ không chỉ để mơ, mà để biến nó thành thực ✨", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.align(Alignment.Center).padding(horizontal = 24.dp), textAlign = TextAlign.Center)
    }
}