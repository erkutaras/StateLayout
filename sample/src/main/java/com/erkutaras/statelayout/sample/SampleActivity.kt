package com.erkutaras.statelayout.sample

import android.os.Bundle
import androidx.annotation.StringRes
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_sample.*

class SampleActivity : AppCompatActivity() {

    private val simpleFragment = SimpleFragment.newInstance()
    private val customFragment = CustomFragment.newInstance()

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                title = updateTitle(R.string.title_simple)
                showFragment(simpleFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                title = updateTitle(R.string.title_custom)
                showFragment(customFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun updateTitle(@StringRes title: Int) = getString(R.string.app_name) + "-" + getString(title)

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        showFragment(SimpleFragment.newInstance())
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
