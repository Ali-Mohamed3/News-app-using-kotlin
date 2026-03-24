package com.example.newsapp

import retrofit2.Call
import retrofit2.http.GET

interface NewsCallable {
    @GET("v2/top-headlines?country=us&category=general&apiKey=976a669174e74c3c8dac16d4496f6829&pageSize=30")
    fun getNews(): Call<News>
}