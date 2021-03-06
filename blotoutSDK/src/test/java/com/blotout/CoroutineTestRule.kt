package com.blotout

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.*
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class CoroutineTestRule(private val testDispatcher: TestCoroutineDispatcher= TestCoroutineDispatcher()):TestWatcher() {

    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    fun runBlockingTest(block:suspend TestCoroutineScope.()->Unit){
        testDispatcher.runBlockingTest{
            block()
        }
    }
}