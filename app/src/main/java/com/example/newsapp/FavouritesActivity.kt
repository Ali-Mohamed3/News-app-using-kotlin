package com.example.newsapp

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.example.newsapp.databinding.ActivityFavouritesBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FavouritesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavouritesBinding
    private  var db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFavouritesBinding.inflate(layoutInflater)



        setContentView(binding.root)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        loadNews()
        binding.swipeFavourites.setOnRefreshListener {
            loadNews()
        }


    }
    val news: ArrayList<Article> = ArrayList<Article>()
    private fun loadNews(){
        db.collection("news")
            .get()
            .addOnSuccessListener { result ->
                for(document in result){
                    val article = document.toObject<Article>()
                    news.add(article)
                }
                binding.progressFav.isVisible = false
                binding.swipeFavourites.isRefreshing = false

                showNews(news)
            }

    }
    private fun showNews(articles: ArrayList<Article>){
        val adapter = NewsAdapter(this,articles)
        binding.newsRvFav.adapter = adapter
    }
}