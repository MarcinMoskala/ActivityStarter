@file:Suppress("IllegalIdentifier")

package activitystarter.wrapper

import activitystarter.ActivityStarter
import org.junit.Test

class WrapperManagerBuildTest {

    @Test fun `ArgWrappers can be defined using Builder`(){
        ActivityStarter.setWrapperManager(WrapperManager.Builder().build())
    }
}