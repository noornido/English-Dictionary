package com.example.dictionaryapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.dictionaryapp.databinding.ActivityMainBinding
import org.json.JSONArray

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val KEY = "DEFINITION"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val queue = Volley.newRequestQueue(this)


        binding.findButton.setOnClickListener{

            val stringRequest = StringRequest(Request.Method.GET, getURL(),
                Response.Listener { response ->
                    try{
                        extractDefinitionFromJSON(response)
                    }catch(exception : Exception){
                        exception.printStackTrace()
                    }
                },
               Response.ErrorListener { error ->
                   Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
                }
            )

            queue.add(stringRequest)

        }

    }

    private fun getURL() : String{

        val word = binding.wordEditText.text.toString()
        //val apiKey = "73bb94b8-b60d-4613-ad0a-fa7884fe27ef"
        //val url = "https://www.dictionaryapi.com/api/v3/references/learners/json/$word?key=$apiKey"
        val url = "https://api.dictionaryapi.dev/api/v2/entries/en/$word"



        return url
    }


    private fun extractDefinitionFromJSON(response: String): String{

        val jsonArray = JSONArray(response)
        val firstIndexOfJsonArray = jsonArray.getJSONObject(0)
        val meanings = firstIndexOfJsonArray.getJSONArray("meanings")
        val firstObjectInMeaningsArray = meanings.getJSONObject(0)
        val definition = firstObjectInMeaningsArray.getJSONArray("definitions").getJSONObject(0).getString("definition")



        val intent = Intent(this, DefinitionActivity::class.java)
        intent.putExtra(KEY, definition)
        startActivity(intent)

        return definition

    }


}

