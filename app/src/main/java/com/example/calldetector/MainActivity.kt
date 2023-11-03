package com.example.calldetector

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.calldetector.broadcastreceiver.PhoneCallReceiver
import com.example.calldetector.customdialog.StatusDialog
import com.example.calldetector.util.PHONE_STATE_PERMISSION
import com.example.calldetector.util.SharedPreferenceHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity:AppCompatActivity()
{
    private lateinit var phoneCallReceiver:PhoneCallReceiver
    private lateinit var sharedPreferenceHelper:SharedPreferenceHelper
    private lateinit var statusDialog:StatusDialog
    private lateinit var fab:FloatingActionButton

    private  var isPermissionGranted:Boolean=false

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState:Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab=findViewById(R.id.fab)
        phoneCallReceiver=PhoneCallReceiver()
        
        permission()
        sharedPreferenceHelper=SharedPreferenceHelper(this)

        if (sharedPreferenceHelper.isEnabled()) 
        {
            registerReceiver(phoneCallReceiver, IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED))
        }
        
        statusDialog=StatusDialog(this)
        fab.setOnClickListener {statusDialog.show()}

    }

    override fun onCreateOptionsMenu(menu:Menu): Boolean
    {
        menuInflater.inflate(R.menu.menu_items, menu)

        if(isPermissionGranted)
        {
            if(sharedPreferenceHelper.isEnabled())
            {
                val item=menu.findItem(R.id.action_toggle)
                item.isChecked=true
                item.title="disable"
            }
            else
            {
                val item=menu.findItem(R.id.action_toggle)
                item.isChecked=false
                item.title="enable"
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item:MenuItem): Boolean
    {

        if(isPermissionGranted)
        {
            if(item.itemId == R.id.action_toggle)
            {
                if(item.isChecked)
                {
                    item.title="enable"
                    sharedPreferenceHelper.makeEnable(false)
                    item.isChecked=false
                    unregisterReceiver(phoneCallReceiver)
                    Toast.makeText(this, "deactivated", Toast.LENGTH_SHORT).show()


                }
                else
                {
                    item.title="disable"
                    item.isChecked=true
                    sharedPreferenceHelper.makeEnable(true)
                    val filter=IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED)
                    registerReceiver(phoneCallReceiver, filter)
                    Toast.makeText(this, "activated", Toast.LENGTH_SHORT).show()
                }
            }
            return true
        }
        else
        {
            permission()
            return super.onOptionsItemSelected(item)
        }

    }


    private fun permission()
    {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)
        {
            isPermissionGranted=true
        }
        else
        {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_PHONE_STATE), PHONE_STATE_PERMISSION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PHONE_STATE_PERMISSION)
        {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                isPermissionGranted=true
            }
            else
            {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_PHONE_STATE)) {
                    val dialog = AlertDialog.Builder(this)
                        .setTitle("Permission required")
                        .setMessage("You need to grant the phone state permission to use this feature. Please open settings and enable it.")
                        .setPositiveButton("Settings") { _, _ ->
                            // Open settings intent
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri = Uri.fromParts("package", packageName, null)
                            intent.data = uri
                            startActivity(intent)
                        }
                        .setNegativeButton("Cancel", null)
                        .create()
                    dialog.show()
                }
            }
        }
    }

    override fun onDestroy()
    {
        super.onDestroy()
        if(sharedPreferenceHelper.isEnabled())
            unregisterReceiver(phoneCallReceiver)
    }
}