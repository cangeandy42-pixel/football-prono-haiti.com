package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SubscriptionRequest
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    viewModel: PronoViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isAdminLoggedIn by viewModel.isAdminLoggedIn.collectAsState()
    val adminLoginError by viewModel.adminLoginError.collectAsState()
    val allMatches by viewModel.allMatches.collectAsState()
    val paymentInfo by viewModel.paymentInfo.collectAsState()
    val subscriptionRequests by viewModel.subscriptionRequests.collectAsState()

    var pinInput by remember { mutableStateOf("") }
    var showChangePinDialog by remember { mutableStateOf(false) }
    var newPinInput by remember { mutableStateOf("") }

    // Payment settings form state
    var editMonCash by remember(paymentInfo) { mutableStateOf(paymentInfo.monCash) }
    var editNatCash by remember(paymentInfo) { mutableStateOf(paymentInfo.natCash) }
    var editBinance by remember(paymentInfo) { mutableStateOf(paymentInfo.binance) }

    // Match form state from ViewModel
    val homeTeam by viewModel.formHomeTeam.collectAsState()
    val awayTeam by viewModel.formAwayTeam.collectAsState()
    val league by viewModel.formLeague.collectAsState()
    val matchTime by viewModel.formMatchTime.collectAsState()
    val pickAnalysis by viewModel.formPickAnalysis.collectAsState()
    val prediction by viewModel.formPrediction.collectAsState()
    val confidence by viewModel.formConfidence.collectAsState()
    val visibility by viewModel.formVisibility.collectAsState()
    val odds by viewModel.formOdds.collectAsState()
    val formMessage by viewModel.formMessage.collectAsState()

    val predictionOptions = listOf(
        "1 — Viktwa ekip lakay",
        "X — Match nil",
        "2 — Viktwa ekip deyò",
        "Plis pase 1.5 gòl",
        "Plis pase 2.5 gòl",
        "Mwens pase 2.5 gòl",
        "Chak ekip make",
        "Double chans 1X",
        "Double chans X2",
        "Rezilta egzak"
    )

    val confidenceOptions = listOf("Wo", "Mwayen", "Ba", "Pa endike")

    val visibilityOptions = listOf(
        "premium" to "Espas Premium sèlman",
        "public" to "Akèy — piblik",
        "both" to "Piblik ak Premium"
    )

    var predictionExpanded by remember { mutableStateOf(false) }
    var confidenceExpanded by remember { mutableStateOf(false) }
    var visibilityExpanded by remember { mutableStateOf(false) }

    if (!isAdminLoggedIn) {
        // PIN Login Screen
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(20.dp)
                .testTag("admin_login_box"),
            contentAlignment = Alignment.Center
        ) {
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
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(28.dp))
                            .background(CardElevated)
                            .border(1.dp, PitchGreen, RoundedCornerShape(28.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = PitchGreen,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Administratè — Football Prono Haiti",
                        color = TextPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Antre kòd sekrè administratè a pou jere analiz ak peman ou yo. (Kòd pa defo: 2468)",
                        color = TextMuted,
                        fontSize = 12.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    OutlinedTextField(
                        value = pinInput,
                        onValueChange = { pinInput = it },
                        label = { Text("Kòd PIN administratè") },
                        placeholder = { Text("2468") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_pin_input"),
                        shape = RoundedCornerShape(10.dp),
                        colors = outlinedColors()
                    )

                    if (adminLoginError != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = adminLoginError ?: "",
                            color = Color(0xFFFF5252),
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = { viewModel.loginAdmin(pinInput) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("admin_login_submit_btn"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PitchGreen,
                            contentColor = PitchDarker
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "Antre nan Espas Mwen",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    } else {
        // Authenticated Admin Dashboard
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .testTag("admin_dashboard"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Admin Top Bar
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "⚙️ ESPAS ADMINISTRATÈ",
                            color = PitchGreenLight,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Jesyon analiz, peman ak kliyan",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    }

                    Row {
                        OutlinedButton(
                            onClick = { showChangePinDialog = true },
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("Chanje PIN", fontSize = 11.sp, color = TrophyGold)
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        IconButton(
                            onClick = { viewModel.logoutAdmin() },
                            modifier = Modifier.testTag("admin_logout_btn")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Logout,
                                contentDescription = "Dekonekte",
                                tint = Color(0xFFFF5252)
                            )
                        }
                    }
                }
            }

            // Write / Edit Analysis Form Card
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
                        Text(
                            text = if (viewModel.editingMatchId != null) "💾 Modifye Analiz la" else "✍️ Ekri Pwòp Analiz Mwen",
                            color = TrophyGold,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Ou ka antre nenpòt ekip, kòt ak detay taktik ou vle.",
                            color = TextMuted,
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Home Team & Away Team
                        OutlinedTextField(
                            value = homeTeam,
                            onValueChange = { viewModel.formHomeTeam.value = it },
                            label = { Text("Ekip lakay") },
                            placeholder = { Text("Egzanp: Barcelona") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("form_home_team_input"),
                            shape = RoundedCornerShape(10.dp),
                            colors = outlinedColors()
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = awayTeam,
                            onValueChange = { viewModel.formAwayTeam.value = it },
                            label = { Text("Ekip deyò") },
                            placeholder = { Text("Egzanp: Real Madrid") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("form_away_team_input"),
                            shape = RoundedCornerShape(10.dp),
                            colors = outlinedColors()
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // League and Match Time
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = league,
                                onValueChange = { viewModel.formLeague.value = it },
                                label = { Text("Lig / Konpetisyon") },
                                placeholder = { Text("La Liga") },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("form_league_input"),
                                shape = RoundedCornerShape(10.dp),
                                colors = outlinedColors()
                            )

                            OutlinedTextField(
                                value = matchTime,
                                onValueChange = { viewModel.formMatchTime.value = it },
                                label = { Text("Dat ak lè") },
                                placeholder = { Text("Jodi a • 15:00") },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("form_match_time_input"),
                                shape = RoundedCornerShape(10.dp),
                                colors = outlinedColors()
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Odds input
                        OutlinedTextField(
                            value = odds,
                            onValueChange = { viewModel.formOdds.value = it },
                            label = { Text("Kòt (Odds)") },
                            placeholder = { Text("1.85") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("form_odds_input"),
                            shape = RoundedCornerShape(10.dp),
                            colors = outlinedColors()
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Analysis text area with helper button
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Analiz konplè pa mwen",
                                color = TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            TextButton(
                                onClick = { viewModel.insertTemplateText() },
                                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
                            ) {
                                Text("+ Mete Modèl Tèks", color = PitchGreen, fontSize = 11.sp)
                            }
                        }

                        OutlinedTextField(
                            value = pickAnalysis,
                            onValueChange = { viewModel.formPickAnalysis.value = it },
                            placeholder = {
                                Text(
                                    "Fòm dènye match yo:\nAnaliz atak ak defans:\nEstatistik kle:\nPronostic mwen:\nNòt pèsonèl:",
                                    color = TextMuted.copy(alpha = 0.6f),
                                    fontSize = 12.sp
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                                .testTag("form_pick_analysis_input"),
                            shape = RoundedCornerShape(10.dp),
                            colors = outlinedColors()
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Prediction Dropdown
                        ExposedDropdownMenuBox(
                            expanded = predictionExpanded,
                            onExpandedChange = { predictionExpanded = it }
                        ) {
                            OutlinedTextField(
                                value = prediction,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Chwa pronostic") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = predictionExpanded) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                                    .testTag("form_prediction_dropdown"),
                                shape = RoundedCornerShape(10.dp),
                                colors = outlinedColors()
                            )
                            ExposedDropdownMenu(
                                expanded = predictionExpanded,
                                onDismissRequest = { predictionExpanded = false },
                                modifier = Modifier.background(CardElevated)
                            ) {
                                predictionOptions.forEach { opt ->
                                    DropdownMenuItem(
                                        text = { Text(opt, color = TextPrimary, fontSize = 13.sp) },
                                        onClick = {
                                            viewModel.formPrediction.value = opt
                                            predictionExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Confidence and Visibility
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Confidence
                            ExposedDropdownMenuBox(
                                expanded = confidenceExpanded,
                                onExpandedChange = { confidenceExpanded = it },
                                modifier = Modifier.weight(1f)
                            ) {
                                OutlinedTextField(
                                    value = confidence,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Konfyans") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = confidenceExpanded) },
                                    modifier = Modifier
                                        .menuAnchor()
                                        .fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = outlinedColors()
                                )
                                ExposedDropdownMenu(
                                    expanded = confidenceExpanded,
                                    onDismissRequest = { confidenceExpanded = false },
                                    modifier = Modifier.background(CardElevated)
                                ) {
                                    confidenceOptions.forEach { opt ->
                                        DropdownMenuItem(
                                            text = { Text(opt, color = TextPrimary, fontSize = 13.sp) },
                                            onClick = {
                                                viewModel.formConfidence.value = opt
                                                confidenceExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            // Visibility
                            val currentVisLabel = visibilityOptions.firstOrNull { it.first == visibility }?.second ?: "VIP"
                            ExposedDropdownMenuBox(
                                expanded = visibilityExpanded,
                                onExpandedChange = { visibilityExpanded = it },
                                modifier = Modifier.weight(1f)
                            ) {
                                OutlinedTextField(
                                    value = currentVisLabel,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Piblikasyon") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = visibilityExpanded) },
                                    modifier = Modifier
                                        .menuAnchor()
                                        .fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = outlinedColors()
                                )
                                ExposedDropdownMenu(
                                    expanded = visibilityExpanded,
                                    onDismissRequest = { visibilityExpanded = false },
                                    modifier = Modifier.background(CardElevated)
                                ) {
                                    visibilityOptions.forEach { (key, label) ->
                                        DropdownMenuItem(
                                            text = { Text(label, color = TextPrimary, fontSize = 12.sp) },
                                            onClick = {
                                                viewModel.formVisibility.value = key
                                                visibilityExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        // Status message
                        if (formMessage != null) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = formMessage ?: "",
                                color = PitchGreenLight,
                                fontSize = 12.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Submit & Clear buttons
                        Button(
                            onClick = {
                                viewModel.saveMatch(
                                    onSuccess = {
                                        Toast.makeText(context, "Analiz la anrejistre!", Toast.LENGTH_SHORT).show()
                                    },
                                    onError = { err ->
                                        Toast.makeText(context, err, Toast.LENGTH_SHORT).show()
                                    }
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("publish_analysis_btn"),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PitchGreen,
                                contentColor = PitchDarker
                            )
                        ) {
                            Icon(
                                imageVector = if (viewModel.editingMatchId != null) Icons.Default.Save else Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (viewModel.editingMatchId != null) "💾 Sove Chanjman Analiz La" else "📢 Pibliye Analiz Mwen",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedButton(
                            onClick = { viewModel.clearForm() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .testTag("clear_form_btn"),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextMuted)
                        ) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Netwaye fòm nan", fontSize = 12.sp)
                        }
                    }
                }
            }

            // Payment Instructions Settings Card
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
                            Icon(imageVector = Icons.Default.Payment, contentDescription = null, tint = TrophyGold, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "💳 Jesyon Nimewo Peman yo",
                                color = TextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "Chanje nimewo MonCash, NatCash oswa Binance kliyan yo wè lè yo vle peye.",
                            color = TextMuted,
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = editMonCash,
                            onValueChange = { editMonCash = it },
                            label = { Text("MonCash") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("admin_moncash_input"),
                            shape = RoundedCornerShape(8.dp),
                            colors = outlinedColors()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = editNatCash,
                            onValueChange = { editNatCash = it },
                            label = { Text("NatCash") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("admin_natcash_input"),
                            shape = RoundedCornerShape(8.dp),
                            colors = outlinedColors()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = editBinance,
                            onValueChange = { editBinance = it },
                            label = { Text("Binance ID") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("admin_binance_input"),
                            shape = RoundedCornerShape(8.dp),
                            colors = outlinedColors()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                viewModel.updatePaymentSettings(editMonCash, editNatCash, editBinance)
                                Toast.makeText(context, "Nimewo peman yo mete ajou!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("save_payments_btn"),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = TrophyGold,
                                contentColor = PitchDarker
                            )
                        ) {
                            Text("Sove Nimewo Peman Yo", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Customer Subscription Requests Management
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📥 DEMANN ABÒNMAN KLIYAN (${subscriptionRequests.size})",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (subscriptionRequests.isEmpty()) {
                item {
                    Text(
                        text = "Poko gen okenn demann abònman anrejistre.",
                        color = TextMuted,
                        fontSize = 13.sp
                    )
                }
            } else {
                items(subscriptionRequests, key = { it.id }) { req ->
                    RequestAdminCard(
                        request = req,
                        onUpdateStatus = { newStatus ->
                            viewModel.updateRequestStatus(req.id, newStatus)
                            Toast.makeText(context, "Estati chanje: $newStatus", Toast.LENGTH_SHORT).show()
                        },
                        onContactWhatsApp = {
                            val uri = Uri.parse("https://api.whatsapp.com/send?phone=${req.phone}&text=Bonjou%20${req.customerName},%20konsènan%20abònman%20Football%20Prono%20ou%20an...")
                            try {
                                context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                            } catch (e: Exception) {
                                Toast.makeText(context, "Pa ka louvri WhatsApp", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
            }

            // List of Published Analyses
            item {
                Text(
                    text = "📋 ANALIZ MWEN PIBLIYE YO (${allMatches.size})",
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (allMatches.isEmpty()) {
                item {
                    Text("Poko gen analiz pibliye. Ekri premye analiz ou anlè a.", color = TextMuted, fontSize = 13.sp)
                }
            } else {
                items(allMatches, key = { it.id }) { match ->
                    MatchCard(
                        match = match,
                        isAdmin = true,
                        isInTicket = false,
                        onEdit = {
                            viewModel.editMatch(match)
                            Toast.makeText(context, "Analiz chaje nan fòm nan anlè a pou modifikasyon.", Toast.LENGTH_SHORT).show()
                        },
                        onDelete = {
                            viewModel.deleteMatch(match.id)
                            Toast.makeText(context, "Analiz efase.", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }

    // Change PIN Dialog
    if (showChangePinDialog) {
        AlertDialog(
            onDismissRequest = { showChangePinDialog = false },
            title = { Text("Chanje Kòd PIN") },
            text = {
                Column {
                    Text("Antre nouvo kòd PIN administratè a (omwen 4 chif):", fontSize = 13.sp, color = TextMuted)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newPinInput,
                        onValueChange = { newPinInput = it },
                        placeholder = { Text("Nouvo PIN") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        shape = RoundedCornerShape(8.dp),
                        colors = outlinedColors()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newPinInput.length >= 4) {
                            viewModel.updateAdminPin(newPinInput)
                            Toast.makeText(context, "Kòd PIN chanje avèk siksè!", Toast.LENGTH_SHORT).show()
                            showChangePinDialog = false
                            newPinInput = ""
                        } else {
                            Toast.makeText(context, "Kòd la dwe gen omwen 4 chif.", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PitchGreen, contentColor = PitchDarker)
                ) {
                    Text("Sove")
                }
            },
            dismissButton = {
                TextButton(onClick = { showChangePinDialog = false }) {
                    Text("Anile", color = TextMuted)
                }
            },
            containerColor = CardElevated
        )
    }
}

@Composable
private fun RequestAdminCard(
    request: SubscriptionRequest,
    onUpdateStatus: (String) -> Unit,
    onContactWhatsApp: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardElevated)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = request.customerName,
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                val statusColor = when (request.status) {
                    "Apwouve" -> PitchGreenLight
                    "Rejte" -> Color(0xFFFF5252)
                    else -> TrophyGold
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(statusColor.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(text = request.status, color = statusColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Telefòn: ${request.phone} • Plan: ${request.planPrice} (${request.planDuration})",
                color = TextMuted,
                fontSize = 12.sp
            )
            Text(
                text = "Metòd: ${request.paymentMethod} • Ref: ${request.referenceId}",
                color = TrophyGold,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Dat: ${request.createdAt}",
                color = TextMuted.copy(alpha = 0.7f),
                fontSize = 10.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onContactWhatsApp,
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    Icon(imageVector = Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(14.dp), tint = PitchGreenLight)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("WhatsApp", fontSize = 11.sp, color = PitchGreenLight)
                }

                Button(
                    onClick = { onUpdateStatus("Apwouve") },
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PitchGreen, contentColor = PitchDarker),
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Apwouve", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = { onUpdateStatus("Rejte") },
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFF5252)),
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Rejte", fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
private fun outlinedColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = PitchGreen,
    unfocusedBorderColor = CardBorder,
    focusedContainerColor = CardElevated,
    unfocusedContainerColor = CardElevated,
    focusedTextColor = TextPrimary,
    unfocusedTextColor = TextPrimary,
    focusedLabelColor = PitchGreen,
    unfocusedLabelColor = TextMuted
)
