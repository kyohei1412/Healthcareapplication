package com.example.kyohei.healthcareapplication

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

object RealmProvider {

    val realm: Realm by lazy {
        val config = RealmConfiguration.Builder(
            schema = setOf(HealthRecord::class)
        ).build()

        Realm.open(config)
    }
}
