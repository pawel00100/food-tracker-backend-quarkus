package org.acme.services

import org.acme.dao.DishDAO
import org.acme.model.Dish
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class DishService(val dishDAO: DishDAO) : AbstractCRUDService<Dish>(dishDAO) {
}

