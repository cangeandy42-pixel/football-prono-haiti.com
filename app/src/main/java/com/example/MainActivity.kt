package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.HeaderBanner
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.PremiumScreen
import com.example.ui.screens.SubscriptionScreen
import com.example.ui.screens.TicketBuilderScreen
import com.example.ui.theme.CardPitchGreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.PitchDarker
import com.example.ui.theme.PitchGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TrophyGold
import com.example.ui.viewmodel.PronoViewModel

enum class ScreenTab(val title: String, val icon: ImageVector, val tag: String) {
    HOME("Akèy", Icons.Default.Home, "tab_home"),
    SUBSCRIBE("Abònman", Icons.Default.Payment, "tab_subscribe"),
    PREMIUM("VIP", Icons.Default.WorkspacePremium, "tab_premium"),
    TICKET("Tikè", Icons.Default.ConfirmationNumber, "tab_ticket"),
    ADMIN("Admin", Icons.Default.AdminPanelSettings, "tab_admin")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppScreen()
            }
        }
    }
}

@Composable
fun MainAppScreen(
    viewModel: PronoViewModel = viewModel()
) {
    var currentTab by remember { mutableStateOf(ScreenTab.HOME) }
    val isVipUnlocked by viewModel.isVipUnlocked.collectAsState()
    val ticketMatches by viewModel.ticketMatches.collectAsState()

    // Handle back button to return to HOME if on sub-screen
    BackHandler(enabled = currentTab != ScreenTab.HOME) {
        currentTab = ScreenTab.HOME
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            HeaderBanner(isVip = isVipUnlocked)
        },
        bottomBar = {
            NavigationBar(
                containerColor = PitchDarker,
                modifier = Modifier.testTag("bottom_nav_bar")
            ) {
                ScreenTab.values().forEach { tab ->
                    val isSelected = currentTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { currentTab = tab },
                        icon = {
                            if (tab == ScreenTab.TICKET && ticketMatches.isNotEmpty()) {
                                BadgedBox(
                                    badge = {
                                        Badge(
                                            containerColor = TrophyGold,
                                            contentColor = PitchDarker
                                        ) {
                                            Text(
                                                text = "${ticketMatches.size}",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 10.sp
                                            )
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = tab.icon,
                                        contentDescription = tab.title,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            } else {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = tab.title,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        },
                        label = {
                            Text(
                                text = tab.title,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PitchDarker,
                            selectedTextColor = PitchGreen,
                            indicatorColor = if (tab == ScreenTab.PREMIUM) TrophyGold else PitchGreen,
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted
                        ),
                        modifier = Modifier.testTag(tab.tag)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(CardPitchGreen)
        ) {
            when (currentTab) {
                ScreenTab.HOME -> HomeScreen(
                    viewModel = viewModel,
                    onNavigateToSubscribe = { planPrice ->
                        viewModel.selectPlan(planPrice)
                        currentTab = ScreenTab.SUBSCRIBE
                    }
                )
                ScreenTab.SUBSCRIBE -> SubscriptionScreen(
                    viewModel = viewModel,
                    onNavigateToPremium = {
                        currentTab = ScreenTab.PREMIUM
                    }
                )
                ScreenTab.PREMIUM -> PremiumScreen(
                    viewModel = viewModel,
                    onNavigateToSubscribe = {
                        currentTab = ScreenTab.SUBSCRIBE
                    }
                )
                ScreenTab.TICKET -> TicketBuilderScreen(
                    viewModel = viewModel,
                    onNavigateToHome = {
                        currentTab = ScreenTab.HOME
                    }
                )
                ScreenTab.ADMIN -> AdminScreen(
                    viewModel = viewModel
                )
            }
        }
    }
}
