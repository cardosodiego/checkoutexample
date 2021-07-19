package com.mercadolivre.checkoutexample.viewmodel

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadolivre.checkoutexample.model.*
import com.mercadolivre.checkoutexample.services.CheckoutPreferenceServiceImpl
import com.mercadolivre.checkoutexample.services.OperationResult
import com.mercadolivre.checkoutexample.util.ConfigurationUtils
import com.mercadolivre.checkoutexample.util.Credentials
import com.mercadopago.android.px.core.MercadoPagoCheckout
import com.mercadopago.android.px.model.PaymentTypes
import kotlinx.coroutines.launch
import java.math.BigDecimal

class MainViewModel : ViewModel() {


    private val _checkoutBuilder = MutableLiveData<MercadoPagoCheckout.Builder>()
    val checkoutBuilder: MutableLiveData<MercadoPagoCheckout.Builder>
        get() = _checkoutBuilder

    var excludeCreditCard: Boolean = false
    var excludeDebitCard: Boolean = false
    var useCustomPaymentProcessor: Boolean = false
    var useVisualProcessor: Boolean = false
    var addChargeToCreditCard: Boolean = false
    var useCustomStrings: Boolean = false
    var useDialogOnHeaderTap: Boolean = false
    var useCustomTopAndBottomFragmentsOnResultScreen: Boolean = false

    fun createCheckoutBuilder() {
        viewModelScope.launch {
            val prefId = generatePreferenceId()
            if (prefId.isNotEmpty()) {
                val builder = if (useCustomPaymentProcessor){
                    MercadoPagoCheckout.Builder(
                        Credentials.DEFAULT_MLA_COLLECTOR_PUBLIC_KEY,
                        prefId,
                        ConfigurationUtils.getPaymentConfiguration(useVisualProcessor,
                            addChargeToCreditCard))
                } else {
                    MercadoPagoCheckout.Builder(
                        Credentials.DEFAULT_MLA_COLLECTOR_PUBLIC_KEY, prefId)
                }.setAdvancedConfiguration(
                    ConfigurationUtils.getAdvancedConfiguration(
                        useCustomStrings,
                        useDialogOnHeaderTap,
                        useCustomTopAndBottomFragmentsOnResultScreen))
                    .setPrivateKey(Credentials.DEFAULT_MLA_PAYER_ACCESS_TOKEN)
                checkoutBuilder.postValue(builder)
            }
        }
    }

    private suspend fun generatePreferenceId(): String {
        val item = Item()
        val checkoutPrefDTO = CheckoutPreference("", listOf(item),
            Payer(Credentials.DEFAULT_MLA_PAYER_EMAIL),
            PaymentPreference(getExcludedPaymentTypes(), emptyList())
        )
        return when(val createRefResponse = CheckoutPreferenceServiceImpl()
            .create(checkoutPrefDTO, Credentials.DEFAULT_MLA_COLLECTOR_ACCESS_TOKEN)) {
            is OperationResult.Success -> {
                createRefResponse.value.id
            }
            else -> "" //TODO
        }
    }

    private fun getExcludedPaymentTypes(): List<PaymentType> {
        val excludedPaymentTypes = mutableListOf<PaymentType>()
        if(excludeCreditCard) {
            excludedPaymentTypes.add(PaymentType(PaymentTypes.CREDIT_CARD))
        }
        if(excludeDebitCard){
            excludedPaymentTypes.add(PaymentType(PaymentTypes.DEBIT_CARD))
        }
        return excludedPaymentTypes
    }

}