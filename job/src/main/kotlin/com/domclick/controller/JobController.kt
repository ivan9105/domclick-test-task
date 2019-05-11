package com.domclick.controller

import com.domclick.controller.dto.JobRequest
import com.domclick.controller.dto.ResponseModel
import com.domclick.service.JobManager
import mu.KLogging
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/job")
class JobController(
        private val jobManager: JobManager
) {
    companion object : KLogging()

    @GetMapping("/list")
    fun jobList(
            @RequestParam("type") type: String?,
            @RequestParam("ids") ids: List<String>?
    ) = ResponseModel(true, jobManager.find(type, ids))

    @DeleteMapping("delete/{id}")
    fun deleteJob(
            @PathVariable(name = "id") id: String,
            @RequestParam("type") type: String?
    ) = ResponseModel(jobManager.delete(id, type), null)

    @PutMapping("/add")
    fun addJob(
            @Valid @RequestBody dto: JobRequest
    ) = ResponseModel(true, jobManager.add(dto.type, dto.intervalInSeconds, dto.params))
}