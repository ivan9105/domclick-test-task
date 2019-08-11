package com.domclick.repository

import com.domclick.entity.oauth2.DepartmentEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface DepartmentRepository : CrudRepository<DepartmentEntity, Long>