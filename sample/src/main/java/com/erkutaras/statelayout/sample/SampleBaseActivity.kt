package com.erkutaras.statelayout.sample

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by erkutaras on 2.02.2019.
 */
abstract class SampleBaseActivity : AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(getMenuResId(), menu)
        return true
    }

    @MenuRes
    abstract fun getMenuResId(): Int

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_simple -> {
                startActivity(Intent(this, StateLayoutSampleActivity::class.java))
                return true
            }
            R.id.menu_custom -> {
                startActivity(Intent(this, CustomSampleActivity::class.java))
                return true
            }
            R.id.menu_loading_animation -> {
                startActivity(Intent(this, AnimationLoadingSampleActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}