package com.blipblipcode.distribuidoraayl.data.dto


import com.blipblipcode.distribuidoraayl.data.mapper.Mappable
import com.blipblipcode.distribuidoraayl.domain.models.customer.Activity
import com.google.firebase.firestore.PropertyName

@Suppress("unused")
data class ActivityDto (
    @get:PropertyName("turn")
    @set:PropertyName("turn")
    var turn:String,
    @get:PropertyName("code")
    @set:PropertyName("code")
    var code:Int,
    @get:PropertyName("name")
    @set:PropertyName("name")
    var name:String,
    @get:PropertyName("isMainActivity")
    @set:PropertyName("isMainActivity")
    var isMainActivity:Boolean
): Mappable<Activity> {
    constructor() : this(turn="", code=0, name="", isMainActivity = false)
    override fun mapToDomain(): Activity {
        return Activity(turn, code, name, isMainActivity)
    }
}