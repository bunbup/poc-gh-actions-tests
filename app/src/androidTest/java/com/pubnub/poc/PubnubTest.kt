package com.pubnub.poc

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.PNPublishResult
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class PubnubTest {
    lateinit var pnUnderTest: PubNub

    val testChannel = UUID.randomUUID().toString()

    @Before
    fun setup() {
        val pnConfiguration = PNConfiguration().apply {
            publishKey = "demo-36"
            subscribeKey = "demo-36"
        }

        pnUnderTest = PubNub(pnConfiguration)
    }

    @Test
    fun testScenario() {
        val publishResult: PNPublishResult? = pnUnderTest.publish(channel = testChannel, message = "test")
            .sync()

        assertNotNull(publishResult?.timetoken)
    }
}
