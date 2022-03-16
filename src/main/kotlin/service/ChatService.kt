package service

import exception.ChatNotFoundException
import exception.MessageNotFoundException
import model.Message
import repository.ChatRepository

object ChatService {
    private val chatRepository = ChatRepository()

    fun getUnreadChatsCount(): Int {
        var count = 0
        chatRepository.getChatList()?.forEach { s ->
            if (s.value.any { (!it.wasRead) }) {
                count++
            }
        }
        return count
    }


    fun clearAllChats() {
        chatRepository.clearAllChats()
    }


    fun getChats(): MutableList<MutableList<Message>> {
        val newList = mutableListOf<MutableList<Message>>()
        chatRepository.getChatList()?.filter { s -> s.value.isNotEmpty() }?.forEach { s -> newList.add(s.value) }
        newList.ifEmpty { throw MessageNotFoundException("No messages") }
        return newList
    }


    fun getMessages(chatId: Int, messageId: Int, messagesCount: Int): List<Message>? {
        var messageList = chatRepository.getMessagesByChatId(chatId)
        val newMessageList = messageList?.subList((messageId - 1), messageList.lastIndex)
        return newMessageList?.subList((newMessageList.lastIndex.minus(messagesCount)), newMessageList.lastIndex)
    }

    fun createNewMessage(chatId: Int, message: Message) {
        chatRepository.getMessagesByChatId(chatId)?.add(message)
    }

    fun deleteMessage(chatId: Int, messageId: Int) {
        if (chatRepository.getMessagesByChatId(chatId).isNullOrEmpty()) {
            throw ChatNotFoundException("Chat not found")
        }
        if (messageId == chatRepository.getMessagesByChatId(chatId)?.lastIndex) {
            chatRepository.deleteAllMessagesByChatId(chatId)
        } else {
            chatRepository.deleteMessage(chatId, messageId)
        }
    }

    fun createChat(message: Message): Int {
        var newChatId = chatRepository.chatListSize() + 1
        val newMessagesList = mutableListOf(message)
        chatRepository.putInChatList(newChatId, newMessagesList)
        return newChatId
    }

    fun deleteChat(chatId: Int) {
        chatRepository.deleteAllMessagesByChatId(chatId)
    }

    fun addMessagesByChatId(chatId: Int, message: Message) {
        chatRepository.addMessagesByChatId(chatId, message)
    }

    fun getMessagesByChatId(chatId: Int): MutableList<Message>? {
        return chatRepository.getMessagesByChatId(chatId)
    }

    fun editMessageBy(chatId: Int, messageId: Int, message: Message) {
        chatRepository.editMessageBy(chatId, messageId, message)
    }
}