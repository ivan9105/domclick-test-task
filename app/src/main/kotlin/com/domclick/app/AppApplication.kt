package com.domclick.app

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication(exclude = [(SecurityAutoConfiguration::class)])
@ComponentScan(value = ["com.domclick"])
class AppApplication {

    @Bean
    fun commandLineRunner(ctx: ApplicationContext): CommandLineRunner {
        return CommandLineRunner {
            println("Go to admin page http://127.0.0.1:8080")
            println("Edit user list http://127.0.0.1:8080/users")
            println("Edit account list http://127.0.0.1:8080/accounts")
            println("Show swagger for test api http://127.0.0.1:8080/swagger-ui.html")
            println(System.getProperty("file.encoding"))
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(AppApplication::class.java)
        }
    }
}
