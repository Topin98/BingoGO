package com.dawes.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name="linUsuUsu")
public class UsuarioUsuarioVO {
	
	//numero de mensajes por relacion entre usuarios que se van guardar en base de datos
	public static final int CAP_HISTORIAL_MAX = 20;
	
	@EmbeddedId
	private LinUsuUsuVO linUsuUsu;
	
	@ManyToOne
	@MapsId("idUsuario1")
	private UsuarioVO usuario1;
	
	@ManyToOne
	@MapsId("idUsuario2")
	private UsuarioVO usuario2;
	
	@ColumnDefault("false")
	private boolean activo;
	
	@OneToMany(mappedBy = "usuUsu", cascade = CascadeType.ALL, orphanRemoval = true)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<MensajeVO> lMensajes;

	public UsuarioUsuarioVO() {
		super();
		
		this.lMensajes = new ArrayList<MensajeVO>();
	}

	public UsuarioUsuarioVO(LinUsuUsuVO linUsuUsu, UsuarioVO usuario1, UsuarioVO usuario2, boolean activo,
			List<MensajeVO> lMensajes) {
		super();
		this.linUsuUsu = linUsuUsu;
		this.usuario1 = usuario1;
		this.usuario2 = usuario2;
		this.activo = activo;
		this.lMensajes = lMensajes;
	}

	public LinUsuUsuVO getLinUsuUsu() {
		return linUsuUsu;
	}

	public void setLinUsuUsu(LinUsuUsuVO linUsuUsu) {
		this.linUsuUsu = linUsuUsu;
	}

	public UsuarioVO getUsuario1() {
		return usuario1;
	}

	public void setUsuario1(UsuarioVO usuario1) {
		this.usuario1 = usuario1;
	}

	public UsuarioVO getUsuario2() {
		return usuario2;
	}

	public void setUsuario2(UsuarioVO usuario2) {
		this.usuario2 = usuario2;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public List<MensajeVO> getlMensajes() {
		return lMensajes;
	}

	public void setlMensajes(List<MensajeVO> lMensajes) {
		this.lMensajes = lMensajes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (activo ? 1231 : 1237);
		result = prime * result + ((lMensajes == null) ? 0 : lMensajes.hashCode());
		result = prime * result + ((linUsuUsu == null) ? 0 : linUsuUsu.hashCode());
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
		UsuarioUsuarioVO other = (UsuarioUsuarioVO) obj;
		if (activo != other.activo)
			return false;
		if (lMensajes == null) {
			if (other.lMensajes != null)
				return false;
		} else if (!lMensajes.equals(other.lMensajes))
			return false;
		if (linUsuUsu == null) {
			if (other.linUsuUsu != null)
				return false;
		} else if (!linUsuUsu.equals(other.linUsuUsu))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UsuarioUsuarioVO [linUsuUsu=" + linUsuUsu + ", usuario1=" + usuario1.getIdUsuario() + ", usuario2=" + usuario2.getIdUsuario()
				+ ", activo=" + activo + ", lMensajes=" + lMensajes.size() + "]";
	}

	
	
	
}
