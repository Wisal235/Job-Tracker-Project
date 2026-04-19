package ca.wali235.jobtracker

import ca.wali235.jobtracker.navigation.Routes
import org.junit.Assert.assertEquals
import org.junit.Test

// unit tests for Routes class
// check that navigation routes are built correctly
class RoutesTest {

    @Test
    fun job_details_route_has_correct_id() {
        assertEquals("job_details/42", Routes.JobDetails.createRoute(42))
        assertEquals("job_details/0", Routes.JobDetails.createRoute(0))
    }

    @Test
    fun edit_job_route_has_correct_id() {
        assertEquals("edit_job/7", Routes.EditJob.createRoute(7))
        assertEquals("edit_job/-1", Routes.EditJob.createRoute(-1))
    }
}