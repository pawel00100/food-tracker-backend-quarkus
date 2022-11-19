package org.acme.services

import org.acme.dao.MealDAO
import org.acme.model.Meal
import java.time.Instant
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class MealService(val mealDAO: MealDAO) : AbstractCRUDService<Meal>(mealDAO) {
    fun getFiltered(from: Instant, to: Instant) = mealDAO.getFiltered(from, to)
}

