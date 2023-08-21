package com.applyplugin.movieseriesdatabase.di

import okhttp3.Interceptor
import okhttp3.Response

class CustomInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("accept", "application/json")
            .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1ZjcxYzQwYTliNjU4OWZiMGJmOTkyODg2NDZiYjMxYSIsInN1YiI6IjY0ZGY5ZGY4Yjc3ZDRiMTE0MjVmYjRiZiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.wuD9-LR8X3Od5zHnbf77ovvSIptt-A3eqDjAkxcN44I")
            .build()
        return chain.proceed(request)
    }
}
