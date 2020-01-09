package com.nishant.bluetoothspeechcontroller

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import java.lang.Exception
import java.util.*

private const val TAG = "BluetoothConnection"
class BluetoothConnection(private val context: Context, private val textView: TextView) {
    private lateinit var myBluetooth: BluetoothAdapter
    private lateinit var btSocket: BluetoothSocket
    private lateinit var pairedDevices: Set<BluetoothDevice>
    private lateinit var address: String
    private lateinit var name: String

    companion object {
        val myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    }

    init {
        bluetoothConnectDevice()
    }

    fun sendData(textToSend: String) {
        try {
            btSocket.outputStream.write(textToSend.toByteArray())
        } catch (e: Exception) {
            Toast.makeText(context, "Error while sending to device.", Toast.LENGTH_SHORT).show()
            Log.e(TAG, e.message!!, e)
        }
    }

    private fun bluetoothConnectDevice() {
        try {
            myBluetooth = BluetoothAdapter.getDefaultAdapter()
            pairedDevices = myBluetooth.bondedDevices
            if (pairedDevices.isNotEmpty())
                for (bt in pairedDevices) {
                    address = bt.address.toString()
                    name = bt.name.toString()
                    Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show()
                }
        } catch (e: Exception) {
            Toast.makeText(context, "Error while connecting device.", Toast.LENGTH_SHORT).show()
            Log.e(TAG, e.message!!, e)
        }
        myBluetooth = BluetoothAdapter.getDefaultAdapter()
        val dispositivo = myBluetooth.getRemoteDevice(address)
        btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID)
        btSocket.connect()
        textView.text = "BT name: $name\nBT address = $address"
    }
}