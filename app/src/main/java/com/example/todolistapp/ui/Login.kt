package com.example.todolistapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.todolistapp.R
import com.example.todolistapp.viewmodel.AuthViewModel

@Composable
fun Login(navController: NavHostController) {

    // ===== VIEWMODEL =====
    val viewModel: AuthViewModel = viewModel()

    // ===== STATE =====
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val loginResult by viewModel.loginResult.collectAsState()
    val error by viewModel.error.collectAsState()
    val loading by viewModel.loading.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {

        // ===== BACKGROUND =====
        Image(
            painter = painterResource(id = R.drawable.b_g),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.weight(0.1f))

            // ===== LOGO =====
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier.size(160.dp)
            )

            Text(
                text = "dream list",
                fontSize = 46.sp,
                color = Color(0xFF4A3DA1),
                fontFamily = FontFamily.Serif
            )

            Text(
                text = "Biến ước mơ thành kế hoạch ✨",
                textAlign = TextAlign.Center,
                fontSize = 18.sp, // Tăng kích thước (từ 14 lên 18)
                color = Color.White.copy(alpha = 0.9f), // Màu trắng hơi trong suốt tí cho sang
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(30.dp))

            // ===== EMAIL =====
            CustomTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                icon = Icons.Default.Email
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ===== PASSWORD =====
            CustomTextField(
                value = password,
                onValueChange = { password = it },
                label = "Mật khẩu",
                icon = Icons.Default.Lock,
                isPassword = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ===== ERROR =====
            error?.let {
                Text(text = it, color = Color.Red)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ===== BUTTON LOGIN =====
            GradientButton(text = if (loading) "Đang đăng nhập..." else "Đăng nhập") {
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    viewModel.login(email, password)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ===== LOGIN RESULT =====
            loginResult?.let { res ->
                if (res.success) {

                    // 👉 NAV HOME
                    navController.navigate("Home") {
                        popUpTo("Login") { inclusive = true }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(0.1f))

            // ===== REGISTER =====
            Row {
                Text("Chưa có tài khoản? ")
                Text(
                    text = "Đăng ký",
                    color = Color(0xFF6B62D9),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        navController.navigate("Register")
                    }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    isPassword: Boolean = false
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val mainPurple = Color(0xFF6B62D9) // Màu tím layout của bạn

    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        placeholder = { Text(label, color = Color.Gray) },
        // Đổi màu Icon bên trái (Email/Lock)
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = mainPurple // Lên màu tím
            )
        },
        trailingIcon = {
            if (isPassword) {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible)
                            Icons.Default.Visibility
                        else Icons.Default.VisibilityOff,
                        contentDescription = null,
                        tint = mainPurple // Icon con mắt cũng màu tím luôn
                    )
                }
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = Color.Black
        ),
        singleLine = true,
        visualTransformation =
            if (isPassword && !passwordVisible)
                PasswordVisualTransformation()
            else VisualTransformation.None,
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
fun GradientButton(text: String, onClick: () -> Unit) {

    val gradient = Brush.linearGradient(
        colors = listOf(Color(0xFF9D89F3), Color(0xFF6B62D9)),
        start = Offset(0f, 0f),
        end = Offset(1000f, 1000f)
    )

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        contentPadding = PaddingValues(),
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient),
            contentAlignment = Alignment.Center
        ) {
            Text(text = text, color = Color.White)
        }
    }
}