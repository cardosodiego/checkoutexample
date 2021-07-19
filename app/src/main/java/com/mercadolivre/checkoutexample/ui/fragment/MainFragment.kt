package com.mercadolivre.checkoutexample.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.switchmaterial.SwitchMaterial
import com.mercadolivre.checkoutexample.R
import com.mercadolivre.checkoutexample.ui.MainActivity
import com.mercadolivre.checkoutexample.viewmodel.MainViewModel
import com.mercadopago.android.px.core.CheckoutLazyInit
import com.mercadopago.android.px.core.MercadoPagoCheckout


class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var excludeCreditCard: MaterialCheckBox
    private lateinit var excludeDebitCard: MaterialCheckBox
    private lateinit var useCustomPayment: SwitchMaterial
    private lateinit var useCustomLoading: SwitchMaterial
    private lateinit var addChargeCredit: SwitchMaterial
    private lateinit var useStringCustomized: MaterialCheckBox
    private lateinit var useDialogImage: MaterialCheckBox
    private lateinit var useCustomScreen: MaterialCheckBox
    private lateinit var btnStartCheckout: Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        setViewAccessors(view)
        setExcludeCardCheckBoxListener()
        setPaymentConfigurationListeners()
        setAdvancedConfigurations()
        setBtnStartCheckout()
        setStartCheckoutObserver()
    }

    private fun setViewAccessors(view: View) {
        with(view){
            excludeCreditCard = findViewById(R.id.exclude_credit_card)
            excludeDebitCard = findViewById(R.id.exclude_debit_card)
            useCustomPayment = findViewById(R.id.switch_custom_payment)
            useCustomLoading = findViewById(R.id.switch_custom_loading)
            addChargeCredit = findViewById(R.id.switch_charge_credit)
            useStringCustomized = findViewById(R.id.use_custom_string)
            useDialogImage = findViewById(R.id.use_dialog_header)
            useCustomScreen  = findViewById(R.id.use_custom_fragment_payment_result)
            btnStartCheckout = findViewById(R.id.btn_start_checkout)
        }
    }

    private fun setExcludeCardCheckBoxListener() {
        excludeCreditCard.setOnCheckedChangeListener{_, isChecked ->
            viewModel.excludeCreditCard = isChecked
        }
        excludeDebitCard.setOnCheckedChangeListener{_, isChecked ->
            viewModel.excludeDebitCard = isChecked
        }
    }

    private fun setPaymentConfigurationListeners() {
        useCustomPayment.setOnCheckedChangeListener { _, isChecked ->
            viewModel.useCustomPaymentProcessor = isChecked
            useCustomLoading.isChecked = false
            useCustomLoading.isEnabled = isChecked
            addChargeCredit.isChecked = false
            addChargeCredit.isEnabled = isChecked
        }
        useCustomLoading.setOnCheckedChangeListener { _, isChecked ->
            viewModel.useVisualProcessor = isChecked
        }
        addChargeCredit.setOnCheckedChangeListener { _, isChecked ->
            viewModel.addChargeToCreditCard = isChecked
        }

    }

    private fun setAdvancedConfigurations() {
        useStringCustomized.setOnCheckedChangeListener { _, isChecked ->
            viewModel.useCustomStrings = isChecked
        }
        useDialogImage.setOnCheckedChangeListener { _, isChecked ->
            viewModel.useDialogOnHeaderTap = isChecked
        }
        useCustomScreen.setOnCheckedChangeListener { _, isChecked ->
            viewModel.useCustomTopAndBottomFragmentsOnResultScreen = isChecked
        }
    }

    // start do checkout
    private fun setBtnStartCheckout() {
       btnStartCheckout.setOnClickListener {
           viewModel.createCheckoutBuilder()
       }
    }

    // observer
    private fun setStartCheckoutObserver() {
        viewModel.checkoutBuilder.observe(viewLifecycleOwner, {
            val lazyInit: CheckoutLazyInit = object : CheckoutLazyInit(it) {
                override fun fail(mercadoPagoCheckout: MercadoPagoCheckout) {
                    mercadoPagoCheckout.startPayment(
                        requireActivity(),
                        MainActivity.REQ_CODE_CHECKOUT
                    )
                }

                override fun success(mercadoPagoCheckout: MercadoPagoCheckout) {
                    mercadoPagoCheckout.startPayment(
                        requireActivity(),
                        MainActivity.REQ_CODE_CHECKOUT
                    )
                }
            }
            lazyInit.fetch(requireContext())
        })
    }

}