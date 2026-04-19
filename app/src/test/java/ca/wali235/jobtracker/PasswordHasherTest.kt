package ca.wali235.jobtracker

import ca.wali235.jobtracker.utils.PasswordHasher
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

// unit tests for PasswordHasher
class PasswordHasherTest {

    @Test
    fun verify_returns_true_for_correct_password() {
        val stored = PasswordHasher.hash("Secret123")
        assertTrue(PasswordHasher.verify("Secret123", stored))
    }

    @Test
    fun verify_returns_false_for_wrong_password() {
        val stored = PasswordHasher.hash("Secret123")
        assertFalse(PasswordHasher.verify("secret123", stored)) // different case
        assertFalse(PasswordHasher.verify("WrongPass", stored))
        assertFalse(PasswordHasher.verify("", stored))
    }

    @Test
    fun same_password_gives_different_hash_because_of_salt() {
        val a = PasswordHasher.hash("Secret123")
        val b = PasswordHasher.hash("Secret123")
        // the two hashes should be different
        assertNotEquals(a, b)
        // but both should still verify ok
        assertTrue(PasswordHasher.verify("Secret123", a))
        assertTrue(PasswordHasher.verify("Secret123", b))
    }
}