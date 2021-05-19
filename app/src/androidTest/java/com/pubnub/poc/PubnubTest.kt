package com.pubnub.poc

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit.SECONDS

@RunWith(AndroidJUnit4::class)
class PubnubTest {
    lateinit var pnUnderTest: PubNub

    val testChannel = UUID.randomUUID().toString()
    val countDownLatch = CountDownLatch(1)

    @Before
    fun setup() {
        val pnConfiguration = PNConfiguration().apply {
            publishKey = "demo-36"
            subscribeKey = "demo-36"
        }

        pnUnderTest = PubNub(pnConfiguration)
    }

    @After
    fun cleanUp() {
        pnUnderTest.unsubscribeAll()
        pnUnderTest.forceDestroy()
    }

    @Test
    fun testScenario() {
        pnUnderTest.addListener(object : SubscribeCallback() {
            override fun status(pubnub: PubNub, pnStatus: PNStatus) = Unit

            override fun message(pubnub: PubNub, pnMessageResult: PNMessageResult) {
                val receivedMessage = pnMessageResult.message
                if (receivedMessage.isJsonPrimitive && receivedMessage.asString == "test") {
                    countDownLatch.countDown()
                }
            }
        })

        pnUnderTest.subscribe(channels = listOf(testChannel))

        pnUnderTest.publish(channel = testChannel, message = "test")
            .sync()

        val awaited = countDownLatch.await(10, SECONDS)

        assertTrue(awaited)
    }
}
