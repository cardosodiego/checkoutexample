package com.mercadolivre.checkoutexample.services

import com.mercadolivre.checkoutexample.model.CheckoutPreference
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import java.io.IOException


interface CheckoutPreferenceAPI {
    @POST("/checkout/preferences")
    suspend fun create(@Body body: CheckoutPreference,
        @Header("Authorization") token: String): CheckoutPreference
}

interface  CheckoutPreferenceService {
    val retrofitService: CheckoutPreferenceAPI
}

class CheckoutPreferenceServiceImpl: CheckoutPreferenceService {

    companion object {
        private const val BASE_URL = "https://api.mercadopago.com"

        private val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
    }

    override val retrofitService: CheckoutPreferenceAPI by lazy {
        retrofit.create(CheckoutPreferenceAPI::class.java)
    }

    suspend fun create(
        body: CheckoutPreference,
        accessToken: String
    ): OperationResult<CheckoutPreference> {
        return safeApiCall(Dispatchers.IO) {retrofitService.create(body, doToken(accessToken))}
    }

    private fun doToken(accessToken: String): String {
        return "Bearer $accessToken"
    }

    private suspend fun <T> safeApiCall(
        dispatcher: CoroutineDispatcher,
        apiCall: suspend() -> T
    ): OperationResult<T> {
        return withContext(dispatcher) {
            try {
                OperationResult.Success(apiCall.invoke())
            }  catch (throwable: Throwable){
                when(throwable){
                    is IOException -> OperationResult.NetworkError
                    is HttpException -> {
                        val code = throwable.code()
                        val errorResponse = throwable.response()?.errorBody()?.toString()
                        OperationResult.GenericError(code, errorResponse)
                    }
                    else -> OperationResult.GenericError(null, null)
                }
            }
        }
    }
}