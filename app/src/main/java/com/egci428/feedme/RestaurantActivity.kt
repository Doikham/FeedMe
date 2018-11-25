package com.egci428.feedme

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_restaurant.*
import java.text.NumberFormat

class RestaurantActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val name = intent.getStringExtra("rname")
        resName.text = name

        val des = intent.getStringExtra("rdes")
        resDres.text = des

        val img = intent.getIntExtra("rimg", 0)
        resImg.setImageResource(img)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.getItemId()
        if (id == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
