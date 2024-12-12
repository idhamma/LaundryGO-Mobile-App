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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.ikancipung.laundrygo.R
import com.ikancipung.laundrygo.menu.Laundry
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(navController: NavController, laundryLogo: String, laundryName: String) {
    var messages by remember { mutableStateOf(listOf<Pair<Boolean, String>>()) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ChatToolbar(onBackClick = { navController.popBackStack() }, laundryLogo, laundryName)
        Spacer(modifier = Modifier.weight(1f))
        // Display chat messages
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            for ((isUser, message) in messages) {
                ChatMessage(isUser = isUser, message = message)
            }
        }
        ChatInputArea(onSendMessage = { userMessage ->
            messages = messages + (true to userMessage)
            coroutineScope.launch {
                delay(1000) // Simulate delay for bot reply
                messages = messages + (false to "This is A Dummy")
            }
        })
    }
}

@Composable
fun ChatToolbar(onBackClick: () -> Unit, laundryLogo: String, laundryName: String) {
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
            painter = rememberImagePainter(laundryLogo),
            contentDescription = "Avatar",
            modifier = Modifier
                .size(40.dp)
                .background(Color.LightGray, shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = laundryName,
            fontSize = 18.sp,
            color = Color.Black
        )
    }
}

@Composable
fun ChatInputArea(onSendMessage: (String) -> Unit) {
    var textState by remember { mutableStateOf(TextFieldValue()) }

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
        BasicTextField(
            value = textState,
            onValueChange = { textState = it },
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
            modifier = Modifier
                .size(40.dp)
                .clickable {
                    if (textState.text.isNotBlank()) {
                        onSendMessage(textState.text)
                        textState = TextFieldValue() // Clear input field
                    }
                }
        )
    }
}

@Composable
fun ChatMessage(isUser: Boolean, message: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(
                    if (isUser) Color.Blue else Color.LightGray,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = message,
                color = if (isUser) Color.White else Color.Black,
                fontSize = 16.sp
            )
        }
    }
}

//@Composable
//@Preview
//fun PreviewChatScreen() {
//    ChatScreen(navController = rememberNavController())
//}
