package com.example.run_tracker_native_app.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.run_tracker_native_app.dataclass.ChatMessage
import com.example.run_tracker_native_app.dataclass.ChatRequest
import com.example.run_tracker_native_app.interfaces.GroqApi
import com.example.run_tracker_native_app.interfaces.GroqApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel(private val api: GroqApi = GroqApiClient.api) : ViewModel() {
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun sendMessage(message: String, isFirst: Boolean = false) {
        val request = ChatRequest(
            model = "gpt-3.5-turbo",
            messages = listOf(ChatMessage("user", message))
        )

        viewModelScope.launch {
            try {
                _isLoading.value = true
                if (!isFirst) {
                    _messages.value += ChatMessage("user", message)
                }
                val res = api.chat(
                    url = "chat/completions",
                    body = request
                )
                val reply = res.choices.firstOrNull()?.message
                    ?: ChatMessage("assistant", "No response")
                _messages.value += reply
                _isLoading.value = false
            } catch (e: Exception) {
                _messages.value = _messages.value + ChatMessage("assistant", "Error: ${e.message}")
            }
        }
    }
}