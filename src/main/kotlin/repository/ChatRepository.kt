package repository

import exception.ChatNotFoundException
import exception.MessageNotFoundException
import model.Message

class ChatRepository {
    private var chatList = mutableMapOf<Int, MutableList<Message>>()

    fun getChatList(): Map<Int, MutableList<Message>>? {
        return chatList
    }


    fun putInChatList(chatId: Int, mesList: MutableList<Message>) {
        chatList.put(chatId, mesList)
    }

    fun chatListSize(): Int {
        return chatList.size
    }

    fun clearAllChats() {
        chatList.clear()
    }

    fun getMessagesByChatId(chatId: Int): MutableList<Message>? {
        return if (chatList[chatId]?.isEmpty() == true) throw ChatNotFoundException("Chat not found")
        else {
            chatList[chatId]?.forEach { message -> message.wasRead = true }
            chatList[chatId]
        }

    }

    fun deleteMessage(chatId: Int, messageId: Int) {
        try {
            getMessagesByChatId(chatId)?.get(messageId) ?: throw MessageNotFoundException("Message not found")
        } catch (e: IndexOutOfBoundsException) {
            throw MessageNotFoundException("Message not found")
        }
        getMessagesByChatId(chatId)?.removeAt(messageId)
    }

    fun deleteAllMessagesByChatId(chatId: Int) {
        if (chatList[chatId].isNullOrEmpty()) throw ChatNotFoundException("Chat not found")
        chatList[chatId]?.clear()
    }

    fun addMessagesByChatId(chatId: Int, message: Message) {
        if (chatList[chatId]?.isEmpty() == true) throw ChatNotFoundException("Chat not found")
        chatList[chatId]?.add(message)
    }

    fun editMessageBy(chatId: Int, messageId: Int, message: Message) {
        if (chatList[chatId]?.isEmpty() == true) throw ChatNotFoundException("Chat not found")
        if (chatList[chatId]?.get(messageId) == null) throw MessageNotFoundException("Message not found")
        chatList[chatId]?.set(messageId, message)
    }

}