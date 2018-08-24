package app.itsyour.elegantstocks

import org.junit.Before
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@Suppress("UNCHECKED_CAST")
open class MockitoTest {

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    protected fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }
    private fun <T> uninitialized(): T = null as T
}