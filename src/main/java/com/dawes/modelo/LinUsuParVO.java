package com.dawes.modelo;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class LinUsuParVO implements Serializable{

	private int idUsuario;
	private int idPartida;
	
	public LinUsuParVO() {
		super();
	}

	public LinUsuParVO(int idUsuario, int idPartida) {
		super();
		this.idUsuario = idUsuario;
		this.idPartida = idPartida;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public int getIdPartida() {
		return idPartida;
	}

	public void setIdPartida(int idPartida) {
		this.idPartida = idPartida;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idPartida;
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
		LinUsuParVO other = (LinUsuParVO) obj;
		if (idPartida != other.idPartida)
			return false;
		if (idUsuario != other.idUsuario)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LinUsuParVO [idUsuario=" + idUsuario + ", idPartida=" + idPartida + "]";
	}
	
	
}
