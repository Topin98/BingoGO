package com.dawes.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
@Table(name="partidas")
public class PartidaVO {
	
	public static final int[] PREMIO_FICHAS_BINGO = {100, 50, 25, 10, 5};
	public static final int[] PREMIO_FICHAS_LINEA = {25, 15, 10, 5, 1};
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idPartida;
	
	@OneToMany(mappedBy = "partida")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<UsuarioPartidaVO> lUsuPar;
	
	@Column(nullable=false)
	private LocalDate fecha;

	public PartidaVO() {
		super();
		
		this.lUsuPar = new ArrayList<UsuarioPartidaVO>();
	}
	
	public PartidaVO(List<UsuarioPartidaVO> lUsuPar, LocalDate fecha) {
		super();
		this.lUsuPar = lUsuPar;
		this.fecha = fecha;
	}
	
	public PartidaVO(int idPartida, List<UsuarioPartidaVO> lUsuPar, LocalDate fecha) {
		super();
		this.idPartida = idPartida;
		this.lUsuPar = lUsuPar;
		this.fecha = fecha;
	}

	public int getIdPartida() {
		return idPartida;
	}

	public void setIdPartida(int idPartida) {
		this.idPartida = idPartida;
	}

	public List<UsuarioPartidaVO> getlUsuPar() {
		return lUsuPar;
	}

	public void setlUsuPar(List<UsuarioPartidaVO> lUsuPar) {
		this.lUsuPar = lUsuPar;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fecha == null) ? 0 : fecha.hashCode());
		result = prime * result + idPartida;
		result = prime * result + ((lUsuPar == null) ? 0 : lUsuPar.hashCode());
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
		PartidaVO other = (PartidaVO) obj;
		if (fecha == null) {
			if (other.fecha != null)
				return false;
		} else if (!fecha.equals(other.fecha))
			return false;
		if (idPartida != other.idPartida)
			return false;
		if (lUsuPar == null) {
			if (other.lUsuPar != null)
				return false;
		} else if (!lUsuPar.equals(other.lUsuPar))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PartidaVO [idPartida=" + idPartida + ", lUsuPar=" + lUsuPar + ", fecha=" + fecha + "]";
	}
	
	
}
