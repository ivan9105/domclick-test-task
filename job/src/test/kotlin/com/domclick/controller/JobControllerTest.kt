package com.domclick.controller

import com.domclick.app.JobApplication
import com.domclick.controller.dto.ResponseModel
import com.domclick.service.JobInfo
import com.domclick.service.JobManager
import com.domclick.service.ScheduleTestConfig
import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import org.apache.commons.io.IOUtils
import org.flywaydb.core.Flyway
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.quartz.Trigger
import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONCompareMode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.context.support.AnnotationConfigContextLoader
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.nio.charset.StandardCharsets
import java.util.UUID.randomUUID
import kotlin.test.assertEquals

@RunWith(SpringRunner::class)
@ContextConfiguration(
        classes = [ScheduleTestConfig::class],
        loader = AnnotationConfigContextLoader::class
)
@SpringBootTest(classes = [JobApplication::class], webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
@ImportAutoConfiguration(exclude = [FlywayAutoConfiguration::class])
class JobControllerTest {
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockBean
    lateinit var jobManager: JobManager

    @MockBean
    lateinit var flyway: Flyway

    @Autowired
    lateinit var jobController: JobController

    val addJobJson = ClassPathResource("json/add_job.json")

    @Before
    fun init() {
        mockMvc = MockMvcBuilders.standaloneSetup(jobController).build()
    }

    @Test
    fun crudTest() {
        val expectedJob = JobInfo(
                randomUUID().toString(),
                "CAI_FETCH_EVENTS",
                Trigger.TriggerState.NORMAL)
        Mockito.`when`(jobManager.add(eq("CAI_FETCH_EVENTS"), eq(30), any())).thenReturn(expectedJob)
        Mockito.`when`(jobManager.find(any(), any())).thenReturn(listOf(expectedJob))
        Mockito.`when`(jobManager.delete(any(), any())).thenReturn(true)

        val addResponse = mockMvc.perform(
                MockMvcRequestBuilders.put("/api/job/add")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(IOUtils.toString(addJobJson.inputStream, StandardCharsets.UTF_8))
        ).andReturn().response

        assertEquals(200, addResponse.status)
        JSONAssert.assertEquals(
                objectMapper.writeValueAsString(ResponseModel(true, expectedJob)),
                addResponse.contentAsString,
                JSONCompareMode.LENIENT
        )

        val listResponse = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/job/list")
                        .param("type", "CAI_FETCH_EVENTS")
                        .param("ids", objectMapper.writeValueAsString(listOf(expectedJob.id)))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn().response

        assertEquals(200, listResponse.status)
        JSONAssert.assertEquals(
                objectMapper.writeValueAsString(ResponseModel(true, listOf(expectedJob))),
                listResponse.contentAsString,
                JSONCompareMode.LENIENT
        )

        val deleteResponse = mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/job/delete/${expectedJob.id}")
                        .param("type", "CAI_FETCH_EVENTS")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn().response

        assertEquals(200, listResponse.status)
        JSONAssert.assertEquals(
                objectMapper.writeValueAsString(ResponseModel(true, null)),
                deleteResponse.contentAsString,
                JSONCompareMode.LENIENT
        )
    }
}