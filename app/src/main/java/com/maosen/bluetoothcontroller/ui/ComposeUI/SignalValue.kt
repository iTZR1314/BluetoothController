package com.maosen.bluetoothcontroller.ui.ComposeUI

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue

/**
 * 这个函数提供两个整数值，它们会随着device.rssi的值发生变化，用于显示信号的强度
 */
@Composable
fun SignalValue(value: Int, func: @Composable (red:Int, green:Int) -> Unit) {
    val redValue: Int by animateIntAsState(
        (if (value.times(-3.1875) - 63.75 < 255) {
            value.times(-3.1875) - 63.75
        } else 255).toInt()
    )
    val greenValue: Int by animateIntAsState(
        (if (318.75 + value.times(3.1875) > 0) {
            318.75 + value.times(3.1875)
        } else 255).toInt()
    )
    func(redValue, greenValue)
}