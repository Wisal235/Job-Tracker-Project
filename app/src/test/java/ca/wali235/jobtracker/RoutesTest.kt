package ca.wali235.jobtracker.navigation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

// unit tests for our sealed Routes class (runs on JVM no emulator needed)
class RoutesTest {

    // --- tests for routes that take an id parameter ---

    @Test
    fun `JobDetails createRoute interpolates the job id`() {
        // when we pass 42 we should get job_details/42 back
        assertEquals("job_details/42", Routes.JobDetails.createRoute(42))
        assertEquals("job_details/0", Routes.JobDetails.createRoute(0))
    }

    @Test
    fun `EditJob createRoute interpolates the job id`() {
        // same thing but for edit route
        assertEquals("edit_job/7", Routes.EditJob.createRoute(7))
        assertEquals("edit_job/-1", Routes.EditJob.createRoute(-1))
    }

    @Test
    fun `JobDetails route pattern has a placeholder`() {
        // the pattern itself should contain {jobId} so nav graph can grab it
        assertTrue(Routes.JobDetails.route.contains("{jobId}"))
    }

    @Test
    fun `EditJob route pattern has a placeholder`() {
        assertTrue(Routes.EditJob.route.contains("{jobId}"))
    }

    // --- tests for the simple static routes ---

    @Test
    fun `Landing route string is landing`() {
        assertEquals("landing", Routes.Landing.route)
    }

    @Test
    fun `Login route string is login`() {
        assertEquals("login", Routes.Login.route)
    }

    @Test
    fun `SignUp route string is signup`() {
        assertEquals("signup", Routes.SignUp.route)
    }

    @Test
    fun `Main route string is main`() {
        // main is the new route that holds the bottom nav (jobs follow ups profile)
        assertEquals("main", Routes.Main.route)
    }

    @Test
    fun `AddEditJob route string is add_edit_job`() {
        assertEquals("add_edit_job", Routes.AddEditJob.route)
    }

    // --- make sure all routes are different from each other ---

    @Test
    fun `all route strings are unique`() {
        // if two routes have the same string the nav graph will break
        val allRoutes = listOf(
            Routes.Landing.route,
            Routes.Login.route,
            Routes.SignUp.route,
            Routes.Main.route,
            Routes.AddEditJob.route,
            Routes.EditJob.route,
            Routes.JobDetails.route
        )
        // compare size of list vs size of set they should be equal if all unique
        assertEquals(allRoutes.size, allRoutes.toSet().size)
    }

    @Test
    fun `different job ids produce different routes`() {
        // just a safety check that createRoute really uses the id
        assertNotEquals(
            Routes.JobDetails.createRoute(1),
            Routes.JobDetails.createRoute(2)
        )
    }
}