package com.ikancipung.laundrygo.chat

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.ikancipung.laundrygo.R

class ChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(buildChatUI(this))
    }

    fun buildChatUI(context: Context): ConstraintLayout {
        val rootLayout = ConstraintLayout(context).apply {
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
            )
        }

        // Toolbar Layout
        val toolbar = ConstraintLayout(context).apply {
            id = View.generateViewId()
            setBackgroundColor(Color.WHITE)
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                dpToPx(context, 64)
            )
            elevation = 4f
        }

        // Back Button
        val backButton = ImageView(context).apply {
            id = View.generateViewId()
            setImageDrawable(ContextCompat.getDrawable(context, R.drawable.left_button))
            layoutParams = ConstraintLayout.LayoutParams(dpToPx(context, 24), dpToPx(context, 24))
        }

        // Avatar
        val avatar = ImageView(context).apply {
            id = View.generateViewId()
            setImageDrawable(ContextCompat.getDrawable(context, R.drawable.avatar))
            layoutParams = ConstraintLayout.LayoutParams(dpToPx(context, 40), dpToPx(context, 40))
        }

        // Title Text
        val titleText = TextView(context).apply {
            id = View.generateViewId()
            text = "Antony Laundry"
            textSize = 18f
            setTextColor(Color.BLACK)
            gravity = Gravity.CENTER
        }

        // Message Input Layout
        val messageInputLayout = LinearLayout(context).apply {
            id = View.generateViewId()
            orientation = LinearLayout.HORIZONTAL
            setBackgroundColor(Color.WHITE)
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomToBottom = ConstraintSet.PARENT_ID
            }
            setPadding(dpToPx(context, 16), dpToPx(context, 12), dpToPx(context, 16), dpToPx(context, 12))
        }

        // Add Button (Tanda Plus)
        val addButton = ImageView(context).apply {
            id = View.generateViewId()
            setImageDrawable(ContextCompat.getDrawable(context, R.drawable.more_options))
            layoutParams = LinearLayout.LayoutParams(dpToPx(context, 30), dpToPx(context, 30)).apply {
                marginEnd = dpToPx(context, 20)
            }
        }

        // Message Input
        val messageInput = EditText(context).apply {
            id = View.generateViewId()
            hint = "Type a message"
            textSize = 16f
            layoutParams = LinearLayout.LayoutParams(0, ConstraintLayout.LayoutParams.WRAP_CONTENT, 1f).apply {
                marginEnd = dpToPx(context, 8)
            }
        }

        // Send Button
        val sendButton = ImageView(context).apply {
            id = View.generateViewId()
            setImageDrawable(ContextCompat.getDrawable(context, R.drawable.send))
            layoutParams = LinearLayout.LayoutParams(dpToPx(context, 40), dpToPx(context, 40)) // Memperbesar ukuran ikon
        }

        // Add Views to Message Input Layout
        messageInputLayout.addView(addButton)
        messageInputLayout.addView(messageInput)
        messageInputLayout.addView(sendButton)

        // Add Views to Toolbar
        toolbar.addView(backButton)
        toolbar.addView(avatar)
        toolbar.addView(titleText)

        // Add Views to Root Layout
        rootLayout.addView(toolbar)
        rootLayout.addView(messageInputLayout)

        // Set Constraints for Toolbar
        val set = ConstraintSet()
        set.clone(toolbar)
        set.connect(backButton.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, dpToPx(context, 16))
        set.connect(backButton.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, dpToPx(context, 20))

        set.connect(avatar.id, ConstraintSet.START, backButton.id, ConstraintSet.END, dpToPx(context, 8))
        set.connect(avatar.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, dpToPx(context, 12))

        set.connect(titleText.id, ConstraintSet.START, avatar.id, ConstraintSet.END, dpToPx(context, 1)) // Geser lebih ke tengah
        set.connect(titleText.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, dpToPx(context, 16))
        set.connect(titleText.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        set.connect(titleText.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)

        set.applyTo(toolbar)

        // Set Constraints for Message Input Layout
        val inputSet = ConstraintSet()
        inputSet.clone(rootLayout)
        inputSet.connect(messageInputLayout.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
        inputSet.connect(messageInputLayout.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
        inputSet.connect(messageInputLayout.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)

        inputSet.applyTo(rootLayout)

        return rootLayout
    }

    // Helper function to convert dp to px
    private fun dpToPx(context: Context, dp: Int): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).toInt()
    }
}

// Preview Function for ChatActivity
@Preview(showBackground = true)
@Composable
fun PreviewChatActivity() {
    AndroidView(factory = { context ->
        ChatActivity().buildChatUI(context)
    })
}
