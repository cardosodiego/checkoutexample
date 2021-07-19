package com.mercadolivre.checkoutexample.ui


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mercadolivre.checkoutexample.R
import com.mercadolivre.checkoutexample.ui.fragment.MainFragment


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.content, MainFragment.newInstance())
                .commitNow()
        }
    }
    companion object{
        const val REQ_CODE_CHECKOUT = 1
    }
}