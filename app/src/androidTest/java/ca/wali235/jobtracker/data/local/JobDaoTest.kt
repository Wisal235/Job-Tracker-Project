package ca.wali235.jobtracker.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import ca.wali235.jobtracker.data.model.JobEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

// tests for JobDao using a small in memory database
// so we do not touch the real phone storage
@RunWith(AndroidJUnit4::class)
class JobDaoTest {

    private lateinit var db: JobDatabase
    private lateinit var dao: JobDao

    @Before
    fun setUp() {
        // make a fake database that lives only in memory
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        db = Room.inMemoryDatabaseBuilder(context, JobDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.jobDao()
    }

    @After
    fun tearDown() {
        // close the fake database after each test
        db.close()
    }

    // small helper to build a job quickly
    private fun sampleJob(
        userId: Int = 1,
        client: String = "Acme",
        status: String = "Lead"
    ) = JobEntity(
        userId = userId,
        clientName = client,
        location = "Calgary",
        phone = "5875550100",
        jobType = "Plumbing",
        status = status,
        followUpDate = ""
    )

    @Test
    fun insertJob_thenQueryByUser_returnsIt() = runBlocking {
        // add one job then read it back
        dao.insertJob(sampleJob(userId = 1, client = "Alpha"))
        val list = dao.getJobsByUser(1).first()
        assertEquals(1, list.size)
        assertEquals("Alpha", list[0].clientName)
    }

    @Test
    fun queryByUser_onlyReturnsJobsForThatUser() = runBlocking {
        // each user should only see their own jobs
        dao.insertJob(sampleJob(userId = 1, client = "Alpha"))
        dao.insertJob(sampleJob(userId = 2, client = "Beta"))
        dao.insertJob(sampleJob(userId = 2, client = "Gamma"))

        val user1 = dao.getJobsByUser(1).first()
        val user2 = dao.getJobsByUser(2).first()
        assertEquals(1, user1.size)
        assertEquals(2, user2.size)
        assertTrue(user2.all { it.userId == 2 })
    }

    @Test
    fun queryByUser_withNoJobs_returnsEmptyList() = runBlocking {
        // new user with no jobs should get empty list
        val list = dao.getJobsByUser(99).first()
        assertTrue(list.isEmpty())
    }

    @Test
    fun updateJob_persistsChanges() = runBlocking {
        // change the status and make sure it saves
        dao.insertJob(sampleJob(userId = 1, client = "Alpha", status = "Lead"))
        val saved = dao.getJobsByUser(1).first()[0]
        dao.updateJob(saved.copy(status = "Done"))
        val updated = dao.getJobsByUser(1).first()[0]
        assertEquals("Done", updated.status)
    }

    @Test
    fun deleteJob_removesTheRow() = runBlocking {
        // delete a job and list should be empty
        dao.insertJob(sampleJob(userId = 1, client = "ToDelete"))
        val saved = dao.getJobsByUser(1).first()[0]
        dao.deleteJob(saved)
        val list = dao.getJobsByUser(1).first()
        assertTrue(list.isEmpty())
    }

    @Test
    fun insertJob_replacesOnSamePrimaryKey() = runBlocking {
        // inserting with same id should replace the old one
        val first = sampleJob(userId = 1, client = "Alpha").copy(id = 5)
        dao.insertJob(first)
        val second = first.copy(clientName = "AlphaRenamed")
        dao.insertJob(second)
        val list = dao.getJobsByUser(1).first()
        assertEquals(1, list.size)
        assertEquals("AlphaRenamed", list[0].clientName)
    }
}