package org.acme.model

import java.time.Instant

class Weight(id: Long?, val measurement: Float, val date: Instant) : AbstractModelType<Weight>(id) {
    override fun withId(id: Long?) = Weight(id, measurement, date)
}
