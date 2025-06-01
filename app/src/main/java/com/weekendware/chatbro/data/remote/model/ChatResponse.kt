package com.weekendware.chatbro.data.remote.model

data class ChatResponse(val choices: List<Choice>)
data class Choice(val message: Message)
