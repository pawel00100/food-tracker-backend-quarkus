package org.acme.model

import java.time.Instant

class Meal(id: Long?, val name: String, val kcal: Int?, val kcalExpression: String?, val date: Instant, val exercise: Boolean) : AbstractModelType<Meal>(id) {
    override fun withId(id: Long?) = Meal(id, name, kcal, kcalExpression, date, exercise)
    fun withKcal(kcal: Int?) = Meal(id, name, kcal, kcalExpression, date, exercise)
}