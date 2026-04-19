package ca.wali235.jobtracker.utils

import java.security.MessageDigest
import java.security.SecureRandom

// hash passwords before saving to database
// so we dont store plain text password
// format is saltHex:hashHex

object PasswordHasher {

    private const val SALT_BYTES = 16

    // create new salt and hash the password
    fun hash(password: String): String {
        val salt = ByteArray(SALT_BYTES).also { SecureRandom().nextBytes(it) }
        val hash = sha256(salt + password.toByteArray(Charsets.UTF_8))
        return "${salt.toHex()}:${hash.toHex()}"
    }

    // check if password matches the stored hash
    fun verify(password: String, stored: String): Boolean {
        val parts = stored.split(":")
        if (parts.size != 2) return false
        val salt = parts[0].hexToBytes() ?: return false
        val expected = parts[1].hexToBytes() ?: return false
        val actual = sha256(salt + password.toByteArray(Charsets.UTF_8))
        if (actual.size != expected.size) return false
        var diff = 0
        for (i in actual.indices) diff = diff or (actual[i].toInt() xor expected[i].toInt())
        return diff == 0
    }

    // sha256 helper
    private fun sha256(data: ByteArray): ByteArray =
        MessageDigest.getInstance("SHA-256").digest(data)

    // convert bytes to hex string
    private fun ByteArray.toHex(): String = joinToString("") { "%02x".format(it) }

    // convert hex string back to bytes
    private fun String.hexToBytes(): ByteArray? {
        if (length % 2 != 0) return null
        return ByteArray(length / 2) { i ->
            val hi = substring(i * 2, i * 2 + 1).toIntOrNull(16) ?: return null
            val lo = substring(i * 2 + 1, i * 2 + 2).toIntOrNull(16) ?: return null
            ((hi shl 4) or lo).toByte()
        }
    }
}