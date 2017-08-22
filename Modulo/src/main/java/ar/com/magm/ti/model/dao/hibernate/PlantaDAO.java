package ar.com.magm.ti.model.dao.hibernate;

import org.hibernate.SessionFactory;

import ar.com.magm.ti.model.Planta;
import ar.com.magm.ti.model.dao.IPlantaDAO;
import ar.com.magm.ti.persistence.dao.hibernate.GenericDAO;

public class PlantaDAO extends GenericDAO<Planta, Integer> implements IPlantaDAO {

	public PlantaDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}
	

}
