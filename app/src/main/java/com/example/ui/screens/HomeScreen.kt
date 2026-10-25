package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MatchItem
import com.example.ui.components.MatchCard
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CardElevated
import com.example.ui.theme.CardPitchGreen
import com.example.ui.theme.PitchDarker
import com.example.ui.theme.PitchGreen
import com.example.ui.theme.PitchGreenLight
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TrophyGold
import com.example.ui.viewmodel.PronoViewModel

@Composable
fun HomeScreen(
    viewModel: PronoViewModel,
    onNavigateToSubscribe: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val publicMatches by viewModel.publicMatches.collectAsState()
    val ticketMatches by viewModel.ticketMatches.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    val filteredMatches = remember(publicMatches, searchQuery) {
        if (searchQuery.isBlank()) {
            publicMatches
        } else {
            publicMatches.filter {
                it.homeTeam.contains(searchQuery, ignoreCase = true) ||
                it.awayTeam.contains(searchQuery, ignoreCase = true) ||
                it.league.contains(searchQuery, ignoreCase = true) ||
                it.prediction.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome Banner Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CardBorder, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardPitchGreen)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(PitchGreen)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "BYENVENI NAN FOOTBALL PRONO HAITI",
                            color = PitchGreen,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Analiz foutbòl, prediksyon ak pronostik",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Tout analiz se opinyon pwofesyonèl ekspè foutbòl nou yo. Nou etidye fòm ekip yo, taktik, absans ak estatistik pou nou ba ou pi bon chwa posib.",
                        color = TextMuted,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Subscription Plans Section
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "🏆 PLAN ABÒNMAN PREMIUM",
                    color = TrophyGold,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    PlanCard(
                        title = "Plan 15 Jou",
                        price = "250 HTG",
                        duration = "15 jou",
                        isPopular = false,
                        onSelect = { onNavigateToSubscribe("250") },
                        modifier = Modifier.weight(1f)
                    )

                    PlanCard(
                        title = "Plan 1 Mwa",
                        price = "500 HTG",
                        duration = "30 jou",
                        isPopular = true,
                        onSelect = { onNavigateToSubscribe("500") },
                        modifier = Modifier.weight(1f)
                    )

                    PlanCard(
                        title = "Plan 2 Mwa",
                        price = "1 000 HTG",
                        duration = "60 jou",
                        isPopular = false,
                        onSelect = { onNavigateToSubscribe("1000") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Section header for Public Matches + Search
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📢 ANALIZ PIBLIK",
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${filteredMatches.size} match disponib",
                        color = TextMuted,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Chèche pa ekip, lig oswa pronostic...", color = TextMuted, fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Chèche", tint = TextMuted)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("search_match_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PitchGreen,
                        unfocusedBorderColor = CardBorder,
                        focusedContainerColor = CardPitchGreen,
                        unfocusedContainerColor = CardPitchGreen,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    singleLine = true
                )
            }
        }

        // Matches List
        if (filteredMatches.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(CardPitchGreen)
                        .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (searchQuery.isNotBlank()) "Okenn match pa koresponn ak rechèch ou a." else "Pa gen analiz piblik pou kounye a.",
                        color = TextMuted,
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            items(filteredMatches, key = { it.id }) { match ->
                val isInTicket = ticketMatches.any { it.id == match.id }
                MatchCard(
                    match = match,
                    isAdmin = false,
                    isInTicket = isInTicket,
                    onToggleTicket = {
                        if (isInTicket) {
                            viewModel.removeFromTicket(match.id)
                        } else {
                            viewModel.addToTicket(match)
                        }
                    }
                )
            }
        }

        // Disclaimer Notice
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF2B2513))
                    .border(1.dp, Color(0xFF6B5A24), RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = TrophyGold,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Avi enpòtan: Tout analiz ak pronostik yo se pou rezon enfòmatif sèlman. Pa gen okenn garanti 100%. Toujou jwe ak modération.",
                        color = Color(0xFFFFE8A3),
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun PlanCard(
    title: String,
    price: String,
    duration: String,
    isPopular: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .border(
                1.dp,
                if (isPopular) TrophyGold else CardBorder,
                RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isPopular) CardElevated else CardPitchGreen
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isPopular) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(TrophyGold)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "POPILÈ",
                        color = PitchDarker,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }

            Text(
                text = title,
                color = TextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = price,
                color = PitchGreen,
                fontSize = 16.sp,
                fontWeight = FontWeight.Black
            )

            Text(
                text = duration,
                color = TextMuted,
                fontSize = 10.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onSelect,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(34.dp)
                    .testTag("choose_plan_${price.replace(" ", "_")}"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isPopular) PitchGreen else CardElevated,
                    contentColor = if (isPopular) PitchDarker else TextPrimary
                ),
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Chwazi",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
