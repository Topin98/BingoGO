package com.dawes.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name="roles")
public class RolVO {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idRol;
	
	@Column(nullable=false)
	private String nombre;
	
	@OneToMany(mappedBy = "rol", cascade= CascadeType.ALL, orphanRemoval = true)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<UsuarioRolVO> lUsuarioRol;

	public RolVO() {
		super();
		
		this.lUsuarioRol = new ArrayList<UsuarioRolVO>();
	}

	public RolVO(String nombre, List<UsuarioRolVO> lUsuarioRol) {
		super();
		this.nombre = nombre;
		this.lUsuarioRol = lUsuarioRol;
	}

	public RolVO(int idRol, String nombre, List<UsuarioRolVO> lUsuarioRol) {
		super();
		this.idRol = idRol;
		this.nombre = nombre;
		this.lUsuarioRol = lUsuarioRol;
	}

	public int getIdRol() {
		return idRol;
	}

	public void setIdRol(int idRol) {
		this.idRol = idRol;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<UsuarioRolVO> getlUsuarioRol() {
		return lUsuarioRol;
	}

	public void setlUsuarioRol(List<UsuarioRolVO> lUsuarioRol) {
		this.lUsuarioRol = lUsuarioRol;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idRol;
		result = prime * result + ((lUsuarioRol == null) ? 0 : lUsuarioRol.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
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
		RolVO other = (RolVO) obj;
		if (idRol != other.idRol)
			return false;
		if (lUsuarioRol == null) {
			if (other.lUsuarioRol != null)
				return false;
		} else if (!lUsuarioRol.equals(other.lUsuarioRol))
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RolVO [idRol=" + idRol + ", nombre=" + nombre + ", lUsuarioRol=" + lUsuarioRol + "]";
	}
	
}
