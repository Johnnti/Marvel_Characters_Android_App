package com.example.hero_finder

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.codepath.asynchttpclient.RequestParams
import okhttp3.Headers


class MainActivity : AppCompatActivity() {

    private val params = RequestParams()
    private val pubKey = BuildConfig.MARVEL_PUBLIC_KEY
    private val priKey = BuildConfig.MARVEL_PRIVATE_KEY
    private val timestamp = System.currentTimeMillis().toString()
    private val stringToHash = timestamp + priKey + pubKey

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val inputText: EditText = findViewById(R.id.inputText)
        val imageView: ImageView = findViewById(R.id.imageView)
        val aboutSection: TextView = findViewById(R.id.textView2)
        val comic: TextView = findViewById(R.id.textView5)
        val searchButton: Button = findViewById(R.id.button)
        searchButton.setOnClickListener{
            findChar(inputText, imageView, aboutSection, comic)
        }
    }
    private fun findChar(inputText: EditText, imageView: ImageView, aboutSection: TextView, comic: TextView){
        val client = AsyncHttpClient()
        params["apikey"] = pubKey
        params["ts"] = timestamp
        params["hash"] = stringToHash.toMD5()
        params["name"] = inputText.text.toString()

        client["https://gateway.marvel.com/v1/public/characters", params, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.d("Success message", "$json")
                val results = json.jsonObject
                val imageUrl: String? = results.optJSONObject("data")
                    ?.optJSONArray("results")
                    ?.optJSONObject(0)
                    ?.optJSONObject("thumbnail")
                    ?.let{ thumbnail ->
                        val path = thumbnail.optString("path")
                        val extension = thumbnail.optString("extension")
                        "${path.replace("http://", "https://")}.$extension"
                    }

                Log.d("Results", "$imageUrl")


                val about: String? = results.optJSONObject("data")
                    ?.optJSONArray("results")
                    ?.optJSONObject(0)
                    ?.optString("description")

                Log.d("Description", "$about")

                val comics: String? = results.optJSONObject("data")
                    ?.optJSONArray("results")
                    ?.optJSONObject(0)
                    ?.optJSONObject("comics")
                    ?.optJSONArray("items")
                    ?.optJSONObject(0)
                    ?.optString("name")

                Log.d("Comics", "$comics")

                comic.text = comics
                aboutSection.text = about
                Glide.with(this@MainActivity).load(imageUrl).into(imageView)
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                println("This is the error message: $errorResponse")
            }
        }]
    }

}
