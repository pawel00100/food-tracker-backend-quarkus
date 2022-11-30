package org.acme.model

import java.time.Instant

class Dish(id: Long?, val name: String, val kcal: Int?, val kcalExpression: String?, val created: Instant) : AbstractModelType<Dish>(id) {
    override fun withId(id: Long?) = Dish(id, name, kcal, kcalExpression, created)
    fun withKcal(kcal: Int?) = Dish(id, name, kcal, kcalExpression, created)
}