package ar.com.magm.ti.model.service.impl;

import ar.com.magm.ti.model.Archivo;
import ar.com.magm.ti.model.dao.IArchivoDAO;
import ar.com.magm.ti.model.service.IArchivoService;
import ar.com.magm.ti.service.impl.GenericService;

public class ArchivoService extends GenericService<Archivo, Integer>
		implements IArchivoService {

	public ArchivoService(IArchivoDAO dao) {
		super(dao);
	}
	
}
