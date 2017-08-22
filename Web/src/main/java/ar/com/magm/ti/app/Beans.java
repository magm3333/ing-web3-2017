package ar.com.magm.ti.app;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import ar.com.magm.ti.model.dao.IPlantaDAO;
import ar.com.magm.ti.model.dao.hibernate.PlantaDAO;
import ar.com.magm.ti.model.service.IPlantaService;
import ar.com.magm.ti.model.service.impl.PlantaService;

@Component
public class Beans {

	//DAO
	@Bean
	@Autowired
	public IPlantaDAO plantaDAO(final SessionFactory sessionFactory) {
		return new PlantaDAO(sessionFactory);
	}
	
	//Services
	
	
	@Bean
	@Autowired
	public IPlantaService plantaService(final IPlantaDAO plantaDAO) {
		return new PlantaService(plantaDAO);
	}

	
}
