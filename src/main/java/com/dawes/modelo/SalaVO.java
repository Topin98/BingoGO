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
	
	//static evita automaticamente que esta propiedad vaya a base de datos
	public static final int CAP_MAX = 5;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idSala;
	
	@OneToMany(mappedBy = "sala")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<UsuarioVO> lUsuarios;
	
	@Column(nullable=false)
	private int idPropietario;
	
	@Column(nullable=false, length=30)
	private String nombre;
	
	@ColumnDefault("false")
	private boolean jugando;
	
	private boolean publica;
	
	private String password;

	public SalaVO() {
		super();
		
		this.lUsuarios = new ArrayList<UsuarioVO>();
	}

	public SalaVO(List<UsuarioVO> lUsuarios, int idPropietario, String nombre, boolean jugando, boolean publica,
			String password) {
		super();
		this.lUsuarios = lUsuarios;
		this.idPropietario = idPropietario;
		this.nombre = nombre;
		this.jugando = jugando;
		this.publica = publica;
		this.password = password;
	}

	public SalaVO(int idSala, List<UsuarioVO> lUsuarios, int idPropietario, String nombre, boolean jugando,
			boolean publica, String password) {
		super();
		this.idSala = idSala;
		this.lUsuarios = lUsuarios;
		this.idPropietario = idPropietario;
		this.nombre = nombre;
		this.jugando = jugando;
		this.publica = publica;
		this.password = password;
	}

	public int getIdSala() {
		return idSala;
	}

	public void setIdSala(int idSala) {
		this.idSala = idSala;
	}

	public List<UsuarioVO> getlUsuarios() {
		return lUsuarios;
	}

	public void setlUsuarios(List<UsuarioVO> lUsuarios) {
		this.lUsuarios = lUsuarios;
	}

	public int getIdPropietario() {
		return idPropietario;
	}

	public void setIdPropietario(int idPropietario) {
		this.idPropietario = idPropietario;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public boolean isJugando() {
		return jugando;
	}

	public void setJugando(boolean jugando) {
		this.jugando = jugando;
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
		result = prime * result + idPropietario;
		result = prime * result + idSala;
		result = prime * result + (jugando ? 1231 : 1237);
		result = prime * result + ((lUsuarios == null) ? 0 : lUsuarios.hashCode());
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
		if (idPropietario != other.idPropietario)
			return false;
		if (idSala != other.idSala)
			return false;
		if (jugando != other.jugando)
			return false;
		if (lUsuarios == null) {
			if (other.lUsuarios != null)
				return false;
		} else if (!lUsuarios.equals(other.lUsuarios))
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
		return "SalaVO [idSala=" + idSala + ", lUsuarios=" + lUsuarios.size() + ", idPropietario=" + idPropietario
				+ ", nombre=" + nombre + ", jugando=" + jugando + ", publica=" + publica + ", password=" + password
				+ "]";
	}
	
}
