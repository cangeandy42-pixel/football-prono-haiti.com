package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun SubscriptionScreen(
    viewModel: PronoViewModel,
    onNavigateToPremium: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val paymentInfo by viewModel.paymentInfo.collectAsState()
    val selectedPlan by viewModel.selectedPlan.collectAsState()
    val isVipUnlocked by viewModel.isVipUnlocked.collectAsState()
    val requests by viewModel.subscriptionRequests.collectAsState()

    var customerName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var transactionRef by remember { mutableStateOf("") }
    var selectedMethod by remember { mutableStateOf("MonCash") }
    var vipCodeInput by remember { mutableStateOf("") }

    var formMessage by remember { mutableStateOf<String?>(null) }
    var isSuccessMessage by remember { mutableStateOf(false) }

    val planLabel = when (selectedPlan) {
        "250" -> "250 HTG — 15 jou"
        "1000" -> "1 000 HTG — 60 jou"
        else -> "500 HTG — 30 jou"
    }

    val activePaymentInstruction = when (selectedMethod) {
        "MonCash" -> paymentInfo.monCash
        "NatCash" -> paymentInfo.natCash
        else -> paymentInfo.binance
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("subscription_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // VIP Active status badge if unlocked
        if (isVipUnlocked) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF193B26))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = PitchGreenLight,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Abònman VIP Aktif!",
                                    color = TextPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Ou gen aksè ak tout analiz Premium yo.",
                                    color = PitchGreenLight,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        Button(
                            onClick = onNavigateToPremium,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PitchGreen,
                                contentColor = PitchDarker
                            ),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text("Ale nan VIP", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Main Subscription Box
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
                        text = "💎 PRAN ABÒNMAN OU",
                        color = TrophyGold,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Step 1: Select Plan
                    Text(
                        text = "1. Chwazi plan ou",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PlanSelectChip(
                            label = "250 HTG",
                            sub = "15 jou",
                            isSelected = selectedPlan == "250",
                            onSelect = { viewModel.selectPlan("250") },
                            modifier = Modifier.weight(1f)
                        )
                        PlanSelectChip(
                            label = "500 HTG",
                            sub = "30 jou",
                            isSelected = selectedPlan == "500",
                            onSelect = { viewModel.selectPlan("500") },
                            modifier = Modifier.weight(1f)
                        )
                        PlanSelectChip(
                            label = "1 000 HTG",
                            sub = "60 jou",
                            isSelected = selectedPlan == "1000",
                            onSelect = { viewModel.selectPlan("1000") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Step 2: Payment Method
                    Text(
                        text = "2. Metòd peman",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PaymentMethodChip(
                            name = "MonCash",
                            isSelected = selectedMethod == "MonCash",
                            onSelect = { selectedMethod = "MonCash" },
                            modifier = Modifier.weight(1f)
                        )
                        PaymentMethodChip(
                            name = "NatCash",
                            isSelected = selectedMethod == "NatCash",
                            onSelect = { selectedMethod = "NatCash" },
                            modifier = Modifier.weight(1f)
                        )
                        PaymentMethodChip(
                            name = "Binance",
                            isSelected = selectedMethod == "Binance",
                            onSelect = { selectedMethod = "Binance" },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Instruction Card with Copy Button
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, PitchGreen.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = CardElevated)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Enstriksyon peman $selectedMethod:",
                                    color = PitchGreenLight,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                IconButton(
                                    onClick = {
                                        copyToClipboard(context, activePaymentInstruction)
                                        Toast.makeText(context, "Nimewo peman an kopye!", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.testTag("copy_payment_info_btn")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = "Kopye nimewo a",
                                        tint = PitchGreenLight,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }

                            Text(
                                text = activePaymentInstruction,
                                color = TextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Transfere montan $planLabel a sou nimewo sa a, epi mete nimewo tranzaksyon an anba a.",
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Step 3: Form inputs
                    Text(
                        text = "3. Ranpli enfòmasyon ou",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = customerName,
                        onValueChange = { customerName = it },
                        label = { Text("Non ou konplè") },
                        placeholder = { Text("Egzanp: Jean Baptiste") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("customer_name_input"),
                        shape = RoundedCornerShape(10.dp),
                        colors = outlinedColors()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Telefòn / WhatsApp") },
                        placeholder = { Text("+509 3844-0000") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("customer_phone_input"),
                        shape = RoundedCornerShape(10.dp),
                        colors = outlinedColors()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = transactionRef,
                        onValueChange = { transactionRef = it },
                        label = { Text("Referans Tranzaksyon / ID Peman") },
                        placeholder = { Text("Egzanp: MC-8923472 oswa SMS ID") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("customer_ref_input"),
                        shape = RoundedCornerShape(10.dp),
                        colors = outlinedColors()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Action Button: Voye demann
                    Button(
                        onClick = {
                            viewModel.submitSubscriptionRequest(
                                name = customerName,
                                phone = phone,
                                planPrice = "$selectedPlan HTG",
                                planDuration = planLabel,
                                method = selectedMethod,
                                ref = transactionRef,
                                onSuccess = {
                                    formMessage = "Demann ou anrejistre avèk siksè! Nou pral aktive aksè ou rapidman."
                                    isSuccessMessage = true
                                    transactionRef = ""
                                },
                                onError = { err ->
                                    formMessage = err
                                    isSuccessMessage = false
                                }
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("submit_request_button"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PitchGreen,
                            contentColor = PitchDarker
                        )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Voye Demann Abònman",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Action Button: Direct WhatsApp confirmation
                    OutlinedButton(
                        onClick = {
                            openWhatsApp(
                                context = context,
                                name = customerName,
                                phone = phone,
                                plan = planLabel,
                                method = selectedMethod,
                                ref = transactionRef
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("whatsapp_confirm_button"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = PitchGreenLight
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = androidx.compose.ui.graphics.SolidColor(PitchGreen)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = null,
                            tint = PitchGreenLight,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Konfime sou WhatsApp",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Status message
                    if (formMessage != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSuccessMessage) Color(0xFF183D29) else Color(0xFF422222))
                                .padding(10.dp)
                        ) {
                            Text(
                                text = formMessage ?: "",
                                color = if (isSuccessMessage) PitchGreenLight else Color(0xFFFF8A80),
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }

        // Quick VIP Activation Section (Passcode or Direct Demo toggle)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CardBorder, RoundedCornerShape(14.dp)),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = CardPitchGreen)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.WorkspacePremium,
                            contentDescription = null,
                            tint = TrophyGold,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Ou gen yon Kòd VIP oswa Pas Aktivasyon?",
                            color = TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = vipCodeInput,
                            onValueChange = { vipCodeInput = it },
                            placeholder = { Text("Kòd VIP (eg: VIP2025)", fontSize = 12.sp) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("vip_code_input"),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true,
                            colors = outlinedColors()
                        )

                        Button(
                            onClick = {
                                val code = vipCodeInput.trim().uppercase()
                                if (code == "VIP2025" || code == "VIPHAITI" || code == "FPH2025" || code == "2468") {
                                    viewModel.toggleVip(true)
                                    Toast.makeText(context, "Espas VIP debloke avèk siksè!", Toast.LENGTH_LONG).show()
                                    onNavigateToPremium()
                                } else {
                                    Toast.makeText(context, "Kòd VIP sa a pa valab.", Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = TrophyGold,
                                contentColor = PitchDarker
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("apply_vip_code_btn")
                        ) {
                            Text("Aktive", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Mòd tès / demonstrasyon:",
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                        Text(
                            text = if (isVipUnlocked) "Fèmen aksè VIP" else "Debloke VIP dirèk",
                            color = PitchGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clickable {
                                    viewModel.toggleVip(!isVipUnlocked)
                                    val msg = if (!isVipUnlocked) "VIP debloke pou tès!" else "VIP fèmen."
                                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                }
                                .padding(4.dp)
                        )
                    }
                }
            }
        }

        // Requests History
        if (requests.isNotEmpty()) {
            item {
                Text(
                    text = "📋 ISTORIK DEMANN OU YO",
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            items(requests, key = { it.id }) { req ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = CardElevated)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "${req.planPrice} • ${req.paymentMethod}",
                                color = TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Ref: ${req.referenceId} • ${req.createdAt}",
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        }

                        val statusColor = when (req.status) {
                            "Apwouve" -> PitchGreenLight
                            "Rejte" -> Color(0xFFFF5252)
                            else -> TrophyGold
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(statusColor.copy(alpha = 0.2f))
                                .border(1.dp, statusColor, RoundedCornerShape(12.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = req.status,
                                color = statusColor,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PlanSelectChip(
    label: String,
    sub: String,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) PitchGreen else CardElevated)
            .border(1.dp, if (isSelected) PitchGreen else CardBorder, RoundedCornerShape(10.dp))
            .clickable(onClick = onSelect)
            .padding(vertical = 10.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = label,
                color = if (isSelected) PitchDarker else TextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = sub,
                color = if (isSelected) PitchDarker.copy(alpha = 0.8f) else TextMuted,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
private fun PaymentMethodChip(
    name: String,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) TrophyGold else CardElevated)
            .border(1.dp, if (isSelected) TrophyGold else CardBorder, RoundedCornerShape(8.dp))
            .clickable(onClick = onSelect)
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name,
            color = if (isSelected) PitchDarker else TextPrimary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
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

private fun copyToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Football Prono Payment", text)
    clipboard.setPrimaryClip(clip)
}

private fun openWhatsApp(
    context: Context,
    name: String,
    phone: String,
    plan: String,
    method: String,
    ref: String
) {
    val msg = """
Bonjou Football Prono Haiti! Mwen fè yon transfè pou abònman Premium.
Non: ${name.ifBlank { "Kliyan" }}
Telefòn: ${phone.ifBlank { "Pa endike" }}
Plan: $plan
Metòd: $method
Referans ID: ${ref.ifBlank { "An atant" }}
Mèsi pou aktivasyon an!
    """.trimIndent()

    val encoded = URLEncoder.encode(msg, StandardCharsets.UTF_8.toString())
    val uri = Uri.parse("https://api.whatsapp.com/send?phone=50938444413&text=$encoded")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "WhatsApp pa enstale sou aparèy sa a.", Toast.LENGTH_SHORT).show()
    }
}
