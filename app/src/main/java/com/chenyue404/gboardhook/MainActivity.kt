package com.chenyue404.gboardhook

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

/**
 * Created by cy on 2022/1/14.
 */
class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val et0 = findViewById<EditText>(R.id.et0)
        val et1 = findViewById<EditText>(R.id.et1)
        val bt0 = findViewById<Button>(R.id.bt0)

        val pref: SharedPreferences? = try {
            getSharedPreferences(PluginEntry.SP_FILE_NAME, Context.MODE_WORLD_READABLE)
        } catch (e: SecurityException) {
            Log.d("MainActivity", "getSharedPreferences失败---$e")
            Toast.makeText(this, "读取配置失败", Toast.LENGTH_SHORT).show()
            null
        }

        pref?.getString(PluginEntry.SP_KEY, null)?.split(",")?.let {
            et0.text.append(it[0])
            et1.text.append(it[1])
        }
        bt0.setOnClickListener {
            val num = et0.text.toString().toIntOrNull() ?: PluginEntry.DEFAULT_NUM
            val time = et1.text.toString().toLongOrNull() ?: PluginEntry.DEFAULT_TIME
            pref?.edit()?.putString(PluginEntry.SP_KEY, "$num,$time")?.apply()

            startActivity(
                Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    .apply {
                        addCategory(Intent.CATEGORY_DEFAULT)
                        data = Uri.parse("package:${PluginEntry.PACKAGE_NAME}")
                    })
        }
    }
}