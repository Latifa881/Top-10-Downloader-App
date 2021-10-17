package com.example.top10downloaderapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class MainActivity : AppCompatActivity() {
    lateinit var rvMain: RecyclerView
    lateinit var btGetData: Button
    var appsDetails = ArrayList<Details>()
    val rssURL = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rvMain = findViewById(R.id.rvMain)
        btGetData = findViewById(R.id.btGetData)
        btGetData.setOnClickListener {
            requestApi(rssURL)

            rvMain.adapter = RecyclerViewAdapter(appsDetails)
            rvMain.layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    fun requestApi(url: String) {

        CoroutineScope(Dispatchers.IO).launch {


            val rssFeed = async {

                getXMLData(url)

            }.await()

            if (rssFeed.isEmpty()) {
                Log.e("TAG", "requestApi fun: Error downloading")
            }

        }
    }
    fun getXMLData(urlPath: String?): String {
        val xmlResult = StringBuilder()
        val parser = XMLParser()

        try {
            val url = URL(urlPath)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            val response = connection.responseCode
            Log.d("TAG", "downloadXML: The response code was $response")

            appsDetails =
                connection.getInputStream()?.let {
                    parser.parse(it)
                }
                        as ArrayList<Details>

            Log.d("TAG", "Received ${xmlResult.length} bytes")
            return xmlResult.toString()

        } catch (e: Exception) {
            Log.e("TAG", "downloadXML: Invalid URL ${e.message}")
        }
        return ""
    }
}




