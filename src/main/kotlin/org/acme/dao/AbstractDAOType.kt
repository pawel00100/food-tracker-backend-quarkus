package org.acme.dao

import org.acme.model.AbstractModelType

abstract class AbstractDAOType<BASE_TYPE : AbstractModelType<BASE_TYPE>, CHILD> {

    abstract fun getId(): Long?

    abstract fun setId(id: Long?)

    abstract fun asBase(): BASE_TYPE?

    abstract fun setAll(source: BASE_TYPE)

    abstract fun fromBase(base: BASE_TYPE): CHILD //TODO: is it actually needed?

}