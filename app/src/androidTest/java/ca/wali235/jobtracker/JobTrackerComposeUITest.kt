package ca.wali235.jobtracker

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import ca.wali235.jobtracker.ui.theme.JobTrackerTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

// these are compose ui tests. they run on the emulator.
// they test what the user sees on the screen.
class JobTrackerComposeUITest {

    // this rule starts a small compose environment for each test
    @get:Rule
    val composeTestRule = createComposeRule()

    // test 1: check that a simple text shows up in our theme
    @Test
    fun greetingText_isDisplayed() {
        composeTestRule.setContent {
            JobTrackerTheme {
                Text(text = "Welcome back")
            }
        }
        // look for the text on screen
        composeTestRule.onNodeWithText("Welcome back").assertIsDisplayed()
    }

    // test 2: check that clicking a button runs the click action
    @Test
    fun buttonClick_triggersAction() {
        var clickedCount = 0
        composeTestRule.setContent {
            JobTrackerTheme {
                Button(onClick = { clickedCount++ }) {
                    Text(text = "Add Job")
                }
            }
        }
        // tap the button one time
        composeTestRule.onNodeWithText("Add Job").performClick()
        // check the action ran
        assertEquals(1, clickedCount)
    }

    // test 3: check that multiple text items all show on the screen
    @Test
    fun multipleTexts_areAllDisplayed() {
        composeTestRule.setContent {
            JobTrackerTheme {
                Column {
                    Text(text = "Your Jobs")
                    Text(text = "Lead")
                    Text(text = "Active")
                    Text(text = "Done")
                }
            }
        }
        composeTestRule.onNodeWithText("Your Jobs").assertIsDisplayed()
        composeTestRule.onNodeWithText("Lead").assertIsDisplayed()
        composeTestRule.onNodeWithText("Active").assertIsDisplayed()
        composeTestRule.onNodeWithText("Done").assertIsDisplayed()
    }

    // test 4: check that a button stays clickable after multiple clicks
    @Test
    fun button_canBeClickedMultipleTimes() {
        var count = 0
        composeTestRule.setContent {
            JobTrackerTheme {
                Button(onClick = { count++ }) {
                    Text(text = "Save")
                }
            }
        }
        // click 3 times
        composeTestRule.onNodeWithText("Save").performClick()
        composeTestRule.onNodeWithText("Save").performClick()
        composeTestRule.onNodeWithText("Save").performClick()
        // should be 3 now
        assertTrue("button must work every time", count == 3)
    }
}