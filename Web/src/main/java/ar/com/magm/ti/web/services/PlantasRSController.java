package ar.com.magm.ti.web.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ar.com.magm.ti.model.service.IPlantaService;
import ar.com.magm.ti.service.SimpleResponse;
import ar.com.magm.ti.service.exception.ServiceException;

@RestController
@RequestMapping(value = Constants.URL_PLANTAS)
public class PlantasRSController {
	private static Logger LOG = LoggerFactory.getLogger(PlantasRSController.class);
	@Autowired
	IPlantaService plantaService;
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseEntity<Object> list() {
		try {
			return new ResponseEntity<Object>(plantaService.list(), HttpStatus.OK);
		} catch (ServiceException e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<Object>(new SimpleResponse(-1, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
