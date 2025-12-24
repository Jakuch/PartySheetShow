package com.jakuch.PartySheetShow

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import de.bwaldvogel.mongo.MongoServer
import de.bwaldvogel.mongo.backend.memory.MemoryBackend
import lombok.AllArgsConstructor
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.core.MongoTemplate

@TestConfiguration
@EnableConfigurationProperties(MongoProperties::class)
@AllArgsConstructor
open class TestsConfig(
       private val mongoProperties: MongoProperties
) {

    @Bean
    open fun mongoServer(): MongoServer {
        val server = MongoServer(MemoryBackend())
        server.bind("127.0.0.1", 0)
        return server
    }

    @Bean
    open fun mongoClient(mongoServer: MongoServer): MongoClient {
        val localAddress = mongoServer.localAddress
        val uri = String.format("mongodb://%s:%d", localAddress.hostString, localAddress.port)
        return MongoClients.create(uri)
    }

    @Bean
    open fun mongoTemplate(client: MongoClient): MongoTemplate {
        return MongoTemplate(client, mongoProperties.database)
    }
}