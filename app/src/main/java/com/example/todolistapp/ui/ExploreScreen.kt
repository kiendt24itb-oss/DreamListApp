package com.example.todolistapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.todolistapp.LocalWindowSizeClass
import com.example.todolistapp.R

@Composable
fun ExploreScreen(navController: NavHostController) {
    val windowSize = LocalWindowSizeClass.current
    val widthClass = windowSize.widthSizeClass

    // Dữ liệu mẫu bài viết
    val exploreArticles = listOf(
        ExploreArticle("7 thói quen nhỏ thay đổi cuộc sống", "PHÁT TRIỂN", "8 phút", "4.8", DeepPurple, Icons.Default.AutoStories),
        ExploreArticle("Lập kế hoạch hiệu quả với 3P", "KỸ NĂNG", "6 phút", "4.7", Color(0xFFE84393), Icons.Default.Lightbulb),
        ExploreArticle("Podcast truyền cảm hứng tuần này", "CẢM HỨNG", "12 phút", "4.9", OrangeStatus, Icons.Default.Headphones),
        ExploreArticle("Thiền định cho người mới bắt đầu", "SỨC KHỎE", "5 phút", "4.6", Color.Green, Icons.Default.SelfImprovement),
        ExploreArticle("Quản lý thời gian đỉnh cao", "KỸ NĂNG", "10 phút", "4.5", DeepPurple, Icons.Default.Timer),
        ExploreArticle("Tư duy thiết kế trong đời sống", "SÁNG TẠO", "7 phút", "4.9", Color(0xFF00CEC9), Icons.Default.Brush)
    )

    Box(modifier = Modifier.fillMaxSize().background(PurpleBg)) {
        // 1. LỚP NỀN ẢNH (CỐ ĐỊNH)
        Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.55f)) {
            Image(
                painter = painterResource(id = R.drawable.bg_h),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize().alpha(0.85f)
            )
            Box(modifier = Modifier.fillMaxSize().background(
                Brush.verticalGradient(
                    colors = listOf(Color.Transparent, PurpleBg.copy(alpha = 0.5f), PurpleBg),
                    startY = 600f
                )
            ))
        }

        // 2. NỘI DUNG CHÍNH
        Column(modifier = Modifier.fillMaxSize()) {

            // --- CỤM CỐ ĐỊNH PHẦN TRÊN (Header + Row + Tiêu đề Gợi ý) ---
            Column(modifier = Modifier.background(Color.Transparent)) {
                // Header
                Column(modifier = Modifier.statusBarsPadding().padding(start = 24.dp, end = 24.dp, top = 20.dp, bottom = 10.dp)) {
                    Text("Khám phá ✨", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                    Text("Ý tưởng và cảm hứng mỗi ngày.", fontSize = 15.sp, color = Color.White.copy(alpha = 0.85f))
                }

                // Row Chủ đề nổi bật (Style theo trang Home)
                ExploreSectionHeader("Chủ đề nổi bật", textColor = Color.White)
                FeaturedTopicsHomeStyle()

                // Tiêu đề Gợi ý cho bạn (Cũng cố định luôn)
                ExploreSectionHeader("Gợi ý cho bạn", textColor = DeepPurple)
            }

            // --- VÙNG CUỘN (CHỈ CÁC CARD BÀI VIẾT) ---
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 8.dp)
            ) {
                exploreArticles.forEach { article ->
                    PremiumArticleCard(article)
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            // --- CỤM CỐ ĐỊNH PHẦN DƯỚI (Banner + Footer) ---
            Column(modifier = Modifier.background(PurpleBg)) {
                ModernThinBanner()
                SquaredBottomNav(navController = navController)
            }
        }
    }
}

// --- CÁC COMPONENT CON ---

@Composable
fun ExploreSectionHeader(title: String, textColor: Color) {
    Text(
        text = title,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 20.sp,
        color = textColor,
        modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 12.dp, bottom = 8.dp)
    )
}

@Composable
fun FeaturedTopicsHomeStyle() {
    val topics = listOf(
        Triple("Phát triển", "128 bài", Color(0xFF6C5CE7)),
        Triple("Kỹ năng", "96 bài", Color(0xFFE84393)),
        Triple("Sáng tạo", "74 bài", Color(0xFF00CEC9))
    )
    LazyRow(
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(topics) { topic ->
            Box(
                modifier = Modifier
                    .width(160.dp)
                    .height(75.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Black.copy(alpha = 0.45f))
                    .border(1.2.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(20.dp))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(36.dp).background(topic.third.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.AutoAwesome, null, tint = topic.third, modifier = Modifier.size(18.dp))
                    }
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text(topic.first, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(topic.second, color = Color.White.copy(alpha = 0.6f), fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun PremiumArticleCard(article: ExploreArticle) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(start = 24.dp, end = 24.dp, top = 6.dp, bottom = 6.dp),
        shape = RoundedCornerShape(22.dp),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(56.dp).clip(RoundedCornerShape(16.dp)).background(article.color.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(article.icon, null, tint = article.color, modifier = Modifier.size(24.dp))
            }
            Column(modifier = Modifier.weight(1f).padding(horizontal = 14.dp)) {
                Text(article.tag, color = article.color, fontSize = 10.sp, fontWeight = FontWeight.Black)
                Text(article.title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF1A1C2E), maxLines = 1)
                Text("${article.time} • ⭐ ${article.rating}", fontSize = 12.sp, color = Color.Gray)
            }
            Icon(Icons.Default.ArrowForwardIos, null, tint = Color.LightGray, modifier = Modifier.size(14.dp))
        }
    }
}

@Composable
fun ModernThinBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(85.dp) // Mỏng lại
            .padding(start = 16.dp, end = 16.dp, bottom = 10.dp) // Hở lề một ít
            .clip(RoundedCornerShape(18.dp)) // Bo tròn
    ) {
        Image(
            painter = painterResource(id = R.drawable.banner),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Kiến thức là sức mạnh", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("Mỗi ngày một khám phá mới ✨", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp)
            }
        }
    }
}

// Data Class và các màu sắc
data class ExploreArticle(
    val title: String,
    val tag: String,
    val time: String,
    val rating: String,
    val color: Color,
    val icon: ImageVector
)