package com.maosen.bluetoothcontroller

import androidx.bluetooth.BluetoothDevice
import java.util.*

data class BluetoothScannedDevice(
    val name: String,
    val address: String,
    val rssi: Int,
    val uuid: List<UUID>,
    val device: BluetoothDevice
)