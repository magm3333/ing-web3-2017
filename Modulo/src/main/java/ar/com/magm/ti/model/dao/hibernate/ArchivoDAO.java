package ar.com.magm.ti.model.dao.hibernate;

import org.hibernate.SessionFactory;

import ar.com.magm.ti.model.Archivo;
import ar.com.magm.ti.model.dao.IArchivoDAO;
import ar.com.magm.ti.persistence.dao.hibernate.GenericDAO;

public class ArchivoDAO extends GenericDAO<Archivo, Integer> implements IArchivoDAO {

	public ArchivoDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}
	

}
