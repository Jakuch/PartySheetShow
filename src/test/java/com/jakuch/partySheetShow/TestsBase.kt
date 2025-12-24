package com.jakuch.partySheetShow

import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestsConfig::class)
abstract class TestsBase {

    @Test
    fun contextLoads() {
    }
}