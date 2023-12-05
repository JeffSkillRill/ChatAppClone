package com.example.chatappclone.ui

import com.example.chatappclone.DestinationScreen
import com.example.chatappclone.R

enum class BottomNavigationMenu(val icon: Int, val navDestination: DestinationScreen)
{
    PROFILE(R.drawable.baseline_profile, DestinationScreen.Profile),
    CHATLIST(R.drawable.baseline_chat, DestinationScreen.ChatList),
    STATUSLIST(R.drawable.baseline_status, DestinationScreen.StatusList)
}