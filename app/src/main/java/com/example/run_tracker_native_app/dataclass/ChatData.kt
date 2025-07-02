package com.example.run_tracker_native_app.dataclass

data class ChatRequest(
    val model: String,
    val messages: List<ChatMessage>
)

data class ChatMessage(
    val role: String,
    val content: String
)

data class ChatResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: ChatMessage
)