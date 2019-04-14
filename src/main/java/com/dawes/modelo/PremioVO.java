package com.dawes.modelo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name="premios")
public class PremioVO {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idPremio;
	
	@OneToMany(mappedBy = "premio")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<UsuarioPremioVO> lUsuPre;
	
	@Column(nullable=false, unique=true, length=30)
	private String nombre;
	
	@Column(nullable=false)
	private String descripcion;
	
	private int precio;
	
	@Lob
	private String imagen;

	public PremioVO() {
		super();
		
		this.lUsuPre = new ArrayList<UsuarioPremioVO>();
	}

	public PremioVO(List<UsuarioPremioVO> lUsuPre, String nombre, String descripcion, int precio, String imagen) {
		super();
		this.lUsuPre = lUsuPre;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.precio = precio;
		this.imagen = imagen;
	}

	public PremioVO(int idPremio, List<UsuarioPremioVO> lUsuPre, String nombre, String descripcion, int precio,
			String imagen) {
		super();
		this.idPremio = idPremio;
		this.lUsuPre = lUsuPre;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.precio = precio;
		this.imagen = imagen;
	}

	public int getIdPremio() {
		return idPremio;
	}

	public void setIdPremio(int idPremio) {
		this.idPremio = idPremio;
	}

	public List<UsuarioPremioVO> getlUsuPre() {
		return lUsuPre;
	}

	public void setlUsuPre(List<UsuarioPremioVO> lUsuPre) {
		this.lUsuPre = lUsuPre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getPrecio() {
		return precio;
	}

	public void setPrecio(int precio) {
		this.precio = precio;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((descripcion == null) ? 0 : descripcion.hashCode());
		result = prime * result + idPremio;
		result = prime * result + ((imagen == null) ? 0 : imagen.hashCode());
		result = prime * result + ((lUsuPre == null) ? 0 : lUsuPre.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result + precio;
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
		PremioVO other = (PremioVO) obj;
		if (descripcion == null) {
			if (other.descripcion != null)
				return false;
		} else if (!descripcion.equals(other.descripcion))
			return false;
		if (idPremio != other.idPremio)
			return false;
		if (imagen == null) {
			if (other.imagen != null)
				return false;
		} else if (!imagen.equals(other.imagen))
			return false;
		if (lUsuPre == null) {
			if (other.lUsuPre != null)
				return false;
		} else if (!lUsuPre.equals(other.lUsuPre))
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		if (precio != other.precio)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PremioVO [idPremio=" + idPremio + ", lUsuPre=" + lUsuPre + ", nombre=" + nombre + ", descripcion="
				+ descripcion + ", precio=" + precio + ", imagen=" + imagen + "]";
	}
	
}
