package com.mercadolivre.checkoutexample.services

import android.os.Parcelable
import com.mercadopago.android.px.core.SplitPaymentProcessor
import com.mercadopago.android.px.model.IParcelablePaymentDescriptor

interface IPaymentService: Parcelable {
    fun createPayment(checkoutData: SplitPaymentProcessor.CheckoutData) :
            IParcelablePaymentDescriptor
}