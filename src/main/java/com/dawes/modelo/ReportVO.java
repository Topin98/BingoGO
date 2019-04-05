package com.dawes.modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "reports")
public class ReportVO {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idReport;
	
	//usuario que emite el report
	@ManyToOne
	@JoinColumn(name="idUsuario1")
	private UsuarioVO usuario1;
	
	//usuario que se reporta
	@ManyToOne
	@JoinColumn(name="idUsuario2")
	private UsuarioVO usuario2;
	
	private String motivo;
	
	public ReportVO() {
		super();
	}

	public ReportVO(UsuarioVO usuario1, UsuarioVO usuario2, String motivo) {
		super();
		this.usuario1 = usuario1;
		this.usuario2 = usuario2;
		this.motivo = motivo;
	}

	public ReportVO(int idReport, UsuarioVO usuario1, UsuarioVO usuario2, String motivo) {
		super();
		this.idReport = idReport;
		this.usuario1 = usuario1;
		this.usuario2 = usuario2;
		this.motivo = motivo;
	}

	public int getIdReport() {
		return idReport;
	}

	public void setIdReport(int idReport) {
		this.idReport = idReport;
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

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idReport;
		result = prime * result + ((motivo == null) ? 0 : motivo.hashCode());
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
		ReportVO other = (ReportVO) obj;
		if (idReport != other.idReport)
			return false;
		if (motivo == null) {
			if (other.motivo != null)
				return false;
		} else if (!motivo.equals(other.motivo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ReportVO [idReport=" + idReport + ", usuario1=" + usuario1.getIdUsuario() + ", usuario2=" + usuario2.getIdUsuario() + ", motivo="
				+ motivo + "]";
	}
	
}
