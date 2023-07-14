package com.udacity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

const val STATUS = "STATUS"
const val FILE_NAME = "FILE"

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        fileName.text = intent.getStringExtra(FILE_NAME)
        status_tv.text = intent.getStringExtra(STATUS)
        fab.setOnClickListener {
            finish()
        }

    }

}