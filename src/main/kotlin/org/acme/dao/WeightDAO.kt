package org.acme.dao

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import org.acme.model.Weight
import java.time.Instant
import javax.enterprise.context.ApplicationScoped
import javax.persistence.Entity
import javax.transaction.Transactional

interface WeightDAO : AbstractDAO<Weight> {
    @Transactional
    fun getFiltered(from: Instant, to: Instant): List<Weight>
}


@ApplicationScoped
class WeightDAOImpl(weightRepository: WeightRepository) : AbstractDAOImpl<PersistableWeight, Weight>(weightRepository), WeightDAO {
    override fun baseToDaoType(base: Weight): PersistableWeight {
        return PersistableWeight(base)
    }

    @Transactional
    override fun getFiltered(from: Instant, to: Instant) = getAll().filter { it.date in from..to } //TODO: change to faster query

}

@ApplicationScoped
class WeightRepository : PanacheRepository<PersistableWeight>

@Entity
class PersistableWeight() : AbstractDAOType<Weight, PersistableWeight>() {
    constructor(id: Long?, measurement: Float, date: Instant) : this() {
        this.setId(id)
        this.measurement = measurement
        this.date = date
    }

    constructor(weight: Weight) : this(weight.id, weight.measurement, weight.date)

    var measurement: Float? = null
    lateinit var date: Instant

    override fun asBase(): Weight? {
        return measurement?.let { Weight(getId(), it, date) }
    }

    override fun setAll(source: Weight) {
        measurement = source.measurement
        date = source.date
    }

    override fun fromBase(base: Weight): PersistableWeight {
        return PersistableWeight(base)
    }
}

