package ca.wali235.jobtracker

import ca.wali235.jobtracker.utils.Validators
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

// unit tests for Validators
// these run on jvm so no emulator needed
class ValidatorsTest {

    // --- email tests ---

    @Test
    fun valid_email_returns_true() {
        assertTrue(Validators.isValidEmail("wisal@bvc.ca"))
        assertTrue(Validators.isValidEmail("user.name+tag@example.com"))
    }

    @Test
    fun invalid_email_returns_false() {
        assertFalse(Validators.isValidEmail(""))
        assertFalse(Validators.isValidEmail("no-at-sign"))
        assertFalse(Validators.isValidEmail("missing@tld"))
        assertFalse(Validators.isValidEmail("@example.com"))
    }

    // --- password tests ---

    @Test
    fun good_password_is_valid() {
        // must have 6 chars, one letter and one number
        assertTrue(Validators.isValidPassword("abc123"))
        assertTrue(Validators.isValidPassword("SuperTara1"))
    }

    @Test
    fun short_password_is_invalid() {
        assertFalse(Validators.isValidPassword(""))
        assertFalse(Validators.isValidPassword("ab12"))
    }

    @Test
    fun password_without_letter_or_without_number_is_invalid() {
        assertFalse(Validators.isValidPassword("12345678"))   // no letter
        assertFalse(Validators.isValidPassword("onlyletters")) // no number
    }

    // --- job tests ---

    @Test
    fun job_with_all_fields_filled_is_valid() {
        assertTrue(
            Validators.isValidJob(
                clientName = "Acme Corp",
                phone = "5875550100",
                location = "Calgary",
                jobType = "Plumbing"
            )
        )
    }

    @Test
    fun job_with_empty_field_is_invalid() {
        assertFalse(Validators.isValidJob("", "555", "Calgary", "Plumbing"))
        assertFalse(Validators.isValidJob("Acme", "", "Calgary", "Plumbing"))
        assertFalse(Validators.isValidJob("Acme", "555", "", "Plumbing"))
        assertFalse(Validators.isValidJob("Acme", "555", "Calgary", ""))
        // only spaces also counts as empty
        assertFalse(Validators.isValidJob("   ", "555", "Calgary", "Plumbing"))
    }

    // --- status tests ---

    @Test
    fun allowed_statuses_match_proposal() {
        // proposal says Lead, Quote, Active, Done
        assertEquals(listOf("Lead", "Quote", "Active", "Done"), Validators.ALLOWED_STATUSES)
    }

    @Test
    fun is_valid_status_accepts_allowed_and_rejects_others() {
        assertTrue(Validators.isValidStatus("Lead"))
        assertTrue(Validators.isValidStatus("Done"))
        assertFalse(Validators.isValidStatus("Pending"))
        assertFalse(Validators.isValidStatus(""))
        assertFalse(Validators.isValidStatus("done")) // small letters not allowed
    }
}