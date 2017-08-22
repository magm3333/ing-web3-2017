package ar.com.magm.ti.model.service.impl;

import ar.com.magm.ti.model.Planta;
import ar.com.magm.ti.model.dao.IPlantaDAO;
import ar.com.magm.ti.model.service.IPlantaService;
import ar.com.magm.ti.service.impl.GenericService;

public class PlantaService extends GenericService<Planta, Integer>
		implements IPlantaService {

	public PlantaService(IPlantaDAO dao) {
		super(dao);
	}
	
}
