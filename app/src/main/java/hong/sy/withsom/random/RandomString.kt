package hong.sy.withsom.random

import java.util.*

class RandomString {
    private val characterSet = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private val random = Random(System.nanoTime())

    fun getRandomPassword(): String {
        val password = StringBuilder()

        for(i in 0..7) {
            val rIndex = random.nextInt(characterSet.length)
            password.append(characterSet[rIndex])
        }

        return password.toString()
    }

    fun getRandomCertificationNum(): String {
        val certificationNum = StringBuilder()

        for(i in 0..5) {
            val rIndex = random.nextInt(characterSet.length)
            certificationNum.append(characterSet[rIndex])
        }

        return certificationNum.toString()
    }
}