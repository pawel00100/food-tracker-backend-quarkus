package org.acme.model

abstract class AbstractModelType<CHILD : AbstractModelType<CHILD>>(val id: Long?) {
    abstract fun withId(id: Long?): CHILD

}