package exception

import java.lang.RuntimeException

class ChatNotFoundException(message:String):RuntimeException(message) {
}