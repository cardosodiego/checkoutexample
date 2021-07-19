package com.mercadolivre.checkoutexample.model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class Item(var title: String = "Amazon Echo Dot",
                @SerializedName("unit_price") var unitPrice: BigDecimal = BigDecimal(280),
                var quantity: Int = 1)

