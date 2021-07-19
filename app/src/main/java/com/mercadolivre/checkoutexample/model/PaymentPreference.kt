package com.mercadolivre.checkoutexample.model

import com.google.gson.annotations.SerializedName

data class PaymentPreference(
    @SerializedName("excluded_payment_types")
    val excludedPaymentTypes: List<PaymentType>,
    @SerializedName("excluded_payment_methods")
    val excludedPaymentMethod: List<PaymentMethod>
)

data class PaymentType(val id: String)
data class PaymentMethod(val id: String)
