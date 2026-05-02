package com.example.todolistapp.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.todolistapp.LocalWindowSizeClass
import com.example.todolistapp.R
import com.example.todolistapp.model.UserProfile
import com.example.todolistapp.utils.NotificationScheduler
import com.example.todolistapp.utils.SessionManager
import com.example.todolistapp.viewmodel.ProfileViewModel

@Composable
fun SettingsScreen(
    navController: NavHostController,
    accountId: Int
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val profileViewModel: ProfileViewModel = viewModel()
    val profile by profileViewModel.profile.collectAsState()
    val loading by profileViewModel.loading.collectAsState()
    val message by profileViewModel.message.collectAsState()

    val windowSize = LocalWindowSizeClass.current
    val widthClass = windowSize.widthSizeClass
    val horizontalPadding = if (widthClass == WindowWidthSizeClass.Compact) 20.dp else 45.dp

    val displayName = sessionManager.getName().orEmpty().ifBlank { "Dreamer" }
    val displayEmail = sessionManager.getEmail().orEmpty()

    var showChangePassDialog by remember { mutableStateOf(false) }
    var notificationEnabled by remember { mutableStateOf(true) }
    var notificationInfo by remember { mutableStateOf("Nhận thông báo nhắc nhở mỗi 24h.") }
    var oldPass by remember { mutableStateOf("") }
    var newPass by remember { mutableStateOf("") }
    var confirmPass by remember { mutableStateOf("") }
    var changePassMessage by remember { mutableStateOf<String?>(null) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            notificationEnabled = true
            notificationInfo = "Thông báo đã bật. Ứng dụng sẽ nhắc 24h/lần."
            NotificationScheduler.scheduleDailyReminder(context)
            profile?.let { profileViewModel.updateProfile(it.copy(notification_enabled = true)) }
        } else {
            notificationEnabled = false
            notificationInfo = "Quyền thông báo bị từ chối. Ứng dụng sẽ không thể gửi nhắc nhở."
        }
    }

    LaunchedEffect(accountId) {
        profileViewModel.getProfile(accountId)
    }

    LaunchedEffect(profile) {
        profile?.let {
            notificationEnabled = it.notification_enabled
            notificationInfo = if (it.notification_enabled) {
                "Thông báo bật. Ứng dụng sẽ nhắc 24h/lần."
            } else {
                "Thông báo tắt. Ứng dụng sẽ không gửi nhắc nhở."
            }
        }
    }

    fun updateNotificationState(enabled: Boolean) {
        if (enabled) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                return
            }
            notificationEnabled = true
            notificationInfo = "Thông báo đã bật. Ứng dụng sẽ nhắc 24h/lần."
            NotificationScheduler.scheduleDailyReminder(context)
            profile?.let { profileViewModel.updateProfile(it.copy(notification_enabled = true)) }
        } else {
            notificationEnabled = false
            notificationInfo = "Thông báo tắt. Ứng dụng sẽ không gửi nhắc nhở."
            NotificationScheduler.cancelDailyReminder(context)
            profile?.let { profileViewModel.updateProfile(it.copy(notification_enabled = false)) }
        }
    }

    fun onLogout() {
        sessionManager.logout()
        navController.navigate("Login") {
            popUpTo(0) { inclusive = true }
        }
    }

    if (showChangePassDialog) {
        AlertDialog(
            onDismissRequest = { showChangePassDialog = false },
            title = { Text("Đổi mật khẩu", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = oldPass,
                        onValueChange = { oldPass = it },
                        label = { Text("Mật khẩu cũ") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newPass,
                        onValueChange = { newPass = it },
                        label = { Text("Mật khẩu mới") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = confirmPass,
                        onValueChange = { confirmPass = it },
                        label = { Text("Xác nhận mật khẩu") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    changePassMessage?.let {
                        Text(text = it, color = Color.Red, fontSize = 13.sp)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        when {
                            oldPass.isBlank() || newPass.isBlank() || confirmPass.isBlank() -> {
                                changePassMessage = "Vui lòng điền đủ thông tin."
                            }
                            newPass != confirmPass -> {
                                changePassMessage = "Mật khẩu mới và xác nhận phải trùng nhau."
                            }
                            else -> {
                                changePassMessage = "Đổi mật khẩu thành công."
                                showChangePassDialog = false
                                oldPass = ""
                                newPass = ""
                                confirmPass = ""
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(DeepPurple),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cập nhật")
                }
            },
            dismissButton = {
                TextButton(onClick = { showChangePassDialog = false }) { Text("Hủy") }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize().background(PurpleBg)) {
        Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.45f)) {
            Image(
                painter = painterResource(id = R.drawable.bg_h),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(modifier = Modifier.fillMaxSize().background(
                Brush.verticalGradient(listOf(Color.Transparent, Color.Transparent, PurpleBg.copy(0.6f), PurpleBg))
            ))
        }

        Column(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.statusBarsPadding().padding(horizontal = horizontalPadding, vertical = 30.dp)) {
                Text(
                    "Cài đặt ✨",
                    style = TextStyle(
                        fontSize = 34.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        shadow = Shadow(color = Color.Black.copy(0.2f), blurRadius = 8f)
                    )
                )
                Text("Quản lý tài khoản và trải nghiệm", fontSize = 15.sp, color = Color.White, fontWeight = FontWeight.Bold)
            }

            Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
                SettingLabel("Tài khoản", horizontalPadding)
                UserCardStatic(widthClass, horizontalPadding, displayName, displayEmail, profile?.avatar_path)

                SettingItemCard(
                    title = "Thông tin cá nhân",
                    icon = Icons.Default.Person,
                    hPadding = horizontalPadding,
                    onClick = {
                        navController.navigate("ProfileScreen")
                    }
                )

                SettingItemCard(
                    title = "Đổi mật khẩu",
                    icon = Icons.Default.Lock,
                    hPadding = horizontalPadding,
                    onClick = { showChangePassDialog = true }
                )

                SettingItemCard(
                    title = "Thông báo",
                    icon = Icons.Default.Notifications,
                    showSwitch = true,
                    switchState = notificationEnabled,
                    onSwitchChange = { updateNotificationState(it) },
                    hPadding = horizontalPadding,
                    onClick = { updateNotificationState(!notificationEnabled) }
                )
                Text(
                    text = notificationInfo,
                    modifier = Modifier.padding(horizontal = horizontalPadding + 18.dp, vertical = 4.dp),
                    color = Color.Gray,
                    fontSize = 13.sp
                )

                if (loading) {
                    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = DeepPurple)
                    }
                }

                message?.let {
                    Text(
                        text = it,
                        modifier = Modifier.padding(horizontal = horizontalPadding + 18.dp, vertical = 6.dp),
                        color = Color.Red,
                        fontSize = 13.sp
                    )
                }

                SettingLabel("Khác", horizontalPadding)

                SettingItemCard("Điều khoản sử dụng", Icons.Default.Description, hPadding = horizontalPadding, onClick = { navController.navigate("TermsOfServiceScreen") })
                SettingItemCard("Giới thiệu ứng dụng", Icons.Default.Info, hPadding = horizontalPadding, onClick = { navController.navigate("AboutAppScreen") })

                SettingItemCard(
                    title = "Đăng xuất",
                    icon = Icons.Default.ExitToApp,
                    isRed = true,
                    hPadding = horizontalPadding,
                    onClick = { onLogout() }
                )

                Spacer(modifier = Modifier.height(20.dp))
            }

            Column(modifier = Modifier.background(PurpleBg)) {
                SettingsBanner(widthClass)
                SquaredBottomNav(
                    navController = navController,
                    accountId = accountId
                )
            }
        }
    }
}

// --- COMPONENTS CON ĐỂ KHÔNG BỊ LỖI ĐỎ ---

@Composable
fun SettingLabel(text: String, hPadding: androidx.compose.ui.unit.Dp) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = DeepPurple.copy(alpha = 0.8f),
        modifier = Modifier.padding(start = hPadding + 4.dp, top = 16.dp, bottom = 8.dp)
    )
}

@Composable
fun UserCardStatic(
    widthClass: WindowWidthSizeClass,
    hPadding: androidx.compose.ui.unit.Dp,
    displayName: String,
    displayEmail: String,
    avatarUrl: String?
) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = hPadding, vertical = 8.dp),
        shape = RoundedCornerShape(26.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            if (!avatarUrl.isNullOrBlank()) {
                AsyncImage(
                    model = avatarUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(58.dp).clip(CircleShape).background(PurpleBg)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier.size(58.dp).clip(CircleShape).background(PurpleBg)
                )
            }
            Column(modifier = Modifier.padding(start = 18.dp).weight(1f)) {
                Text(displayName, fontWeight = FontWeight.ExtraBold, fontSize = 19.sp, color = Color(0xFF1A1C2E))
                Text(displayEmail, fontSize = 13.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun SettingItemCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    showSwitch: Boolean = false,
    switchState: Boolean = false,
    onSwitchChange: (Boolean) -> Unit = {},
    isRed: Boolean = false,
    hPadding: androidx.compose.ui.unit.Dp,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = hPadding, vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Row(modifier = Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = if (isRed) Color.Red else DeepPurple, modifier = Modifier.size(22.dp))
            Text(
                text = title,
                modifier = Modifier.weight(1f).padding(start = 16.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = if (isRed) Color.Red else Color(0xFF1A1C2E)
            )
            if (showSwitch) {
                Switch(
                    checked = switchState,
                    onCheckedChange = { onSwitchChange(it) },
                    colors = SwitchDefaults.colors(checkedTrackColor = DeepPurple)
                )
            } else {
                Icon(Icons.Default.ChevronRight, null, tint = Color.LightGray, modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
fun SettingsBanner(widthClass: WindowWidthSizeClass) {
    val hPadding = if (widthClass == WindowWidthSizeClass.Compact) 20.dp else 45.dp
    val bannerHeight = if (widthClass == WindowWidthSizeClass.Compact) 85.dp else 110.dp

    Box(modifier = Modifier.fillMaxWidth().height(bannerHeight).padding(horizontal = hPadding, vertical = 6.dp).clip(RoundedCornerShape(24.dp))) {
        Image(painter = painterResource(id = R.drawable.banner), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
        Box(modifier = Modifier.fillMaxSize().background(Brush.horizontalGradient(listOf(Color.Black.copy(0.4f), Color.Transparent))))
        Text("Kỷ luật là sức mạnh ✨", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp, modifier = Modifier.padding(start = 24.dp).align(Alignment.CenterStart))
    }
}