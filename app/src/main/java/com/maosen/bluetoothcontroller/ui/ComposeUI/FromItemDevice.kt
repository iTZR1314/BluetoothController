package com.maosen.bluetoothcontroller.ui.ComposeUI

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.maosen.bluetoothcontroller.BluetoothScannedDevice
import com.maosen.bluetoothcontroller.R

/**
 * 这是一个没有变化之前的设备列表,第二个参数是用于更改isExpand的值
 */
@Composable
fun FromItemDevice(device: BluetoothScannedDevice,click:()->Unit){
    Card(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()
            .height(50.dp),
        colors = CardColors(
            containerColor = Color(154, 168, 245, 100),
            contentColor = Color.Black,
            disabledContentColor = Color.Black,
            disabledContainerColor = Color(154, 168, 245, 255)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clickable { click() },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.deng),
                contentDescription = "",
                modifier = Modifier
                    .clip(shape = CircleShape)
                    .padding(5.dp)
            )
            Spacer(Modifier.width(10.dp))
            Text("名称")
            Spacer(Modifier.width(10.dp))
            Text("状态：未知")
            Spacer(Modifier.width(10.dp))
            Text("信号强度")
            Spacer(Modifier.width(5.dp))
            SignalValue(device.rssi) { red,green ->
                Surface(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape),
                    color = Color(red,green,0)
                ) { }

            }

        }
    }
}