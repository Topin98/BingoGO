package com.dawes.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name="salas")
public class SalaVO {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idSala;
	
	@OneToMany(mappedBy = "sala")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<UsuarioSalaVO> lUsuSala;
	
	@Column(nullable=false, length=30)
	private String nombre;
	
	@ColumnDefault("true")
	private boolean activa;
	
	private boolean publica;
	
	private String password;

	public SalaVO() {
		super();
		
		this.lUsuSala = new ArrayList<UsuarioSalaVO>();
	}

	public SalaVO(List<UsuarioSalaVO> lUsuSala, String nombre, boolean activa, boolean publica, String password) {
		super();
		this.lUsuSala = lUsuSala;
		this.nombre = nombre;
		this.activa = activa;
		this.publica = publica;
		this.password = password;
	}

	public SalaVO(int idSala, List<UsuarioSalaVO> lUsuSala, String nombre, boolean activa, boolean publica,
			String password) {
		super();
		this.idSala = idSala;
		this.lUsuSala = lUsuSala;
		this.nombre = nombre;
		this.activa = activa;
		this.publica = publica;
		this.password = password;
	}

	public int getIdSala() {
		return idSala;
	}

	public void setIdSala(int idSala) {
		this.idSala = idSala;
	}

	public List<UsuarioSalaVO> getlUsuSala() {
		return lUsuSala;
	}

	public void setlUsuSala(List<UsuarioSalaVO> lUsuSala) {
		this.lUsuSala = lUsuSala;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public boolean isActiva() {
		return activa;
	}

	public void setActiva(boolean activa) {
		this.activa = activa;
	}

	public boolean isPublica() {
		return publica;
	}

	public void setPublica(boolean publica) {
		this.publica = publica;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (activa ? 1231 : 1237);
		result = prime * result + idSala;
		result = prime * result + ((lUsuSala == null) ? 0 : lUsuSala.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + (publica ? 1231 : 1237);
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
		SalaVO other = (SalaVO) obj;
		if (activa != other.activa)
			return false;
		if (idSala != other.idSala)
			return false;
		if (lUsuSala == null) {
			if (other.lUsuSala != null)
				return false;
		} else if (!lUsuSala.equals(other.lUsuSala))
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (publica != other.publica)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SalaVO [idSala=" + idSala + ", lUsuSala=" + lUsuSala + ", nombre=" + nombre + ", activa=" + activa
				+ ", publica=" + publica + ", password=" + password + "]";
	}
	
}
