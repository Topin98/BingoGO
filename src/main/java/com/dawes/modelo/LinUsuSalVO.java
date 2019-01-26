package com.dawes.modelo;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class LinUsuSalVO implements Serializable {

	private int idUsuario;
	private int idSala;
	
	public LinUsuSalVO() {
		super();
	}

	public LinUsuSalVO(int idUsuario, int idSala) {
		super();
		this.idUsuario = idUsuario;
		this.idSala = idSala;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public int getIdSala() {
		return idSala;
	}

	public void setIdSala(int idSala) {
		this.idSala = idSala;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idSala;
		result = prime * result + idUsuario;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LinUsuSalVO other = (LinUsuSalVO) obj;
		if (idSala != other.idSala)
			return false;
		if (idUsuario != other.idUsuario)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LinUsuSalVO [idUsuario=" + idUsuario + ", idSala=" + idSala + "]";
	}
	
}
