package com.domclick.service

import com.domclick.job.CaiFetchEventsJob
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.support.AnnotationConfigContextLoader
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@RunWith(SpringJUnit4ClassRunner::class)
@ContextConfiguration(
        classes = [ScheduleTestConfig::class],
        loader = AnnotationConfigContextLoader::class
)
class JobManagerTest {

    @Autowired
    lateinit var jobManager: JobManager

    @Rule
    @JvmField
    val expectedException = ExpectedException.none()!!

    @Test
    fun crudTest() {
        val job = jobManager.add(
                CaiFetchEventsJob.TYPE,
                20,
                hashMapOf(
                        CaiFetchEventsJob.START_DATE to "2017-01-01",
                        CaiFetchEventsJob.END_DATE to "2019-01-01"
                )
        )

        assertEquals(CaiFetchEventsJob.TYPE, job.type)
        assertNotNull(job.id)
        assertNotNull(jobManager.find(job.type, listOf(job.id))[0])
        assertTrue(jobManager.delete(job.id, job.type))
    }

    @Test
    fun unknownJobTypeTest() {
        expectedException.expect(IllegalArgumentException::class.java)
        expectedException.expectMessage("Job with type 'unknownType' not found")
        jobManager.add(
                "unknownType",
                20,
                hashMapOf(
                        CaiFetchEventsJob.START_DATE to "2017-01-01",
                        CaiFetchEventsJob.END_DATE to "2019-01-01"
                )
        )
    }

    @Test
    fun deleteUnknownJobTest() {
        assertFalse(jobManager.delete("unknownJob", "unknownType"))
    }
}