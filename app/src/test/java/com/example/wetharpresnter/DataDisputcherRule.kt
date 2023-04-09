package com.example.wetharpresnter

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class MainDispatchersRule (val testCorotineDispatcher: TestCoroutineDispatcher =TestCoroutineDispatcher()):
    TestWatcher(), TestCoroutineScope by TestCoroutineScope(testCorotineDispatcher) {


    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(testCorotineDispatcher)
    }

    override fun finished(description: Description) {
        super.finished(description)
        testCorotineDispatcher.cleanupTestCoroutines()
    }
}