package org.acme.dao

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import mu.KotlinLogging
import org.acme.model.AbstractModelType

import javax.transaction.Transactional

private val logger = KotlinLogging.logger {}

interface AbstractDAO<T : AbstractModelType<T>> {
    @Transactional
    fun getAll(): List<T>

    @Transactional
    fun getById(id: Long): T?

    @Transactional
    fun create(obj: T): Long?

    @Transactional
    fun delete(obj: T)

    @Transactional
    fun deleteById(id: Long)

    @Transactional
    fun update(obj: T)
}


abstract class AbstractDAOImpl<DAO_TYPE : AbstractDAOType<BASE_TYPE, DAO_TYPE>, BASE_TYPE : AbstractModelType<BASE_TYPE>>(val repository: PanacheRepository<DAO_TYPE>) :
    AbstractDAO<BASE_TYPE> {
    constructor() : this(EmptyRepository()) { //Required for IoC
    }

    abstract fun baseToDaoType(base: BASE_TYPE): DAO_TYPE

    @Transactional
    override fun getAll(): List<BASE_TYPE> = repository.findAll().list().map { it.asBase()!! }

    @Transactional
    override fun getById(id: Long) = repository.findById(id)!!.asBase()

    @Transactional
    override fun create(obj: BASE_TYPE): Long? {
        val persistableMeal = baseToDaoType(obj)
        repository.persistAndFlush(persistableMeal)
        return persistableMeal.getId()
    }

    @Transactional
    override fun delete(obj: BASE_TYPE) {
        val found = repository.findById(obj.id!!)
        if (found != null) repository.delete(found) else logger.error { "not found" }
    }

    @Transactional
    override fun deleteById(id: Long) {
        val found = repository.findById(id)
        if (found != null) repository.delete(found) else logger.error { "not found" }
    }

    @Transactional
    override fun update(meal: BASE_TYPE) {
        if (meal.id == null) {
            logger.error { "trying to update meal with id = null" }
            return
        }

        val found = repository.findById(meal.id)

        if (found == null) {
            logger.error { "trying to update meal that is not in db" }
            return
        }

        found.setAll(meal)

        repository.persistAndFlush(found)
    }

}

private class EmptyRepository<T : Any> : PanacheRepository<T>
