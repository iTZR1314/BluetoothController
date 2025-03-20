package com.maosen.bluetoothcontroller.ui.ComposeUI

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.maosen.bluetoothcontroller.BluetoothScannedDevice


/**
 * 第二个参数是按钮的执行函数
 */
@Composable
fun ToItemDevice(device: BluetoothScannedDevice,click:()->Unit){
    Card(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()
            .height(400.dp),
        colors = CardColors(
            containerColor = Color(154, 168, 245, 100),
            contentColor = Color.Black,
            disabledContentColor = Color.Black,
            disabledContainerColor = Color(154, 168, 245, 255)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.15f)
                ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text("蓝牙连接状态")
            Spacer(Modifier.width(30.dp))
            SignalValue(device.rssi) {red , green  ->
                Surface(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape),
                    color = Color(red,green,0)
                ) { }
            }

            Spacer(Modifier.width(10.dp))
            Surface(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
            ) { }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
                ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = {click()}, shape = CircleShape, modifier = Modifier.size(150.dp)) {

            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text("状态切换")
        }
    }
}