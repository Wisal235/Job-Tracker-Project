package ca.wali235.jobtracker.utils

// simple validation helpers for forms
// used in sign up and add job screens

object Validators {

    // email pattern (basic check)
    private val EMAIL_REGEX = Regex(
        "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    )

    // check if email looks ok
    fun isValidEmail(email: String): Boolean {
        if (email.isBlank()) return false
        return EMAIL_REGEX.matches(email.trim())
    }

    // password must be 6 chars or more
    // and contain at least one letter and one number
    fun isValidPassword(password: String): Boolean {
        if (password.length < 6) return false
        val hasLetter = password.any { it.isLetter() }
        val hasDigit = password.any { it.isDigit() }
        return hasLetter && hasDigit
    }

    // job is valid if all 4 fields are filled
    fun isValidJob(
        clientName: String,
        phone: String,
        location: String,
        jobType: String
    ): Boolean {
        return clientName.isNotBlank() &&
                phone.isNotBlank() &&
                location.isNotBlank() &&
                jobType.isNotBlank()
    }

    // allowed status values
    val ALLOWED_STATUSES = listOf("Lead", "Quote", "Active", "Done")

    fun isValidStatus(status: String): Boolean = status in ALLOWED_STATUSES
}