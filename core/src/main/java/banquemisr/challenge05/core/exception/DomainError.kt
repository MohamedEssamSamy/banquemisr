package banquemisr.challenge05.core.exception

sealed class DomainError {
    data class Server(val code:Int,val message: String):DomainError()
    data class Network(var message: String):DomainError()
    data class Unknown(var message: String) : DomainError()
}