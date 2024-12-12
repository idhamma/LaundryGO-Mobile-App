package com.ikancipung.laundrygo.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ikancipung.laundrygo.R

@Composable
fun ChatScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ChatToolbar(onBackClick = { navController.popBackStack() })
        Spacer(modifier = Modifier.weight(1f)) // Placeholder for chat messages
        ChatInputArea()
    }
}

@Composable
fun ChatToolbar(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(color = androidx.compose.ui.graphics.Color.White)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.left_button),
            contentDescription = "Back Button",
            modifier = Modifier
                .size(24.dp)
                .clickable { onBackClick() }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Image(
            painter = painterResource(id = R.drawable.avatar),
            contentDescription = "Avatar",
            modifier = Modifier
                .size(40.dp)
                .background(Color.LightGray, shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Antony Laundry",
            fontSize = 18.sp,
            color = Color.Black
        )
    }
}

@Composable
fun ChatInputArea() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.more_options),
            contentDescription = "Add Options",
            modifier = Modifier.size(30.dp)
        )
        Spacer(modifier = Modifier.width(20.dp))
        val textState = remember { TextFieldValue() }
        BasicTextField(
            value = textState,
            onValueChange = {},
            modifier = Modifier
                .weight(1f)
                .background(Color.LightGray, shape = MaterialTheme.shapes.small)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            decorationBox = { innerTextField ->
                if (textState.text.isEmpty()) {
                    Text("Type a message", fontSize = 16.sp, color = Color.Gray)
                }
                innerTextField()
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            painter = painterResource(id = R.drawable.send),
            contentDescription = "Send Message",
            modifier = Modifier.size(40.dp)
        )
    }
}

@Composable
@Preview
fun PreviewChatScreen() {
    ChatScreen(navController = rememberNavController())
}
