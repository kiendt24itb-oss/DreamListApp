package com.example.todolistapp.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun ExploreScreen(
    navController: NavHostController,
    accountId: Int
) {
    val windowSize = LocalWindowSizeClass.current
    val widthClass = windowSize.widthSizeClass

    // --- THÔNG SỐ ADAPTIVE ---
    val horizontalPadding = if (widthClass == WindowWidthSizeClass.Compact) 24.dp else 45.dp
    val headerSize = if (widthClass == WindowWidthSizeClass.Compact) 32.sp else 42.sp
    val subHeaderSize = if (widthClass == WindowWidthSizeClass.Compact) 15.sp else 18.sp

    val exploreArticles = listOf(
        ExploreArticle("7 thói quen nhỏ thay đổi cuộc sống", "PHÁT TRIỂN", "8 phút", "4.8", DeepPurple, Icons.Default.AutoStories),
        ExploreArticle("Lập kế hoạch hiệu quả với 3P", "KỸ NĂNG", "6 phút", "4.7", Color(0xFFE84393), Icons.Default.Lightbulb),
        ExploreArticle("Podcast truyền cảm hứng tuần này", "CẢM HỨNG", "12 phút", "4.9", OrangeStatus, Icons.Default.Headphones),
        ExploreArticle("Thiền định cho người mới bắt đầu", "SỨC KHỎE", "5 phút", "4.6", Color.Green, Icons.Default.SelfImprovement),
        ExploreArticle("Quản lý thời gian đỉnh cao", "KỸ NĂNG", "10 phút", "4.5", DeepPurple, Icons.Default.Timer),
        ExploreArticle("Tư duy thiết kế trong đời sống", "SÁNG TẠO", "7 phút", "4.9", Color(0xFF00CEC9), Icons.Default.Brush)
    )

    Box(modifier = Modifier.fillMaxSize().background(PurpleBg)) {
        // 1. NỀN ẢNH ADAPTIVE
        Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(if (widthClass == WindowWidthSizeClass.Compact) 0.55f else 0.65f)) {
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

        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Column(modifier = Modifier.statusBarsPadding().padding(start = horizontalPadding, end = horizontalPadding, top = 20.dp, bottom = 10.dp)) {
                Text("Khám phá ✨", fontSize = headerSize, fontWeight = FontWeight.ExtraBold, color = Color.White)
                Text("Ý tưởng và cảm hứng mỗi ngày.", fontSize = subHeaderSize, color = Color.White.copy(alpha = 0.85f))
            }

            // Chủ đề nổi bật (LazyRow)
            ExploreSectionHeader("Chủ đề nổi bật", textColor = Color.White, horizontalPadding)
            FeaturedTopicsHomeStyle(widthClass, horizontalPadding)

            // Tiêu đề Gợi ý
            ExploreSectionHeader("Gợi ý cho bạn", textColor = DeepPurple, horizontalPadding)

            // VÙNG CUỘN TASK/ARTICLE
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 8.dp)
            ) {
                // Nếu màn hình lớn, bạn có thể cân nhắc dùng FlowRow hoặc chia Grid,
                // nhưng ở đây mình tối ưu PremiumCard để giãn ra đẹp hơn.
                exploreArticles.forEach { article ->
                    PremiumArticleCard(article, horizontalPadding, widthClass)
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            // BOTTOM AREA
            Column(modifier = Modifier.background(PurpleBg)) {
                ModernThinBanner(widthClass)
                SquaredBottomNav(
                    navController = navController,
                    accountId = accountId
                )
            }
        }
    }
}

@Composable
fun ExploreSectionHeader(title: String, textColor: Color, hPadding: androidx.compose.ui.unit.Dp) {
    Text(
        text = title,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 20.sp,
        color = textColor,
        modifier = Modifier.padding(start = hPadding, end = hPadding, top = 12.dp, bottom = 8.dp)
    )
}

@Composable
fun FeaturedTopicsHomeStyle(widthClass: WindowWidthSizeClass, hPadding: androidx.compose.ui.unit.Dp) {
    val topics = listOf(
        Triple("Phát triển", "128 bài", Color(0xFF6C5CE7)),
        Triple("Kỹ năng", "96 bài", Color(0xFFE84393)),
        Triple("Sáng tạo", "74 bài", Color(0xFF00CEC9))
    )

    val itemWidth = if (widthClass == WindowWidthSizeClass.Compact) 160.dp else 220.dp
    val itemHeight = if (widthClass == WindowWidthSizeClass.Compact) 75.dp else 90.dp

    LazyRow(
        contentPadding = PaddingValues(horizontal = hPadding),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(topics) { topic ->
            Box(
                modifier = Modifier
                    .width(itemWidth)
                    .height(itemHeight)
                    .clip(RoundedCornerShape(22.dp))
                    .background(Color.Black.copy(alpha = 0.45f))
                    .border(1.2.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(22.dp))
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(if (widthClass == WindowWidthSizeClass.Compact) 36.dp else 44.dp)
                            .background(topic.third.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.AutoAwesome, null, tint = topic.third, modifier = Modifier.size(20.dp))
                    }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(topic.first, color = Color.White, fontWeight = FontWeight.Bold, fontSize = if (widthClass == WindowWidthSizeClass.Compact) 13.sp else 16.sp)
                        Text(topic.second, color = Color.White.copy(alpha = 0.6f), fontSize = if (widthClass == WindowWidthSizeClass.Compact) 10.sp else 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun PremiumArticleCard(article: ExploreArticle, hPadding: androidx.compose.ui.unit.Dp, widthClass: WindowWidthSizeClass) {
    val iconBoxSize = if (widthClass == WindowWidthSizeClass.Compact) 56.dp else 68.dp
    val titleSize = if (widthClass == WindowWidthSizeClass.Compact) 15.sp else 18.sp

    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = hPadding, vertical = 6.dp),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 0.8.dp
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(iconBoxSize).clip(RoundedCornerShape(18.dp)).background(article.color.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(article.icon, null, tint = article.color, modifier = Modifier.size(26.dp))
            }
            Column(modifier = Modifier.weight(1f).padding(horizontal = 16.dp)) {
                Text(article.tag, color = article.color, fontSize = 11.sp, fontWeight = FontWeight.Black)
                Text(article.title, fontWeight = FontWeight.Bold, fontSize = titleSize, color = Color(0xFF1A1C2E), maxLines = 1)
                Text("${article.time} • ⭐ ${article.rating}", fontSize = 13.sp, color = Color.Gray)
            }
            Icon(Icons.Default.ArrowForwardIos, null, tint = Color.LightGray, modifier = Modifier.size(14.dp))
        }
    }
}

@Composable
fun ModernThinBanner(widthClass: WindowWidthSizeClass) {
    // --- KHỚP THÔNG SỐ VỚI CALENDARSCREEN ---
    val hPadding = if (widthClass == WindowWidthSizeClass.Compact) 20.dp else 45.dp
    val bannerHeight = when (widthClass) {
        WindowWidthSizeClass.Compact -> 85.dp
        WindowWidthSizeClass.Medium -> 105.dp
        else -> 125.dp // Đồng bộ 125.dp thay vì 120.dp
    }
    val verticalPadding = 6.dp // Khớp với 6.dp của file kia
    val cornerRadius = 24.dp   // Khớp với 24.dp của file kia

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(bannerHeight)
            .padding(horizontal = hPadding, vertical = verticalPadding)
            .clip(RoundedCornerShape(cornerRadius))
    ) {
        Image(
            painter = painterResource(id = R.drawable.banner),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Gradient overlay đồng bộ
        Box(modifier = Modifier.fillMaxSize().background(
            Brush.horizontalGradient(listOf(Color.Black.copy(alpha = 0.45f), Color.Transparent))
        ))

        Box(
            modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Column {
                Text(
                    "Kiến thức là sức mạnh",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = if (widthClass == WindowWidthSizeClass.Compact) 15.sp else 20.sp
                )
                Text(
                    "Mỗi ngày một khám phá mới ✨",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = if (widthClass == WindowWidthSizeClass.Compact) 11.sp else 14.sp
                )
            }
        }
    }
}
data class ExploreArticle(
    val title: String,
    val tag: String,
    val time: String,
    val rating: String,
    val color: Color,
    val icon: ImageVector
)