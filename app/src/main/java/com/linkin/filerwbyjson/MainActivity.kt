package com.linkin.filerwbyjson

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.linkin.filerwbyjson.databinding.ActivityMainBinding
import com.linkin.rwlibrary.LinsFileOperation
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val ioScope by lazy { CoroutineScope(Dispatchers.IO) }
    private val tempOb = TempOb("属性1","属性2",100)

    @RequiresApi(Build.VERSION_CODES.R)
    private val lauch = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        if (!Environment.isExternalStorageManager()){
            Toast.makeText(this, "请授予权限", Toast.LENGTH_SHORT).show()
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            intent.data = Uri.parse("package:" + applicationContext!!.packageName)
            requestResult.launch(intent)
        }
    }

    private val requestResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        Log.d(TAG, ": ${it.toString()}")
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //lauch.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        lauch.launch(android.Manifest.permission.MANAGE_EXTERNAL_STORAGE)

        binding.read.setOnClickListener {
            ioScope.launch {
                val result =  withContext(Dispatchers.IO){
                    LinsFileOperation.read<TempOb>(Environment.getExternalStoragePublicDirectory("").toString(),"a.json")
                }
                withContext(Dispatchers.Main){
                    binding.content.text = result.toString()
                }
            }
        }

        binding.write.setOnClickListener {
            ioScope.launch {
                LinsFileOperation.write(Environment.getExternalStoragePublicDirectory("").toString(),"a.json",tempOb)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ioScope.cancel("Activity Is Destorying")
        Log.d(TAG, "onDestroy: ")
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}