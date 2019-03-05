package com.dawes.modelo;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class LinUsuRolVO implements Serializable {

	private int idUsuario;
	private int idRol;
	
	public LinUsuRolVO() {
		super();
	}

	public LinUsuRolVO(int idUsuario, int idRol) {
		super();
		this.idUsuario = idUsuario;
		this.idRol = idRol;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public int getIdRol() {
		return idRol;
	}

	public void setIdRol(int idRol) {
		this.idRol = idRol;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idRol;
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
		LinUsuRolVO other = (LinUsuRolVO) obj;
		if (idRol != other.idRol)
			return false;
		if (idUsuario != other.idUsuario)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LinUsuRolVO [idUsuario=" + idUsuario + ", idRol=" + idRol + "]";
	}
	
}
