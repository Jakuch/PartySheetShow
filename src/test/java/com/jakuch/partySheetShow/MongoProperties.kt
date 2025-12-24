package com.jakuch.partySheetShow

import org.springframework.boot.context.properties.ConfigurationProperties


@ConfigurationProperties(prefix = "spring.data.mongodb")
class MongoProperties(
        val database: String
) {
}