package org.acme.dao

import org.acme.model.AbstractModelType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class AbstractDAOType<BASE_TYPE : AbstractModelType<BASE_TYPE>, CHILD> {

    @Id
    @GeneratedValue
    private var id: Long? = null

    open fun getId() = id

    open fun setId(id: Long?) {
        this.id = id
    }

    abstract fun asBase(): BASE_TYPE?

    abstract fun setAll(source: BASE_TYPE)

    abstract fun fromBase(base: BASE_TYPE): CHILD //TODO: is it actually needed?


}