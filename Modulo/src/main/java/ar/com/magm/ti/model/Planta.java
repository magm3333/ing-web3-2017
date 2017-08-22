package ar.com.magm.ti.model;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

@Entity
@Proxy(lazy = false)
@Access(value = AccessType.FIELD)
@Table(name = "plantas")
public class Planta implements Serializable {

	private static final long serialVersionUID = 2464927409480955012L;
	@Id
	@GeneratedValue
	private int idPlanta;

	public int getIdPlanta() {
		return idPlanta;
	}

	public void setIdPlanta(int idPlanta) {
		this.idPlanta = idPlanta;
	}

	public String getPlanta() {
		return planta;
	}

	public void setPlanta(String planta) {
		this.planta = planta;
	}

	private String planta;

	@Override
	public int hashCode() {
		return getIdPlanta();

	}

	@Override
	public String toString() {
		return String.format("Planta: %s - %s", getIdPlanta(), getPlanta());
	}
}
