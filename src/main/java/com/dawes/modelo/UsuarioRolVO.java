package com.dawes.modelo;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Table(name="linUsuRol")
public class UsuarioRolVO {
	
	@EmbeddedId
	private LinUsuRolVO linUsuRol;
	
	@ManyToOne
	@MapsId("idUsuario")
	private UsuarioVO usuario;
	
	@ManyToOne
	@MapsId("idRol")
	private RolVO rol;

	public UsuarioRolVO() {
		super();
	}

	public UsuarioRolVO(LinUsuRolVO linUsuRol, UsuarioVO usuario, RolVO rol) {
		super();
		this.linUsuRol = linUsuRol;
		this.usuario = usuario;
		this.rol = rol;
	}

	public LinUsuRolVO getLinUsuRol() {
		return linUsuRol;
	}

	public void setLinUsuRol(LinUsuRolVO linUsuRol) {
		this.linUsuRol = linUsuRol;
	}

	public UsuarioVO getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioVO usuario) {
		this.usuario = usuario;
	}

	public RolVO getRol() {
		return rol;
	}

	public void setRol(RolVO rol) {
		this.rol = rol;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((linUsuRol == null) ? 0 : linUsuRol.hashCode());
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
		UsuarioRolVO other = (UsuarioRolVO) obj;
		if (linUsuRol == null) {
			if (other.linUsuRol != null)
				return false;
		} else if (!linUsuRol.equals(other.linUsuRol))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UsuarioRolVO [linUsuRol=" + linUsuRol + ", usuario=" + usuario.getIdUsuario() + ", rol=" + rol.getIdRol() + "]";
	}

	
}
