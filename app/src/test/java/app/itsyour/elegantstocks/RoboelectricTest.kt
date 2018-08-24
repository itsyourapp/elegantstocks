package app.itsyour.elegantstocks

import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest="AndroidManifest.xml", constants = BuildConfig::class, sdk = [27], application = TestApp::class)
abstract class RobolectricTest {
    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }
}