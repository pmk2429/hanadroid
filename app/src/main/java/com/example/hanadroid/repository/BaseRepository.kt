package com.example.hanadroid.repository

import com.example.hanadroid.networking.ErrorResponse
import com.example.hanadroid.networking.NetworkResult
import com.example.hanadroid.util.CoroutineDispatcherProvider
import com.squareup.moshi.Moshi
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import okio.IOException
import retrofit2.HttpException
import retrofit2.Response

abstract class BaseRepository(
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider
) {

    suspend fun <T> safeApiCall(
        apiToBeCalled: suspend () -> Response<T>
    ): NetworkResult<T> = withContext(coroutineDispatcherProvider.io()) {
        return@withContext try {
            val response: Response<T> = apiToBeCalled()
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
            NetworkResult.Error(code = e.code(), message = e.message!!)
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
