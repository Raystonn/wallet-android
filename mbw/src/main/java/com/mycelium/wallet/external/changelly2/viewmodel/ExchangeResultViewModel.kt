package com.mycelium.wallet.external.changelly2.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.mycelium.wallet.MbwManager
import com.mycelium.wallet.R
import com.mycelium.wallet.WalletApplication
import com.mycelium.wallet.activity.util.toStringFriendlyWithUnit
import com.mycelium.wallet.external.changelly.model.ChangellyTransaction
import java.math.BigDecimal
import java.text.DateFormat
import java.util.*


class ExchangeResultViewModel : ViewModel() {
    val mbwManager = MbwManager.getInstance(WalletApplication.getInstance())
    val spendValue = MutableLiveData<String>()
    val spendValueFiat = MutableLiveData<String>()
    val getValue = MutableLiveData<String>()
    val getValueFiat = MutableLiveData<String>()
    val txId = MutableLiveData<String>()
    val date = MutableLiveData<String>()
    val fromAddress = MutableLiveData("")
    val toAddress = MutableLiveData("")
    val trackLink = MutableLiveData("")
    val trackLinkText = MutableLiveData("")
    val isExchangeComplete = MutableLiveData(false)

    val more = MutableLiveData(true)
    val moreText = Transformations.map(more) {
        WalletApplication.getInstance().getString(
                if (it) {
                    R.string.show_transaction_details
                } else {
                    R.string.show_transaction_details_hide
                })
    }

    fun setTransaction(result: ChangellyTransaction) {
        txId.value = result.id
        spendValue.value = "${result.amountExpectedFrom} ${result.currencyFrom.toUpperCase()}"
        getValue.value = "${result.amountExpectedTo} ${result.currencyTo.toUpperCase()}"
        date.value = DateFormat.getDateInstance(DateFormat.LONG).format(Date(result.createdAt * 1000L))
        spendValueFiat.value = getFiatValue(result.amountExpectedFrom, result.currencyFrom)
        getValueFiat.value = getFiatValue(result.amountExpectedTo, result.currencyTo)
        isExchangeComplete.value = result.status == "finished"
        val showLink = result.status in arrayOf("finished", "refunded", "failed", "expired", "hold")
        trackLink.value = if (showLink) result.trackUrl else ""
        trackLinkText.value =
                if (showLink) WalletApplication.getInstance().getString(R.string.link_to_track_transaction)
                else WalletApplication.getInstance().getString(R.string.exchanging).capitalize()
    }

    private fun getFiatValue(amount: BigDecimal?, currency: String) =
            mbwManager.getWalletManager(false).getAssetTypes()
                    .firstOrNull { it.symbol.equals(currency, true) }
                    ?.let {
                        amount?.let { amount ->
                            mbwManager.exchangeRateManager
                                    .get(it.value(amount.toPlainString()), mbwManager.getFiatCurrency(it))
                                    ?.toStringFriendlyWithUnit()?.let { "≈$it" }
                        }
                    } ?: ""

}