package com.example.hanadroid.networking.dispatcher

import com.example.hanadroid.networking.ErrorResponse
import com.example.hanadroid.networking.NetworkResult
import com.squareup.moshi.Moshi
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class NetworkOperationTask<T>(
    private val apiToBeCalled: suspend () -> Response<T>
) : suspend () -> NetworkResult<T> {

    override suspend fun invoke(): NetworkResult<T> {
        return try {
            val response = apiToBeCalled.invoke()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                NetworkResult.Success(data = body)
            } else {
                val errorResponse: ErrorResponse? = convertErrorBody(response.errorBody())
                NetworkResult.Error(
                    response.code(),
                    errorResponse?.failureMessage ?: "Something went wrong"
                )
            }
        } catch (e: HttpException) {
            NetworkResult.Error(code = e.code(), message = e.message ?: "HTTP exception")
        } catch (e: IOException) {
            NetworkResult.Exception(e)
        } catch (e: Exception) {
            NetworkResult.Exception(e)
        }
    }

    private fun convertErrorBody(errorBody: ResponseBody?): ErrorResponse? {
        return try {
            errorBody?.source()?.let {
                val moshiAdapter = Moshi.Builder().build().adapter(ErrorResponse::class.java)
                moshiAdapter.fromJson(it)
            }
        } catch (exception: Exception) {
            null
        }
    }
}

