package ca.wali235.jobtracker.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import ca.wali235.jobtracker.data.model.UserEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

// tests for UserDao using in memory database
// we do not touch the real phone storage
@RunWith(AndroidJUnit4::class)
class UserDaoTest {

    private lateinit var db: JobDatabase
    private lateinit var dao: UserDao

    @Before
    fun setUp() {
        // build a fake database in memory
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        db = Room.inMemoryDatabaseBuilder(context, JobDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.userDao()
    }

    @After
    fun tearDown() {
        // close the fake database after each test
        db.close()
    }

    @Test
    fun insertUser_canBeFoundByEmail() = runBlocking {
        // add a user then find by email
        dao.insertUser(UserEntity(email = "wisal@bvc.ca", password = "pw123"))
        val user = dao.getUserByEmail("wisal@bvc.ca")
        assertNotNull(user)
        assertEquals("wisal@bvc.ca", user?.email)
    }

    @Test
    fun getUserByEmail_returnsNullWhenMissing() = runBlocking {
        // unknown email should give null
        val user = dao.getUserByEmail("nobody@nowhere.com")
        assertNull(user)
    }

    @Test
    fun loginUser_returnsUserOnCorrectCredentials() = runBlocking {
        // right email and right password means login works
        dao.insertUser(UserEntity(email = "wisal@bvc.ca", password = "pw123"))
        val user = dao.loginUser("wisal@bvc.ca", "pw123")
        assertNotNull(user)
    }

    @Test
    fun loginUser_returnsNullOnWrongPassword() = runBlocking {
        // wrong password should give null
        dao.insertUser(UserEntity(email = "wisal@bvc.ca", password = "pw123"))
        val user = dao.loginUser("wisal@bvc.ca", "wrong")
        assertNull(user)
    }
}