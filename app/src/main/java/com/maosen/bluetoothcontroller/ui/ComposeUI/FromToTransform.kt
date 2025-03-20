package com.maosen.bluetoothcontroller.ui.ComposeUI

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.maosen.bluetoothcontroller.R

/**
 * 这里的参数代表将一个内容用动画的方式转换为另一个内容
 */

@Composable
fun FromToTransform(isExpand: Boolean, from: @Composable () -> Unit, to: @Composable () -> Unit) {
    Surface(
        color = Color.White,
    ) {
        AnimatedContent(
            targetState = !isExpand,
            transitionSpec = {
                expandVertically(animationSpec = tween(150, 150)) togetherWith
                        shrinkVertically(animationSpec = tween(150)) using
                        SizeTransform { initialSize, targetSize ->
                            if (targetState) {
                                keyframes {
                                    // Expand horizontally first.
                                    IntSize(targetSize.width, initialSize.height) at 150
                                    durationMillis = 300
                                }
                            } else {
                                keyframes {
                                    // Shrink vertically first.
                                    IntSize(initialSize.width, targetSize.height) at 150
                                    durationMillis = 300
                                }
                            }
                        }
            }, label = "size transform"
        ) { targetExpanded ->
            if (targetExpanded) {
                from()
            } else {
                to()
            }
        }
    }
}


@Composable
@Preview
private fun Test() {
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var isExpanded by remember {
            mutableStateOf(false)
        }
        FromToTransform(isExpanded, {

        }, {
            Card(
                modifier = Modifier
                    .clickable { isExpanded = !isExpanded }
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
                        .background(Color.Red),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("蓝牙连接状态")
                    Spacer(Modifier.width(30.dp))
                    Surface(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                    ) { }
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
                        .background(Color.Green),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(onClick = {}, shape = CircleShape, modifier = Modifier.size(150.dp)) {

                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .background(Color.Blue),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("状态切换")
                }
            }
        })
    }

}