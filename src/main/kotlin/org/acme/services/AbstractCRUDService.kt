package org.acme.services

import org.acme.dao.AbstractDAO
import org.acme.model.AbstractModelType

abstract class AbstractCRUDService<T : AbstractModelType<T>>(val dao: AbstractDAO<T>) {

    constructor() : this(EmptyDAO<T>())

    fun create(meal: T) = dao.create(meal)

    fun getById(id: Long) = dao.getById(id)

    fun getAll() = dao.getAll()

    fun delete(obj: T) = dao.delete(obj)

    fun update(obj: T) = dao.update(obj)

    fun deleteById(id: Long) = dao.deleteById(id)
}

class EmptyDAO<T : AbstractModelType<T>> : AbstractDAO<T> {
    override fun getAll(): List<T> = listOf()

    override fun getById(id: Long): T? = null

    override fun create(obj: T): Long? = null

    override fun delete(obj: T) = Unit

    override fun deleteById(id: Long) = Unit

    override fun update(obj: T) = Unit
}
