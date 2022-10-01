package jp.ac.okinawa_ct.nitoc_ict.aroa_wearos

import android.app.Activity
import android.os.Bundle
import jp.ac.okinawa_ct.nitoc_ict.aroa_wearos.databinding.ActivityMainBinding

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}