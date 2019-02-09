package com.domclick.config.security.acl

import com.domclick.repository.acl.AnswerRepository
import com.domclick.test_config.AclTestConfig
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.support.AnnotationConfigContextLoader

@Ignore("fix later")
@RunWith(SpringJUnit4ClassRunner::class)
@ActiveProfiles("test")
@ContextConfiguration(classes = [(AclTestConfig::class)], loader = AnnotationConfigContextLoader::class)
class AclTest {
    @Autowired
    private var answerRepository: AnswerRepository? = null

    @Test
    @WithMockUser(username = "PROJECT_MANAGER")
    fun projectManagerCanWriteEntityWithId_1() {
        val answer = answerRepository!!.findById(1L).get()
        answer.content = "Test"
        answerRepository!!.save(answer)
    }

    @Test(expected = AccessDeniedException::class)
    @WithMockUser(username = "PROJECT_MANAGER")
    fun projectManageCannotWriteEntityWithId_4() {
        val answer = answerRepository!!.findById(4L).get()
        answer.content = "Test"
        answerRepository!!.save(answer)
    }

    @Test
    @WithMockUser(username = "DEVELOPER")
    fun developerCanWriteEntityWithId_5() {
        val answer = answerRepository!!.findById(5L).get()
        answer.content = "Test"
        answerRepository!!.save(answer)
    }

    @Test(expected = AccessDeniedException::class)
    @WithMockUser(username = "DEVELOPER")
    fun developerCannotWriteEntityWithId_2() {
        val answer = answerRepository!!.findById(2L).get()
        answer.content = "Test"
        answerRepository!!.save(answer)
    }

    @Test(expected = AccessDeniedException::class)
    @WithMockUser(username = "TEXT_WRITER")
    fun textWriterCannotWriteEntityWithId_3() {
        val answer = answerRepository!!.findById(3L).get()
        answer.content = "Test"
        answerRepository!!.save(answer)
    }
}