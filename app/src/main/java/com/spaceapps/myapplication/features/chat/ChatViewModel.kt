package com.spaceapps.myapplication.features.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spaceapps.myapplication.AUTH_HEADER
import com.spaceapps.myapplication.SERVER_SOCKET_URL
import com.spaceapps.myapplication.local.AuthTokenStorage
import com.spaceapps.myapplication.utils.async
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import okhttp3.*
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val authTokenStorage: AuthTokenStorage,
    private val okHttpClient: OkHttpClient
) : ViewModel() {

    private var webSocket: WebSocket? = null
    private val receivedMessages = MutableSharedFlow<String>()
    private val sentMessages = MutableSharedFlow<String>()

    init {
        async { connectToSocket() }
    }

    private suspend fun connectToSocket() {
        val request = Request.Builder().apply {
            authTokenStorage.getAuthToken()?.let { token -> addHeader(AUTH_HEADER, token) }
        }.url(SERVER_SOCKET_URL).build()
        webSocket = okHttpClient.newWebSocket(
            request,
            object : WebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: Response) {
                    val token = runBlocking { authTokenStorage.getAuthToken() }
                    webSocket.send(token ?: return)
                    collectSentMessages()
                }

                override fun onMessage(webSocket: WebSocket, text: String) = runBlocking {
                    Timber.d(text)
                    receivedMessages.emit(text)
                }

                override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                    val token = runBlocking { authTokenStorage.getAuthToken() }
                    webSocket.send(token ?: return)
                }
            }
        )
    }

    private fun collectSentMessages() {
        sentMessages.onEach {
            webSocket?.send(it)
        }.launchIn(viewModelScope)
    }

    private fun sendMessage(text: String) = async {
        sentMessages.emit(text)
    }
}
