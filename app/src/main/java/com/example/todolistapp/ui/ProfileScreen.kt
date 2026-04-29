package com.example.todolistapp.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.Cake
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.todolistapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavHostController) {
    // --- STATE DỮ LIỆU ---
    var fullName by remember { mutableStateOf("Nguyễn Văn Kiên") }
    var email by remember { mutableStateOf("kien.nguyen@gmail.com") }
    var age by remember { mutableStateOf("22") }
    var address by remember { mutableStateOf("Đà Nẵng, Việt Nam") }

    // --- STATE HÌNH ẢNH ---
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> selectedImageUri = uri }

    // --- STATE ĐỔI EMAIL ---
    // --- STATE ĐỔI EMAIL ---
    var showEmailDialog by remember { mutableStateOf(false) }
    var passwordConfirm by remember { mutableStateOf("") }
    var newEmailInput by remember { mutableStateOf("") }

    if (showEmailDialog) {
        AlertDialog(
            onDismissRequest = {
                showEmailDialog = false
                passwordConfirm = "" // Xóa trắng để lần sau nhập lại cho bảo mật
            },
            title = {
                Text("Thay đổi Email", fontWeight = FontWeight.Bold, color = DeepPurple)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        "Vui lòng nhập email mới và xác nhận bằng mật khẩu hiện tại.",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )

                    // 1. NHẬP EMAIL TRƯỚC (Hợp lý hơn)
                    OutlinedTextField(
                        value = newEmailInput,
                        onValueChange = { newEmailInput = it },
                        label = { Text("Email mới") },
                        placeholder = { Text("example@gmail.com") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                            keyboardType = androidx.compose.ui.text.input.KeyboardType.Email
                        )
                    )

                    // 2. NHẬP MẬT KHẨU SAU
                    OutlinedTextField(
                        value = passwordConfirm,
                        onValueChange = { passwordConfirm = it },
                        label = { Text("Mật khẩu xác nhận") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        // Logic: Check email không trống và pass không trống mới cho lưu
                        if(newEmailInput.isNotEmpty() && passwordConfirm.isNotEmpty()) {
                            email = newEmailInput
                            showEmailDialog = false
                            passwordConfirm = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(DeepPurple),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Cập nhật")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEmailDialog = false }) {
                    Text("Hủy", color = Color.Gray)
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize().background(PurpleBg)) {
        Box(modifier = Modifier.fillMaxWidth().height(220.dp).background(Brush.verticalGradient(listOf(DeepPurple, PurpleBg))))

        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Row(modifier = Modifier.statusBarsPadding().padding(horizontal = 16.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                }
                Text("Thông tin cá nhân", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            Column(
                modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // AVATAR (Ấn vào để chọn ảnh)
                Box(modifier = Modifier.padding(vertical = 20.dp).clickable { photoPickerLauncher.launch("image/*") }) {
                    if (selectedImageUri != null) {
                        AsyncImage(
                            model = selectedImageUri,
                            contentDescription = null,
                            modifier = Modifier.size(110.dp).clip(CircleShape).border(4.dp, Color.White, CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = null,
                            modifier = Modifier.size(110.dp).clip(CircleShape).border(4.dp, Color.White, CircleShape).background(Color.White),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Box(modifier = Modifier.align(Alignment.BottomEnd).size(32.dp).background(DeepPurple, CircleShape).border(2.dp, Color.White, CircleShape), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.CameraAlt, null, tint = Color.White, modifier = Modifier.size(16.dp))
                    }
                }

                // INPUT FIELDS
                ProfileInputField(label = "Họ và tên", value = fullName, onValueChange = { fullName = it }, icon = Icons.Rounded.Person)

                // EMAIL FIELD (Có nút Edit để mở Dialog)
                ProfileInputField(
                    label = "Email đăng nhập",
                    value = email,
                    onValueChange = {},
                    icon = Icons.Rounded.Email,
                    enabled = false,
                    trailingIcon = {
                        IconButton(onClick = { showEmailDialog = true }) {
                            Icon(Icons.Default.Edit, null, tint = DeepPurple, modifier = Modifier.size(18.dp))
                        }
                    }
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Box(modifier = Modifier.weight(0.35f)) {
                        ProfileInputField(label = "Tuổi", value = age, onValueChange = { if(it.length <= 3) age = it }, icon = Icons.Rounded.Cake)
                    }
                    Box(modifier = Modifier.weight(0.65f)) {
                        ProfileInputField(label = "Địa chỉ", value = address, onValueChange = { address = it }, icon = Icons.Rounded.LocationOn)
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))
                Button(onClick = { /* Lưu DB */ }, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(DeepPurple)) {
                    Text("Lưu thay đổi", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    enabled: Boolean = true,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = label, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DeepPurple.copy(0.7f), modifier = Modifier.padding(start = 4.dp, bottom = 4.dp))
        Surface(shape = RoundedCornerShape(16.dp), color = if (enabled) Color.White else Color.White.copy(0.6f), shadowElevation = 1.dp) {
            Row(modifier = Modifier.padding(start = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, null, tint = DeepPurple, modifier = Modifier.size(20.dp))
                TextField(
                    value = value,
                    onValueChange = onValueChange,
                    enabled = enabled,
                    modifier = Modifier.weight(1f),
                    // Tinh chỉnh colors để không bị padding thừa làm cắt chữ
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    textStyle = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Medium),
                    singleLine = true
                )
                if (trailingIcon != null) trailingIcon()
            }
        }
    }
}