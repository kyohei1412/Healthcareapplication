package com.example.kyohei.healthcareapplication

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class HealthRecord : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()

    var message: String = ""      // 既存があるなら残してOK
    var createdAt: Long = 0L

    var heightCm: Int = 0         // 身長(cm)
    var weightKg: Float = 0f      // 体重(kg)
    var max: Int = 0     // 最高血圧
    var min: Int = 0     // 最低血圧
    var pulse: Int = 0   // 脈拍

}
