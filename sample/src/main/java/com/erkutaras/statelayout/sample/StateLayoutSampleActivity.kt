package com.erkutaras.statelayout.sample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.erkutaras.statelayout.OnStateLayoutListener
import com.erkutaras.statelayout.StateLayout

class StateLayoutSampleActivity : AppCompatActivity(), OnStateLayoutListener {

    var stateLayout: StateLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_state_layout_sample)

        stateLayout = findViewById(R.id.stateLayout)
        stateLayout?.onStateLayoutListener = this

        findViewById<Button>(R.id.button_loading).setOnClickListener { stateLayout?.loading() }
        findViewById<Button>(R.id.button_content).setOnClickListener { stateLayout?.content() }
        findViewById<Button>(R.id.button_error).setOnClickListener { stateLayout?.error() }
    }

    override fun onErrorStateButtonClick() {
        Toast.makeText(this, "TRY AGAIN CLICK!", Toast.LENGTH_SHORT).show()
        stateLayout?.loading()
    }
}
