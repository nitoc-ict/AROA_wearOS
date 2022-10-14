package jp.ac.okinawa_ct.nitoc_ict.aroa_wearos

import android.Manifest
import android.app.Activity
import android.app.Service
import android.bluetooth.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.hardware.usb.UsbDevice
import android.os.Binder
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import jp.ac.okinawa_ct.nitoc_ict.aroa_wearos.databinding.ActivityMainBinding
import java.util.UUID

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding
    private val REQUEST_ENABLE_BT = 1

    val bluetoothManager: BluetoothManager =
        getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
    val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vibrateFunctionBase()

        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(receiver, filter)

        val discoverableIntent : Intent =Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE).apply {
        putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
        }
        startActivity(discoverableIntent)
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action : String? = intent?.action
            when(action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device : BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val deviceName = if (ActivityCompat.checkSelfPermission(
                            TODO("コンテキストの取得方法が分かりません！"),
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return
                    } else {

                    }
                        device?.name
                    val deviceHardwareAddress = device?.address

                    class AcceptThread : Thread() {
                        val mmServerSocket : BluetoothServerSocket? by lazy(LazyThreadSafetyMode.NONE) {
                            bluetoothAdapter?.listenUsingInsecureRfcommWithServiceRecord(deviceName.toString(), TODO("deviceHardwareAddressを入れるとタイプエラーになってます！"))
                        }
                    }

                }
            }
        }
    }



    override fun onDestroy() {
        super.onDestroy()

        unregisterReceiver(receiver)
    }

    /*private inner class ConnectThread(device : BluetoothDevice) : Thread() {
        private val Socket : BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
                device.createInsecureRfcommSocketToServiceRecord(device.uuids[0].uuid)
            }
        }
    }*/


    private fun vibrateFunctionBase() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) { //APIレベルが31(android.os.Build.VERSION_CODES.Sは定数値)以上の場合の処理
            val vibratorManager = this.getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager
            val vibrationEffect = VibrationEffect.createWaveform(
                longArrayOf(500L, 500L), //timings : タイミング・振幅をミリ秒の配列で指定
                intArrayOf(
                    VibrationEffect.DEFAULT_AMPLITUDE,
                    10
                ), //amplitudes : 上の配列の振幅値(0~255) 0はOFF
                1
            ) //repeat : 繰り返しのタイミングを決める配列をintで選ぶ -1だと繰り返さない
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
                    VibrationEffect.createOneShot(
                        300, //milliseconds
                        VibrationEffect.DEFAULT_AMPLITUDE //amplitudes
                    )
                )
            }

        }
    }

    //開発中
    fun bluetoothConnectFunction() {

        if (bluetoothAdapter != null) {
            if (!bluetoothAdapter.isEnabled) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
            }

            //android.os.Process.killProcess(android.os.Process.myPid())
        }

        var bluetoothDevice: BluetoothDevice? = null
        var pairedDevice: Set<BluetoothDevice>? = if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        } else {
            TODO("例外処置かかないとエラーになるので暫定的に入れている")
        }
        bluetoothAdapter?.bondedDevices


        val uuid: UUID = UUID.fromString(TODO("uuidを出力するクラスをいつかは入れたい"))


    }
}

    /*class DeviceControlActivity : Activity() {
        fun displayGattServices(gattServices : List<BluetoothGattService>?){
            if (gattServices == null) return
            var uuid : String?
            val unknownServiceString : String =
        }
    }*/
