package com.example.top10downloaderapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.URL

class Top100AppActivity : AppCompatActivity() {
    lateinit var rvMain: RecyclerView
    lateinit var btGetData: Button
    var appsDetails = ArrayList<Details>()
    val rssURL =
        "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=100/xml"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top100_app)
        rvMain = findViewById(R.id.rvMain)
        btGetData = findViewById(R.id.btGetData)
        btGetData.setOnClickListener {
            requestApi(rssURL)

        }
    }

    fun requestApi(url: String) {

        CoroutineScope(Dispatchers.IO).launch {

            val rssFeed = async {

                getXMLData(url)

            }.await()

            if (rssFeed.isEmpty()) {
                Log.e("TAG", "requestApi fun: Error downloading")
            } else {
                withContext(Dispatchers.Main) {
                    rvMain.adapter = RecyclerViewAdapter(appsDetails)
                    rvMain.layoutManager = LinearLayoutManager(this@Top100AppActivity)
                }

            }

        }
    }

    fun getXMLData(urlPath: String?): ArrayList<Details> {
        val parser = XMLParser()

        val url = URL(urlPath)
        val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
        val response = connection.responseCode
        Log.d("TAG", "downloadXML: The response code was $response")

        appsDetails =
            connection.getInputStream()?.let {
                parser.parse(it)
            }
                    as ArrayList<Details>


        return appsDetails

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.top10 -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.top100 -> {
                val intent = Intent(this, Top100AppActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

