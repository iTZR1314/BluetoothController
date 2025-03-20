package com.maosen.bluetoothcontroller

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.maosen.bluetoothcontroller.ui.ComposeUI.FromItemDevice
import com.maosen.bluetoothcontroller.ui.ComposeUI.FromToTransform
import com.maosen.bluetoothcontroller.ui.ComposeUI.MainHomePage
import com.maosen.bluetoothcontroller.ui.ComposeUI.ToItemDevice
import com.maosen.bluetoothcontroller.ui.theme.BluetoothControllerTheme

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.values.all { it }) {
            Log.d("权限", "拥有权限")
            Toast.makeText(this, "拥有权限", Toast.LENGTH_SHORT).show()
        } else {
            Log.d("权限", "没有权限")
            Toast.makeText(this, "没有权限", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: BluetoothViewModel by viewModels()
            val deviceList by viewModel.deviceList.collectAsState()
            val permissions = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT
            )
            val allPermissionsGranted = remember {
                permissions.all {
                    ContextCompat.checkSelfPermission(this@MainActivity, it) == PackageManager.PERMISSION_GRANTED
                }
            }
            BluetoothControllerTheme {
                MainHomePage ({if (allPermissionsGranted) {
                    viewModel.startScanning(this@MainActivity)
                } else {
                    requestPermissionLauncher.launch(permissions)
                }}){
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().background(Color.White),
                        verticalArrangement = Arrangement.Top
                    ) {
                        items(deviceList, key = { it.address }) { device ->
                            var isExpand by remember { mutableStateOf(false) }
                            val expandHeight: Int by animateIntAsState(if (isExpand) 400 else 50)
                            FromToTransform(isExpand, {
                                FromItemDevice(device) {
                                    isExpand=!isExpand
                                    if (allPermissionsGranted) {
                                        viewModel.startScanning(this@MainActivity)
                                    } else {
                                        requestPermissionLauncher.launch(permissions)
                                    }

                                }
                            }, {
                                ToItemDevice(device) {
                                    viewModel.connectToDevice(
                                        device,
                                        sendMessage = "这是一个测试"
                                    )
                                }
                            })

//                            SignalValue(device.rssi) { red, green ->
//                                Card(
//                                    modifier = Modifier
//                                        .padding(5.dp)
//                                        .fillMaxWidth()
//                                        .height(expandHeight.dp),
//                                    colors = CardColors(
//                                        containerColor = Color(red, green, 0),
//                                        contentColor = Color.Black,
//                                        disabledContainerColor = Color(red, green, 0),
//                                        disabledContentColor = Color.Black
//                                    )
//                                ) {
//                                    Row(
//                                        modifier = Modifier.fillMaxSize(),
//                                        verticalAlignment = Alignment.CenterVertically,
//                                        horizontalArrangement = Arrangement.Center
//                                    ) {
//                                        Text(
//                                            text = device.name,
//                                            modifier = Modifier.align(alignment = Alignment.CenterVertically)
//                                        )
//                                        Spacer(modifier = Modifier.width(5.dp))
//
//                                        Text(
//                                            text = device.address,
//                                            modifier = Modifier.align(alignment = Alignment.CenterVertically)
//                                        )
//                                        Spacer(modifier = Modifier.width(5.dp))
//
//                                        Text(
//                                            text = device.rssi.toString(),
//                                            modifier = Modifier.align(alignment = Alignment.CenterVertically)
//                                        )
//                                        Spacer(modifier = Modifier.width(5.dp))
//
//                                        IconButton(onClick = { isExpand = !isExpand }) {
//                                            Icon(Icons.Default.MoreVert, contentDescription = "")
//                                        }
//
//                                        DropdownMenu(
//                                            expanded = isExpand,
//                                            onDismissRequest = { isExpand = !isExpand }) {
//                                            for (i in device.uuid) {
//                                                DropdownMenuItem(
//                                                    text = { Text(text = i.toString()) },
//                                                    onClick = {
//                                                        viewModel.connectToDevice(
//                                                            device,
//                                                            sendMessage = "这是一个测试"
//                                                        )
//                                                    })
//                                            }
//                                        }
//                                    }
//
//
//                                }
//                            }

                        }
                    }
//                    Column(
//                        verticalArrangement = Arrangement.Center,
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .fillMaxHeight(0.6f)
//                    ) {
//                        BluetoothScanScreen(
//                            viewModel,
//                            context = this@MainActivity,
//                            requestPermissionLauncher
//                        )
//
//                    }
                }
            }
        }
    }
}

@Composable
fun BluetoothScanScreen(
    viewModel: BluetoothViewModel,
    context: Context,
    resultLauncher: ActivityResultLauncher<Array<String>>
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT
        )
        val allPermissionsGranted = remember {
            permissions.all {
                ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            }
        }
        Row {
            Button(onClick = {
                if (allPermissionsGranted) {
                    viewModel.startScanning(context)
                } else {
                    resultLauncher.launch(permissions)
                }
            }) {
                Text("开始扫描")
            }

            Button(onClick = {
                if (allPermissionsGranted) {
                    viewModel.stopScanning(context)
                } else {
                    resultLauncher.launch(permissions)
                }
            }) {
                Text("停止扫描")
            }
        }
    }
}





@Preview
@Composable
private fun Test() {
    MainHomePage({}){

    }
}


