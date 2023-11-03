package com.example.calldetector.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.TelephonyCallback
import android.telephony.TelephonyManager
import android.widget.Toast
import com.example.calldetector.util.SharedPreferenceHelper

class PhoneCallReceiver : BroadcastReceiver()
{

    private lateinit var sharedPreferenceHelper:SharedPreferenceHelper

    override fun onReceive(context: Context, intent: Intent)
    {

        sharedPreferenceHelper=SharedPreferenceHelper(context)

        if (intent.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED)
        {
            val message=sharedPreferenceHelper.getStatus()

            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                telephonyManager.registerTelephonyCallback(
                        context.mainExecutor,
                        object : TelephonyCallback(), TelephonyCallback.CallStateListener {
                            override fun onCallStateChanged(state: Int)
                            {
                                when (state)
                                {
                                    TelephonyManager.CALL_STATE_RINGING ->
                                    {
                                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                        }
                                                          )
            }
        }
    }
}