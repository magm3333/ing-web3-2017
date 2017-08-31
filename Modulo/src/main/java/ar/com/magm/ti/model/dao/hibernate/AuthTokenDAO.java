package ar.com.magm.ti.model.dao.hibernate;

import org.hibernate.SessionFactory;

import ar.com.magm.ti.model.AuthToken;
import ar.com.magm.ti.model.dao.IAuthTokenDAO;
import ar.com.magm.ti.persistence.dao.hibernate.GenericDAO;

public class AuthTokenDAO extends GenericDAO<AuthToken, String> implements IAuthTokenDAO {

	public AuthTokenDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

}
