package ar.com.magm.ti.model.service.impl;

import ar.com.magm.ti.model.AuthToken;
import ar.com.magm.ti.model.dao.IAuthTokenDAO;
import ar.com.magm.ti.model.service.IAuthTokenService;
import ar.com.magm.ti.service.impl.GenericService;

public class AuthTokenService extends GenericService<AuthToken, String> implements IAuthTokenService {

	public AuthTokenService(IAuthTokenDAO dao) {
		super(dao);
	}

	
}
