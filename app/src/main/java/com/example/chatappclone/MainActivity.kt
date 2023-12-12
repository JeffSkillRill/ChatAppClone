package com.example.chatappclone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chatappclone.ui.ChatListScreen
import com.example.chatappclone.ui.LoginScreen
import com.example.chatappclone.ui.ProfileScreen
import com.example.chatappclone.ui.SignUpScreen
import com.example.chatappclone.ui.SingleChatScreen
import com.example.chatappclone.ui.SingleStatusScreen
import com.example.chatappclone.ui.StatusListScreen
import com.example.chatappclone.ui.theme.ChatAppCloneTheme
import dagger.hilt.android.AndroidEntryPoint

sealed class DestinationScreen(val route: String)
{
    object SignUp: DestinationScreen("signup")
    object Login: DestinationScreen("login")
    object Profile: DestinationScreen("profile")
    object ChatList: DestinationScreen("chatlist")
    object SingleChat: DestinationScreen("singleChat/{chatId}")
    {
        fun createRoute(id: String) = "singleChat/$id"
    }
    object StatusList: DestinationScreen("statusList")
    object SingleStatus: DestinationScreen("singleStatus/{statusId}")
    {
        fun createRoute(id: String) = "singleStatus/$id"
    }

}
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatAppCloneTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //Greeting("Android")
                    ChatAppNavigation()
                }
            }
        }
    }
}
@Composable
fun ChatAppNavigation()
{
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = DestinationScreen.Profile.route)
    {
        composable(DestinationScreen.SignUp.route)
        {
            SignUpScreen()
        }
        composable(DestinationScreen.Login.route)
        {
            LoginScreen()
        }
        composable(DestinationScreen.Profile.route)
        {
            ProfileScreen(navController = navController)
        }
        composable(DestinationScreen.StatusList.route)
        {
            StatusListScreen(navController = navController)
        }
        composable(DestinationScreen.SingleStatus.route)
        {
            SingleStatusScreen(statusId = "123")
        }
        composable(DestinationScreen.ChatList.route)
        {
            ChatListScreen(navController = navController)
        }
        composable(DestinationScreen.SingleChat.route)
        {
            SingleChatScreen(chatId = "123")
        }
    }
}

