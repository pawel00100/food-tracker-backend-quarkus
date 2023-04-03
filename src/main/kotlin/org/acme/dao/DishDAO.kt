package org.acme.dao

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import org.acme.model.Dish
import java.time.Instant
import javax.enterprise.context.ApplicationScoped
import javax.persistence.*

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
    constructor(
        id: Long?,
        name: String,
        kcal: Int?,
        kcalExpression: String?,
        created: Instant,
        barcode: Long?
    ) : this() {
        this.setId(id)
        this.name = name
        this.kcal = kcal
        this.kcalExpression = kcalExpression
        this.created = created
        this.barcode = barcode
    }

    constructor(dish: Dish) : this(dish.id, dish.name, dish.kcal, dish.kcalExpression, dish.created, dish.barcode)

    @Id
    @SequenceGenerator(name = "dish_id_seq", sequenceName = "dish_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dish_id_seq")
    private var id: Long? = null
    lateinit var name: String
    var kcal: Int? = null
    var kcalExpression: String? = null
    lateinit var created: Instant
    var barcode: Long? = null

    override fun asBase(): Dish? {
        return kcal?.let { Dish(getId(), name, it, kcalExpression, created, barcode) }
    }

    override fun setAll(source: Dish) {
        name = source.name
        kcal = source.kcal
        kcalExpression = source.kcalExpression
        barcode = source.barcode
    }

    override fun fromBase(base: Dish): PersistableDish {
        return PersistableDish(base)
    }

    override fun getId() = id

    override fun setId(id: Long?) {
        this.id = id
    }
}