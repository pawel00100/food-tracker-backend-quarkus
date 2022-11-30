package org.acme.dao

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import org.acme.model.Dish
import java.time.Instant
import javax.enterprise.context.ApplicationScoped
import javax.persistence.Entity
import javax.persistence.Table

interface DishDAO : AbstractDAO<Dish> {
}

@ApplicationScoped
class DishDAOImpl(dishRepository: DishRepository) : AbstractDAOImpl<PersistableDish, Dish>(dishRepository), DishDAO {
    override fun baseToDaoType(base: Dish): PersistableDish {
        return PersistableDish(base)
    }
}

@ApplicationScoped
class DishRepository : PanacheRepository<PersistableDish>

@Entity
@Table(name = "dish")
class PersistableDish() : AbstractDAOType<Dish, PersistableDish>() {
    constructor(id: Long?, name: String, kcal: Int?, kcalExpression: String?, created: Instant) : this() {
        this.setId(id)
        this.name = name
        this.kcal = kcal
        this.kcalExpression = kcalExpression
        this.created = created
    }

    constructor(dish: Dish) : this(dish.id, dish.name, dish.kcal, dish.kcalExpression, dish.created)

    lateinit var name: String
    var kcal: Int? = null
    var kcalExpression: String? = null
    lateinit var created: Instant

    override fun asBase(): Dish? {
        return kcal?.let { Dish(getId(), name, it, kcalExpression, created) }
    }

    override fun setAll(source: Dish) {
        name = source.name
        kcal = source.kcal
        kcalExpression = source.kcalExpression
    }

    override fun fromBase(base: Dish): PersistableDish {
        return PersistableDish(base)
    }
}