package ar.com.magm.ti.web.services;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ar.com.magm.ti.exception.NotFoundException;
import ar.com.magm.ti.model.Archivo;
import ar.com.magm.ti.model.service.IArchivoService;
import ar.com.magm.ti.service.exception.ServiceException;

/**
 * 
 * @author magm
 *
 */
@RestController
@RequestMapping(value = Constants.URL_ARCHIVOS)
public class ArchivoRSController extends BaseRSController {
	private static Logger LOG = LoggerFactory.getLogger(ArchivoRSController.class);
	@Autowired
	private IArchivoService archivosService;

	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public ResponseEntity<Object> list(HttpServletRequest request) {

		try {
			if (request.isUserInRole("ADMIN")) {
				return new ResponseEntity<Object>(archivosService.list(), HttpStatus.CREATED);
			} else {
				return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
			}
		} catch (ServiceException e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = { "", "/" }, method = RequestMethod.POST)
	public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
		try {
			Archivo a = new Archivo();
			a.setNombre(file.getOriginalFilename());
			a.setContenido(file.getBytes());
			a.setSize(a.getContenido().length);
			a.setMimeType(file.getContentType());
			a = archivosService.save(a);
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.set("location", Constants.URL_ARCHIVOS + "/" + a.getId());
			return new ResponseEntity<String>(responseHeaders, HttpStatus.CREATED);

		} catch (ServiceException e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> download(@PathVariable("id") int id) {
		Archivo r = new Archivo();
		try {
			r = archivosService.load(id);
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.setContentType(MediaType.parseMediaType(r.getMimeType()));
			String filename = r.getNombre();
			responseHeaders.setContentDispositionFormData(filename, filename);

			return new ResponseEntity<byte[]>(r.getContenido(), responseHeaders, HttpStatus.OK);
		} catch (ServiceException e) {
			return new ResponseEntity<byte[]>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NotFoundException e) {
			return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);

		}

	}

	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> delete(@PathVariable("id") int id) {
		Archivo a = new Archivo();
		a.setId(id);
		try {
			archivosService.delete(a);
			return new ResponseEntity<Object>(HttpStatus.OK);
		} catch (ServiceException e) {
			return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NotFoundException e) {
			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
		}

	}

}
