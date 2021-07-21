package com.mercadolivre.checkoutexample.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadolivre.checkoutexample.util.ConfigurationUtils
import com.mercadolivre.checkoutexample.util.Credentials
import com.mercadopago.android.px.core.MercadoPagoCheckout
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {


    private val _checkoutBuilder = MutableLiveData<MercadoPagoCheckout.Builder>()
    val checkoutBuilder: MutableLiveData<MercadoPagoCheckout.Builder>
        get() = _checkoutBuilder

    var useCustomPaymentProcessor: Boolean = false
    var useVisualProcessor: Boolean = false
    var addChargeToCreditCard: Boolean = false
    var useCustomStrings: Boolean = false
    var useDialogOnHeaderTap: Boolean = false
    var useCustomTopAndBottomFragmentsOnResultScreen: Boolean = false

    fun createCheckoutBuilder() {
        viewModelScope.launch {
            val builder = if (useCustomPaymentProcessor) {
                MercadoPagoCheckout.Builder(
                    Credentials.DEFAULT_MLA_COLLECTOR_PUBLIC_KEY,
                    "737302974-34e65c90-62ad-4b06-9f81-0aa08528ec53",
                    ConfigurationUtils.getPaymentConfiguration(
                        useVisualProcessor,
                        addChargeToCreditCard
                    )
                )
            } else {
                MercadoPagoCheckout.Builder(
                    Credentials.DEFAULT_MLA_COLLECTOR_PUBLIC_KEY,
                    "737302974-34e65c90-62ad-4b06-9f81-0aa08528ec53"
                )
            }.setAdvancedConfiguration(
                ConfigurationUtils.getAdvancedConfiguration(
                    useCustomStrings,
                    useDialogOnHeaderTap,
                    useCustomTopAndBottomFragmentsOnResultScreen
                )
            )
                .setPrivateKey(Credentials.DEFAULT_MLA_PAYER_ACCESS_TOKEN)
            checkoutBuilder.postValue(builder)
        }
    }
}
