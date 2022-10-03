package jp.ac.okinawa_ct.nitoc_ict.aroa_wearos

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothHeadset
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import jp.ac.okinawa_ct.nitoc_ict.aroa_wearos.databinding.ActivityMainBinding

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vibrateFunctionBase()
    }

    fun vibrateFunctionBase() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S)
        { //APIレベルが31(android.os.Build.VERSION_CODES.Sは定数値)以上の場合の処理
            val vibratorManager = this.getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager
            val vibrationEffect = VibrationEffect.createWaveform(
                longArrayOf(500L, 500L), //timings : タイミング・振幅をミリ秒の配列で指定
                intArrayOf(VibrationEffect.DEFAULT_AMPLITUDE, 10), //amplitudes : 上の配列の振幅値(0~255) 0はOFF
                1) //repeat : 繰り返しのタイミングを決める配列をintで選ぶ -1だと繰り返さない
            val combineVibration = android.os.CombinedVibration.createParallel(vibrationEffect)

            //FABを押すと振動するようにしている...はず
            val buttonPushed = findViewById<android.widget.Button>(R.id.extended_fab)
            buttonPushed.setOnClickListener {
                vibratorManager.vibrate(combineVibration)
            }

            //private Vibrater vib;
        } else { //APIレベルが31未満の場合の処理
            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

            //こっちも振動
            val buttonPushed2 = findViewById<android.widget.Button>(R.id.extended_fab)
            buttonPushed2.setOnClickListener {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(300, //milliseconds
                        VibrationEffect.DEFAULT_AMPLITUDE //amplitudes
                    ))
            }

        }
    }
}
