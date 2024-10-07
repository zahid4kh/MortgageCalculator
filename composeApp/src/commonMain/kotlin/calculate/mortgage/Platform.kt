package calculate.mortgage

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform