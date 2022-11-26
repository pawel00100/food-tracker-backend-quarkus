package org.acme.dao

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import org.acme.model.Meal
import java.time.Instant
import javax.enterprise.context.ApplicationScoped
import javax.persistence.Entity
import javax.transaction.Transactional

interface MealDAO : AbstractDAO<Meal> {
    @Transactional
    fun getFiltered(from: Instant, to: Instant): List<Meal>

}

@ApplicationScoped
class MealDAOImpl(mealRepository: MealRepository) : AbstractDAOImpl<PersistableMeal, Meal>(mealRepository), MealDAO {
    override fun baseToDaoType(base: Meal): PersistableMeal {
        return PersistableMeal(base)
    }

    @Transactional
    override fun getFiltered(from: Instant, to: Instant) = getAll().filter { it.date in from..to } //TODO: change to faster query
}

@ApplicationScoped
class MealRepository : PanacheRepository<PersistableMeal>

@Entity
class PersistableMeal() : AbstractDAOType<Meal, PersistableMeal>() {
    constructor(id: Long?, name: String, kcal: Int?, kcalExpression: String?, date: Instant, exercise: Boolean) : this() {
        this.setId(id)
        this.name = name
        this.kcal = kcal
        this.kcalExpression = kcalExpression
        this.date = date
        this.exercise = exercise
    }

    constructor(meal: Meal) : this(meal.id, meal.name, meal.kcal, meal.kcalExpression, meal.date, meal.exercise)

    lateinit var name: String
    var kcal: Int? = null
    var kcalExpression: String? = null
    lateinit var date: Instant
    var exercise: Boolean = false

    override fun asBase(): Meal? {
        return kcal?.let { Meal(getId(), name, it, kcalExpression, date, exercise) }
    }

    override fun setAll(source: Meal) {
        name = source.name
        kcal = source.kcal
        kcalExpression = source.kcalExpression
        date = source.date
        exercise = source.exercise
    }

    override fun fromBase(base: Meal): PersistableMeal {
        return PersistableMeal(base)
    }
}