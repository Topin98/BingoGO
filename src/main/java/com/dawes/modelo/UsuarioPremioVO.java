package com.dawes.modelo;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name="linUsuPre")
public class UsuarioPremioVO {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idUsuPre;
	
	@ManyToOne
	@JoinColumn(name="idUsuario", nullable=false)
	private UsuarioVO usuario;
	
	@ManyToOne
	@JoinColumn(name="idPremio", nullable=false)
	private PremioVO premio;
	
	@Column(nullable=false)
	private LocalDate fecha;
	
	@ColumnDefault("false")
	private boolean enviado;

	public UsuarioPremioVO() {
		super();
	}

	public UsuarioPremioVO(UsuarioVO usuario, PremioVO premio, LocalDate fecha, boolean enviado) {
		super();
		this.usuario = usuario;
		this.premio = premio;
		this.fecha = fecha;
		this.enviado = enviado;
	}

	public UsuarioPremioVO(int idUsuPre, UsuarioVO usuario, PremioVO premio, LocalDate fecha, boolean enviado) {
		super();
		this.idUsuPre = idUsuPre;
		this.usuario = usuario;
		this.premio = premio;
		this.fecha = fecha;
		this.enviado = enviado;
	}
	
	public int getIdUsuPre() {
		return idUsuPre;
	}

	public void setIdUsuPre(int idUsuPre) {
		this.idUsuPre = idUsuPre;
	}

	public UsuarioVO getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioVO usuario) {
		this.usuario = usuario;
	}

	public PremioVO getPremio() {
		return premio;
	}

	public void setPremio(PremioVO premio) {
		this.premio = premio;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public boolean isEnviado() {
		return enviado;
	}

	public void setEnviado(boolean enviado) {
		this.enviado = enviado;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (enviado ? 1231 : 1237);
		result = prime * result + ((fecha == null) ? 0 : fecha.hashCode());
		result = prime * result + idUsuPre;
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
		UsuarioPremioVO other = (UsuarioPremioVO) obj;
		if (enviado != other.enviado)
			return false;
		if (fecha == null) {
			if (other.fecha != null)
				return false;
		} else if (!fecha.equals(other.fecha))
			return false;
		if (idUsuPre != other.idUsuPre)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UsuarioPremioVO [idUsuPre=" + idUsuPre + ", usuario=" + usuario.getIdUsuario() + ", premio=" + premio.getIdPremio() + ", fecha="
				+ fecha + ", enviado=" + enviado + "]";
	}
}
