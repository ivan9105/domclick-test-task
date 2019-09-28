package com.domclick.container

import org.testcontainers.containers.JdbcDatabaseContainer
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy
import java.time.Duration
import java.time.temporal.ChronoUnit.SECONDS

class PostgreSQLContainer @JvmOverloads constructor(dockerImageName: String = "$IMAGE:$DEFAULT_TAG")
    : JdbcDatabaseContainer<PostgreSQLContainer>(dockerImageName)
{
    private var databaseName: String? = null
    private var username: String? = null
    private var password: String? = null

    init {
        this.waitStrategy = LogMessageWaitStrategy()
                .withRegEx(".*database system is ready to accept connections.*\\s")
                .withTimes(2)
                .withStartupTimeout(Duration.of(60, SECONDS))
    }

    override fun getLivenessCheckPorts() = hashSetOf(getMappedPort(DEFAULT_PORT)!!)

    override fun configure() {
        addExposedPort(DEFAULT_PORT)

        addEnv(PGPORT_KEY, DEFAULT_PORT.toString())
        addEnv(POSTGRES_DB_KEY, databaseName)
        addEnv(POSTGRES_USER_KEY, username)
        addEnv(POSTGRES_PASSWORD_KEY, password)

        setCommand("postgres", "-c", FSYNC_OFF_OPTION)
    }

    override fun getDriverClassName() = "org.postgresql.Driver"

    override fun getJdbcUrl() = "jdbc:postgresql://" + containerIpAddress + ":" + getMappedPort(DEFAULT_PORT) + "/" + databaseName

    override fun getDatabaseName() = databaseName

    override fun getUsername() = username

    override fun getPassword() = password

    public override fun getTestQueryString() ="SELECT 1"

    override fun withDatabaseName(databaseName: String?): PostgreSQLContainer {
        this.databaseName = databaseName
        return self()
    }

    override fun withUsername(username: String?): PostgreSQLContainer {
        this.username = username
        return self()
    }

    override fun withPassword(password: String?): PostgreSQLContainer {
        this.password = password
        return self()
    }

    override fun waitUntilContainerStarted() {
        getWaitStrategy().waitUntilReady(this)
    }

    companion object {
        private val IMAGE = "postgres"
        private val DEFAULT_TAG = "10.10-alpine"

        private val PGPORT_KEY = "PGPORT"
        private val POSTGRES_DB_KEY = "POSTGRES_DB"
        private val POSTGRES_USER_KEY = "POSTGRES_USER"
        private val POSTGRES_PASSWORD_KEY = "POSTGRES_PASSWORD"

        private val DEFAULT_PORT = 5433
        private val FSYNC_OFF_OPTION = "fsync=off"
    }
}
