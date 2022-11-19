package org.acme.services

import org.acme.dao.WeightDAOImpl
import org.acme.model.Weight
import java.time.Instant
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class WeightService(val weightDAO: WeightDAOImpl) : AbstractCRUDService<Weight>(weightDAO) {
    fun getFiltered(from: Instant, to: Instant) = weightDAO.getFiltered(from, to)
}

