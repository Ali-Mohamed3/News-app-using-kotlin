package com.example.newsapp

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.example.newsapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    //
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadNews()
        binding.swipe.setOnRefreshListener {
            loadNews()
        }

    }
    private fun loadNews(){
        val retroFit = Retrofit
            .Builder()
            .baseUrl("https://newsapi.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val callable = retroFit.create(NewsCallable::class.java)
        callable.getNews().enqueue(object : Callback<News>{
            override fun onResponse(
                call: Call<News?>,
                response: Response<News?>
            ) {
               val news = response.body()
               val articles = news!!.articles
                Log.d("trace", "Data: $articles")
                articles.removeAll{
                    it.title == "[Removed]"
                }
                showNews(articles)
                binding.progress.isVisible = false
                binding.swipe.isRefreshing = false
            }

            override fun onFailure(
                call: Call<News?>,
                t: Throwable
            ) {
                Log.d("trace", "Error: ${t.message}")
                binding.progress.isVisible = false
            }
        })
    }
    private fun showNews(articles: ArrayList<Article>){
        val adapter = NewsAdapter(this,articles)
        binding.newsRv.adapter = adapter
    }
}