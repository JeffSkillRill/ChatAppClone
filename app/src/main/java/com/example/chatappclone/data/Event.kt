package com.example.chatappclone.data

open class Event<out T>(private val content: T) {
    var hasbeenHandled = false
        private set
    fun getContentOrNull(): T?{
        return if (hasbeenHandled)
            null
        else{
            hasbeenHandled = true
            content
        }
    }
}