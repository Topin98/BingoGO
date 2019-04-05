package com.dawes.modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.Table;

@Entity
@Table(name = "mensajes")
public class MensajeVO {

	public enum Tipo {
		MENSAJE, PETICION, INVITACION
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idMensaje;
	
	@ManyToOne
	@PrimaryKeyJoinColumns(value = {
	    @PrimaryKeyJoinColumn(name = "usuario1_idUsuario"),
	    @PrimaryKeyJoinColumn(name = "usuario2_idUsuario")
	})
	private UsuarioUsuarioVO usuUsu;
	
	private String sender;
	
	private String contenido;
	
	private Tipo tipo; 

	public MensajeVO() {
		super();
	}

	public MensajeVO(UsuarioUsuarioVO usuUsu, String sender, String contenido, Tipo tipo) {
		super();
		this.usuUsu = usuUsu;
		this.sender = sender;
		this.contenido = contenido;
		this.tipo = tipo;
	}

	public MensajeVO(int idMensaje, UsuarioUsuarioVO usuUsu, String sender, String contenido, Tipo tipo) {
		super();
		this.idMensaje = idMensaje;
		this.usuUsu = usuUsu;
		this.sender = sender;
		this.contenido = contenido;
		this.tipo = tipo;
	}
	
	public int getIdMensaje() {
		return idMensaje;
	}

	public void setIdMensaje(int idMensaje) {
		this.idMensaje = idMensaje;
	}

	public UsuarioUsuarioVO getUsuUsu() {
		return usuUsu;
	}

	public void setUsuUsu(UsuarioUsuarioVO usuUsu) {
		this.usuUsu = usuUsu;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contenido == null) ? 0 : contenido.hashCode());
		result = prime * result + idMensaje;
		result = prime * result + ((sender == null) ? 0 : sender.hashCode());
		result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
		result = prime * result + ((usuUsu == null) ? 0 : usuUsu.hashCode());
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
		MensajeVO other = (MensajeVO) obj;
		if (contenido == null) {
			if (other.contenido != null)
				return false;
		} else if (!contenido.equals(other.contenido))
			return false;
		if (idMensaje != other.idMensaje)
			return false;
		if (sender == null) {
			if (other.sender != null)
				return false;
		} else if (!sender.equals(other.sender))
			return false;
		if (tipo != other.tipo)
			return false;
		if (usuUsu == null) {
			if (other.usuUsu != null)
				return false;
		} else if (!usuUsu.equals(other.usuUsu))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MensajeVO [idMensaje=" + idMensaje + ", usuUsu=" + usuUsu + ", sender=" + sender + ", contenido="
				+ contenido + ", tipo=" + tipo + "]";
	}
	
	
}
