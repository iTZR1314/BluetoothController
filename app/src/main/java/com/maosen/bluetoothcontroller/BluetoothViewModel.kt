package com.maosen.bluetoothcontroller

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.bluetooth.BluetoothLe
import androidx.bluetooth.GattService
import androidx.bluetooth.ScanFilter
import androidx.bluetooth.ScanResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.lang.Exception
import java.nio.charset.Charset

class BluetoothViewModel : ViewModel() {
    /**
     * 这一步是进行蓝牙的扫描
     */
    private lateinit var bluetoothLe: BluetoothLe
    private val _scanResult = MutableSharedFlow<ScanResult>()

    /**
     * 这是一个控制蓝牙扫描的句柄，可以停止蓝牙扫描
     */
    var scanJob: Job? = null

    private val _deviceList = MutableStateFlow<List<BluetoothScannedDevice>>(emptyList())
    val deviceList = _deviceList.asStateFlow()

    /**
     * 这是连接蓝牙的句柄，可以用它来取消蓝牙连接
     */
    var connectJob:Job?=null


    /**
     * 这是蓝牙读取服务特性的句柄
     */
    var read: Deferred<Unit>? = null

    /**
     *这是向蓝牙写入服务特性的句柄
     */

    var write: Deferred<Unit>? = null
    @SuppressLint("MissingPermission")
    fun startScanning(context: Context) {
        if (scanJob?.isActive == true) {
            return

        }
        bluetoothLe = BluetoothLe(context)
        val scanFlow = bluetoothLe.scan(filters = listOf(
            ScanFilter(
                deviceName = "MCC"
            )
        ))
        Toast.makeText(context, "开始扫描", Toast.LENGTH_SHORT).show()

        scanJob = viewModelScope.launch(Dispatchers.Default) {
            scanFlow.collect { result ->

//                Log.d("BluetoothViewModel","scan result : $result ${result.device.name} ${result.rssi}")
                _scanResult.emit(result)
                val newDevice = BluetoothScannedDevice(
                    name = result.device.name ?: "Unknown",
                    address = result.deviceAddress.address,
                    rssi = result.rssi,
                    uuid = result.serviceUuids,
                    device = result.device
                )


                val index = _deviceList.value.indexOfFirst { it.name == newDevice.name }

                if (index != -1) {
                    val updatedList = _deviceList.value.toMutableList()
                    updatedList[index] = updatedList[index].copy(rssi = newDevice.rssi)
                    _deviceList.value = updatedList
                } else {
                    _deviceList.value += newDevice
                }

            }

        }

    }

    fun stopScanning(context: Context) {
        scanJob?.cancel()
        Toast.makeText(context, "停止扫描", Toast.LENGTH_SHORT).show()
    }

    /**
     *这一步是对蓝牙发起连接
     */

    @SuppressLint("MissingPermission")
    fun connectToDevice(bluetoothScannedDevice: BluetoothScannedDevice,sendMessage:String) {
        Log.d("蓝牙连接", "点击")
        if (connectJob?.isActive == true){
            return
        }
        val device = bluetoothScannedDevice.device
        connectJob=viewModelScope.launch(Dispatchers.IO) {
            Log.d("蓝牙连接", "协程作用域启动")
            try {
                bluetoothLe.connectGatt(device) {
                    val server = servicesFlow
                    Log.d("蓝牙连接", "蓝牙连接成功执行代码块Block")
                    server.collect() {
                        for (i in it) {
                            read =async(Dispatchers.IO) {
                                for (a in i.characteristics){
                                    val b= readCharacteristic(a)
                                    val c= b.getOrNull()
                                    if (c!=null){
                                        Log.d("蓝牙连接","特征值"+String(c, Charset.defaultCharset())+" " +i.uuid.toString())
                                    }else{
                                        Log.d("蓝牙连接","空值" + " " + i.uuid.toString())
                                    }

                                }
                            }
                            write = async(Dispatchers.IO) {
                                for (a in i.characteristics) {
                                    if (readCharacteristic(a).getOrNull()!=null){
                                        val writeItem = async {
                                            try {
                                                writeCharacteristic(a, sendMessage.toByteArray())
                                            } catch (e: Exception) {
                                                null
                                            }

                                        }
                                        if (writeItem.await()==null) {
                                            Log.d("蓝牙连接","写入失败"+ sendMessage + i.uuid.toString())
                                        }else{
                                            Log.d("蓝牙连接", "写入成功" + sendMessage + i.uuid.toString()+ writeItem.await()!!.isSuccess.toString())

                                        }
                                    }




                                }
                            }

                        }

                    }
                }
            } catch (e: CancellationException) {
                Log.d("蓝牙连接", "蓝牙连接失败抛出异常")
            }
        }
        Log.d("蓝牙连接","执行完成")

    }

}