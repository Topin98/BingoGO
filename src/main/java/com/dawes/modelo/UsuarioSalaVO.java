package com.dawes.modelo;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Table(name="linUsuSal")
public class UsuarioSalaVO {

	@EmbeddedId
	private LinUsuSalVO linUsuSal;
	
	@ManyToOne
	@MapsId("idUsuario")
	private UsuarioVO usuario;
	
	@ManyToOne
	@MapsId("idSala")
	private SalaVO sala;

	public UsuarioSalaVO() {
		super();
	}

	public UsuarioSalaVO(LinUsuSalVO linUsuSal, UsuarioVO usuario, SalaVO sala) {
		super();
		this.linUsuSal = linUsuSal;
		this.usuario = usuario;
		this.sala = sala;
	}

	public LinUsuSalVO getLinUsuSal() {
		return linUsuSal;
	}

	public void setLinUsuSal(LinUsuSalVO linUsuSal) {
		this.linUsuSal = linUsuSal;
	}

	public UsuarioVO getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioVO usuario) {
		this.usuario = usuario;
	}

	public SalaVO getSala() {
		return sala;
	}

	public void setSala(SalaVO sala) {
		this.sala = sala;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((linUsuSal == null) ? 0 : linUsuSal.hashCode());
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
		UsuarioSalaVO other = (UsuarioSalaVO) obj;
		if (linUsuSal == null) {
			if (other.linUsuSal != null)
				return false;
		} else if (!linUsuSal.equals(other.linUsuSal))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UsuarioSalaVO [linUsuSal=" + linUsuSal + ", usuario=" + usuario.getIdUsuario() + ", sala=" + sala.getIdSala() + "]";
	}
}
