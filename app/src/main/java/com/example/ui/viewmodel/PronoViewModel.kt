package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.PaymentInfo
import com.example.data.local.SettingsManager
import com.example.data.model.MatchItem
import com.example.data.model.SubscriptionRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PronoViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application, viewModelScope)
    private val matchDao = database.matchDao()
    private val subscriptionDao = database.subscriptionDao()
    private val settingsManager = SettingsManager(application)

    val publicMatches: StateFlow<List<MatchItem>> = matchDao.getPublicMatches()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val premiumMatches: StateFlow<List<MatchItem>> = matchDao.getPremiumMatches()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allMatches: StateFlow<List<MatchItem>> = matchDao.getAllMatches()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val subscriptionRequests: StateFlow<List<SubscriptionRequest>> = subscriptionDao.getAllRequests()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _paymentInfo = MutableStateFlow(settingsManager.getPaymentInfo())
    val paymentInfo: StateFlow<PaymentInfo> = _paymentInfo.asStateFlow()

    private val _isAdminLoggedIn = MutableStateFlow(false)
    val isAdminLoggedIn: StateFlow<Boolean> = _isAdminLoggedIn.asStateFlow()

    private val _adminLoginError = MutableStateFlow<String?>(null)
    val adminLoginError: StateFlow<String?> = _adminLoginError.asStateFlow()

    private val _isVipUnlocked = MutableStateFlow(settingsManager.isVipUnlocked())
    val isVipUnlocked: StateFlow<Boolean> = _isVipUnlocked.asStateFlow()

    // Ticket matches (Bet Ticket Simulator)
    private val _ticketMatches = MutableStateFlow<List<MatchItem>>(emptyList())
    val ticketMatches: StateFlow<List<MatchItem>> = _ticketMatches.asStateFlow()

    // Preselected plan when user clicks "Chwazi plan" on Home
    private val _selectedPlan = MutableStateFlow("500")
    val selectedPlan: StateFlow<String> = _selectedPlan.asStateFlow()

    // Admin match form state
    var editingMatchId: Long? = null
        private set
    val formHomeTeam = MutableStateFlow("")
    val formAwayTeam = MutableStateFlow("")
    val formLeague = MutableStateFlow("")
    val formMatchTime = MutableStateFlow("")
    val formPickAnalysis = MutableStateFlow("")
    val formPrediction = MutableStateFlow("1 — Viktwa ekip lakay")
    val formConfidence = MutableStateFlow("Wo")
    val formVisibility = MutableStateFlow("premium")
    val formOdds = MutableStateFlow("1.85")
    val formMessage = MutableStateFlow<String?>(null)

    fun selectPlan(planValue: String) {
        _selectedPlan.value = planValue
    }

    fun loginAdmin(pin: String): Boolean {
        val currentPin = settingsManager.getAdminPin()
        if (pin.trim() == currentPin) {
            _isAdminLoggedIn.value = true
            _adminLoginError.value = null
            return true
        } else {
            _adminLoginError.value = "Kòd la pa bon. Eseye ankò (Default: 2468)"
            return false
        }
    }

    fun logoutAdmin() {
        _isAdminLoggedIn.value = false
        clearForm()
    }

    fun updateAdminPin(newPin: String) {
        if (newPin.isNotBlank() && newPin.length >= 4) {
            settingsManager.setAdminPin(newPin)
        }
    }

    fun updatePaymentSettings(monCash: String, natCash: String, binance: String) {
        val updated = PaymentInfo(monCash.trim(), natCash.trim(), binance.trim())
        settingsManager.savePaymentInfo(updated)
        _paymentInfo.value = updated
    }

    fun toggleVip(unlocked: Boolean) {
        settingsManager.setVipUnlocked(unlocked)
        _isVipUnlocked.value = unlocked
    }

    fun editMatch(match: MatchItem) {
        editingMatchId = match.id
        formHomeTeam.value = match.homeTeam
        formAwayTeam.value = match.awayTeam
        formLeague.value = match.league
        formMatchTime.value = match.matchTime
        formPickAnalysis.value = match.pickAnalysis
        formPrediction.value = match.prediction
        formConfidence.value = match.confidence
        formVisibility.value = match.visibility
        formOdds.value = match.odds.toString()
        formMessage.value = "Modifye analiz la epi peze 'Sove Chanjman'."
    }

    fun clearForm() {
        editingMatchId = null
        formHomeTeam.value = ""
        formAwayTeam.value = ""
        formLeague.value = ""
        formMatchTime.value = ""
        formPickAnalysis.value = ""
        formPrediction.value = "1 — Viktwa ekip lakay"
        formConfidence.value = "Wo"
        formVisibility.value = "premium"
        formOdds.value = "1.85"
        formMessage.value = null
    }

    fun insertTemplateText() {
        formPickAnalysis.value = "Fòm dènye match yo:\n\nAnaliz atak ak defans:\n\nEstatistik kle:\n\nPronostic mwen:\n\nNòt pèsonèl:"
    }

    fun saveMatch(onSuccess: () -> Unit, onError: (String) -> Unit) {
        val home = formHomeTeam.value.trim()
        val away = formAwayTeam.value.trim()
        val pick = formPickAnalysis.value.trim()

        if (home.isEmpty() || away.isEmpty() || pick.isEmpty()) {
            onError("Tanpri antre non toude ekip yo ak analiz ou.")
            return
        }

        val parsedOdds = formOdds.value.toDoubleOrNull() ?: 1.85
        val timeNow = SimpleDateFormat("d MMM, HH:mm", Locale.getDefault()).format(Date())

        viewModelScope.launch {
            val item = MatchItem(
                id = editingMatchId ?: 0L,
                homeTeam = home,
                awayTeam = away,
                league = formLeague.value.trim().ifEmpty { "Entènasyonal" },
                matchTime = formMatchTime.value.trim().ifEmpty { "Jodi a" },
                pickAnalysis = pick,
                prediction = formPrediction.value,
                confidence = formConfidence.value,
                visibility = formVisibility.value,
                updatedAt = timeNow,
                odds = parsedOdds
            )

            if (editingMatchId != null) {
                matchDao.update(item)
            } else {
                matchDao.insert(item)
            }

            clearForm()
            formMessage.value = "Analiz ou anrejistre avèk siksè!"
            onSuccess()
        }
    }

    fun deleteMatch(id: Long) {
        viewModelScope.launch {
            matchDao.deleteById(id)
            removeFromTicket(id)
        }
    }

    fun submitSubscriptionRequest(
        name: String,
        phone: String,
        planPrice: String,
        planDuration: String,
        method: String,
        ref: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val trimmedName = name.trim()
        val trimmedPhone = phone.trim()
        val trimmedRef = ref.trim()

        if (trimmedName.isEmpty() || trimmedPhone.isEmpty() || trimmedRef.isEmpty()) {
            onError("Tanpri ranpli non ou, telefòn WhatsApp ak referans tranzaksyon an.")
            return
        }

        val timeNow = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        val req = SubscriptionRequest(
            customerName = trimmedName,
            phone = trimmedPhone,
            planPrice = planPrice,
            planDuration = planDuration,
            paymentMethod = method,
            referenceId = trimmedRef,
            status = "An atant",
            createdAt = timeNow
        )

        viewModelScope.launch {
            subscriptionDao.insert(req)
            onSuccess()
        }
    }

    fun updateRequestStatus(id: Long, newStatus: String) {
        viewModelScope.launch {
            subscriptionDao.updateStatus(id, newStatus)
        }
    }

    fun addToTicket(match: MatchItem) {
        if (_ticketMatches.value.none { it.id == match.id }) {
            _ticketMatches.value = _ticketMatches.value + match
        }
    }

    fun removeFromTicket(matchId: Long) {
        _ticketMatches.value = _ticketMatches.value.filter { it.id != matchId }
    }

    fun clearTicket() {
        _ticketMatches.value = emptyList()
    }
}
