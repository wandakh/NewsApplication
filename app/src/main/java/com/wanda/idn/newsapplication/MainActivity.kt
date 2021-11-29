package com.wanda.idn.newsapplication

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.wanda.idn.newsapplication.adapter.NewsAdapter
import com.wanda.idn.newsapplication.adapter.OnItemClickCallback
import com.wanda.idn.newsapplication.databinding.ActivityMainBinding
import com.wanda.idn.newsapplication.model.ArticlesItem
import com.wanda.idn.newsapplication.model.ResponseNews
import com.wanda.idn.newsapplication.service.RetrofitConfig
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import javax.security.auth.callback.Callback

class MainActivity : AppCompatActivity(), View.OnClickListener {

    val date = getCurretDateTime()
    var refUsers: DatabaseReference? = null
    val firebaseUser: FirebaseUser? = null
    lateinit var mainBinding: ActivityMainBinding

    private fun getCurretDateTime(): Date {
        return Calendar.getInstance().time
    }

    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        supportActionBar?.hide()
        mainBinding.apply {
            ibProfileMain.setOnClickListener(this@MainActivity)
            tvDateMain.text = date.toString("dd/MM/yyyy")
        }
//        mainBinding.ibProfileMain.setOnClickListener(this)
//        mainBinding.tvDateMain.text = date.toString("dd/MM/yyyy")
        getNews()


    }

    private fun getNews() {
        val country = "id"
        val apiKey = "9d97a9bc85b54dc8804f9ef68e5d3c5c"

        val loading = ProgressDialog.show(this, "Request Data", "Loading...")
        RetrofitConfig.getiInstance().getNewsHeadLine(country, apiKey).enqueue(
            object : retrofit2.Callback<ResponseNews> {
                override fun onResponse(
                    call: Call<ResponseNews>,
                    response: Response<ResponseNews>
                ) {
                    Log.d("Response", "Succses" + response.body()?.articles)
                    loading.dismiss()
                    if (response.isSuccessful) {
                        val status = response.body()?.status
                        if (status.equals("ok")) {
                            Toast.makeText(this@MainActivity, "Data Success!", Toast.LENGTH_SHORT)
                                .show()
                            val newsData = response.body()?.articles
                            val newsAdapter = NewsAdapter(this@MainActivity, newsData)
                            newsAdapter.setOnItemClickCallback(object : OnItemClickCallback{
                                override fun onItemClicked(news: ArticlesItem) {
                                    val intent = Intent(this@MainActivity, DetailActivity::class.java)
                                    intent.putExtra(DetailActivity.EXTRA_NEWS, news)
                                    startActivity(intent)
                                }
                            })


                            mainBinding.rvMain.apply {
                                adapter = newsAdapter
                                layoutManager = LinearLayoutManager(this@MainActivity)
                                val dataHighlight = response.body()
                                Glide.with(this@MainActivity).load(
                                    dataHighlight?.articles?.component4()?.urlToImage
                                ).centerCrop().into(mainBinding.ivMainBanner)

                                mainBinding.apply {
                                    tvHighlight.text = dataHighlight?.articles?.component4()?.title
                                    tvDateHighlight.text = dataHighlight?.articles?.component4()?.publishedAt
                                    tvNameAuthor.text = dataHighlight?.articles?.component4()?.author
                                }
                            }
                        } else {
                            Toast.makeText(this@MainActivity, "Data Failed", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }
                }

                override fun onFailure(call: Call<ResponseNews>, t: Throwable) {
                    Log.d("Response", "Failed : ") + t.localizedMessage
                    loading.dismiss()
                }

            }
        )

    }

    companion object {
        fun getLaunchService(from: Context) = Intent(from, MainActivity::class.java).apply {
            addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                        or Intent.FLAG_ACTIVITY_CLEAR_TASK
            )
        }
    }

    override fun onClick(p0: View) {
        when(p0.id){
            R.id.ib_profile_main ->
                startActivity(Intent(ProfileActivity.getLaunchService(this)))
        }
    }
}