import exception.ChatNotFoundException
import exception.MessageNotFoundException
import model.Message
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import service.ChatService

class ChatServiceTests {

    @Before
    fun beforeEach() {
        ChatService.clearAllChats()
    }

    @Test
    fun get_unread_chats_count_test() {
        val message = Message("some text")
        ChatService.createChat(message)
        ChatService.getUnreadChatsCount()
        val expectedCount = 1
        val actualCount = ChatService.getUnreadChatsCount()
        assertEquals(expectedCount, actualCount)
    }

    @Test
    fun success_get_chats_test() {
        val message = Message("some text")
        ChatService.createChat(message)
        val actualChatListSize = ChatService.getChats().size
        val exptectedChatListSize = 1
        assertEquals(exptectedChatListSize, actualChatListSize)
    }

    @Test
    fun success_get_messages_test() {
        val message1 = Message("some text")
        val message2 = Message("some text")
        val message3 = Message("some text")
        val message4 = Message("some text")
        val message5 = Message("some text")
        val message6 = Message("some text")
        val message7 = Message("some text")

        val messList = mutableListOf(message2, message3, message4, message5, message6, message7)

        val chatId = ChatService.createChat(message1)

        messList.forEach { message -> ChatService.createNewMessage(chatId, message) }

        val actualMessListSize = ChatService.getMessages(chatId, 3, 2)?.size
        val expectedMessListSize = 2

        assertEquals(expectedMessListSize, actualMessListSize)
    }

    @Test(expected = ChatNotFoundException::class)
    fun delete_chat_failure_test() {
        ChatService.deleteChat(123)
    }

    @Test(expected = MessageNotFoundException::class)
    fun delete_message_failure_test() {
        val message1 = Message("some text")
        val chatId = ChatService.createChat(message1)

        ChatService.deleteMessage(chatId, 123)
    }

    @Test
    fun create_chat_success_test() {
        val message = Message("some text")
        ChatService.createChat(message)
        ChatService.createChat(message)
        ChatService.createChat(message)

        val actualChatListSize = ChatService.getChats().size
        val expectedChatListSize = 3
        assertEquals(expectedChatListSize, actualChatListSize)
    }

    @Test
    fun add_message_to_chat_success_test() {
        val message = Message("some text")
        val chatId = ChatService.createChat(message)

        val newMessage = Message("other text")
        ChatService.addMessagesByChatId(chatId, newMessage)

        val expectedMessage = ChatService.getMessagesByChatId(chatId)?.get(1)?.text
        val actualMessage = "other text"
        assertEquals(expectedMessage, actualMessage)
    }

    @Test
    fun edit_message_success_test() {
        val message = Message("some text")
        val chatId = ChatService.createChat(message)

        val newMessage = Message("other text")

        ChatService.editMessageBy(chatId, 0, newMessage)

        val expectedMessage = ChatService.getMessagesByChatId(chatId)?.get(0)?.text
        val actualMessage = "other text"
        assertEquals(expectedMessage, actualMessage)

    }

}