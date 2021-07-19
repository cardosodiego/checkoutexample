package com.mercadolivre.checkoutexample.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mercadolivre.checkoutexample.R
import com.mercadolivre.checkoutexample.services.IPaymentService
import com.mercadopago.android.px.core.SplitPaymentProcessor


class CustomVisualPaymentFragment(
    private val paymentService: IPaymentService,
    private val checkoutData: SplitPaymentProcessor.CheckoutData) : Fragment() {

    private var onPaymentListener: SplitPaymentProcessor.OnPaymentListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_custom_visual_payment, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is SplitPaymentProcessor.OnPaymentListener){
            onPaymentListener = context
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        onPaymentListener = null
    }

    override fun onResume() {
        super.onResume()
        // Pronto para iniciar o pagamento
        val payment = paymentService.createPayment(checkoutData)
        view?.postDelayed({
            onPaymentListener?.onPaymentFinished(payment)
        }, DELAY_TIME_IN_MILI)
    }

    companion object {
      const val DELAY_TIME_IN_MILI = 2000L
    }
}