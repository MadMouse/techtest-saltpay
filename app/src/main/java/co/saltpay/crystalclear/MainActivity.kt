package co.saltpay.crystalclear

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import co.saltpay.crystalclear.ui.landing.LandingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, LandingFragment.newInstance())
                .commitNow()
        }
    }
}