package com.apex.codeassesment.data.remote

import com.apex.codeassesment.data.model.User
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject

// TODO (2 points): Add tests
class RemoteDataSource @Inject constructor() {
    private val client: OkHttpClient
    private val retrofit: Retrofit
    private val apiService: ApiService

    init {
        client = OkHttpClient.Builder().build()
        retrofit = Retrofit.Builder().baseUrl("https://randomuser.me/").client(client)
            .addConverterFactory(MoshiConverterFactory.create()).build()
        apiService = retrofit.create(ApiService::class.java)
    }

    // TODO (5 points): Load data from endpoint https://randomuser.me/api
    fun loadUser(callback: RemoteDataSourceCallback<User>) {
        apiService.loadUser().enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        callback.onSuccess(user)
                    } else {
                        callback.onError("Failed to parse response")
                    }
                } else {
                    callback.onError("Request failed with code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                callback.onError(t.message ?: "Request failed")
            }
        })
    }

    // TODO (3 points): Load data from endpoint https://randomuser.me/api?results=10
    // TODO (Optional Bonus: 3 points): Handle succes and failure from endpoints
    fun loadUsers(callback: RemoteDataSourceCallback<List<User>>) {
        apiService.loadUsers(10).enqueue(object : Callback<ApiUsersResponse> {
            override fun onResponse(
                call: Call<ApiUsersResponse>, response: Response<ApiUsersResponse>
            ) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    val users = apiResponse?.results
                    if (users != null) {
                        callback.onSuccess(users)
                    } else {
                        callback.onError("Failed to parse response")
                    }
                } else {
                    callback.onError("Request failed with code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ApiUsersResponse>, t: Throwable) {
                callback.onError(t.message ?: "Request failed")
            }
        })
    }

    private interface ApiService {
        @GET("api")
        fun loadUser(): Call<User>

        @GET("api")
        fun loadUsers(@Query("results") count: Int): Call<ApiUsersResponse>
    }

    data class ApiUsersResponse(val results: List<User>)
}

interface RemoteDataSourceCallback<T> {
    fun onSuccess(data: T)
    fun onError(errorMessage: String)
}