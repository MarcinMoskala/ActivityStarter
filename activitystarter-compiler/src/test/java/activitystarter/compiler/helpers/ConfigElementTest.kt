package activitystarter.compiler.helpers

import activitystarter.ActivityStarterConfig
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ConfigElementTest {

    @Test fun `ConfigElement returns objects that are ActivityStarterConfig`() {
        assertTrue (ConfigElement.empty is ActivityStarterConfig)
        assertTrue (ConfigElement.singleConverter is ActivityStarterConfig)
    }
}