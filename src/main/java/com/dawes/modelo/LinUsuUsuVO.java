package com.dawes.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class LinUsuUsuVO implements Serializable{
	
	private int idUsuario1;
	private int idUsuario2;
	
	public LinUsuUsuVO() {
		super();
	}

	public LinUsuUsuVO(int idUsuario1, int idUsuario2) {
		super();
		this.idUsuario1 = idUsuario1;
		this.idUsuario2 = idUsuario2;
	}

	public int getIdUsuario1() {
		return idUsuario1;
	}

	public void setIdUsuario1(int idUsuario1) {
		this.idUsuario1 = idUsuario1;
	}

	public int getIdUsuario2() {
		return idUsuario2;
	}

	public void setIdUsuario2(int idUsuario2) {
		this.idUsuario2 = idUsuario2;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idUsuario1;
		result = prime * result + idUsuario2;
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
		LinUsuUsuVO other = (LinUsuUsuVO) obj;
		if (idUsuario1 != other.idUsuario1)
			return false;
		if (idUsuario2 != other.idUsuario2)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AmigosVO [idUsuario1=" + idUsuario1 + ", idUsuario2=" + idUsuario2 + "]";
	}
	
}
