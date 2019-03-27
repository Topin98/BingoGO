package com.dawes.modelo;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Table(name="linUsuPar")
public class UsuarioPartidaVO {
	
	@EmbeddedId
	private LinUsuParVO linUsuPar;
	
	@ManyToOne
	@MapsId("idUsuario")
	private UsuarioVO usuario;
	
	@ManyToOne
	@MapsId("idPartida")
	private PartidaVO partida;
	
	private int posicionLinea;
	
	private int posicionBingo;
	
	public UsuarioPartidaVO() {
		super();
	}

	public UsuarioPartidaVO(LinUsuParVO linUsuPar, UsuarioVO usuario, PartidaVO partida, int posicionLinea,
			int posicionBingo) {
		super();
		this.linUsuPar = linUsuPar;
		this.usuario = usuario;
		this.partida = partida;
		this.posicionLinea = posicionLinea;
		this.posicionBingo = posicionBingo;
	}

	public LinUsuParVO getLinUsuPar() {
		return linUsuPar;
	}

	public void setLinUsuPar(LinUsuParVO linUsuPar) {
		this.linUsuPar = linUsuPar;
	}

	public UsuarioVO getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioVO usuario) {
		this.usuario = usuario;
	}

	public PartidaVO getPartida() {
		return partida;
	}

	public void setPartida(PartidaVO partida) {
		this.partida = partida;
	}

	public int getPosicionLinea() {
		return posicionLinea;
	}

	public void setPosicionLinea(int posicionLinea) {
		this.posicionLinea = posicionLinea;
	}

	public int getPosicionBingo() {
		return posicionBingo;
	}

	public void setPosicionBingo(int posicionBingo) {
		this.posicionBingo = posicionBingo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((linUsuPar == null) ? 0 : linUsuPar.hashCode());
		result = prime * result + posicionBingo;
		result = prime * result + posicionLinea;
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
		UsuarioPartidaVO other = (UsuarioPartidaVO) obj;
		if (linUsuPar == null) {
			if (other.linUsuPar != null)
				return false;
		} else if (!linUsuPar.equals(other.linUsuPar))
			return false;
		if (posicionBingo != other.posicionBingo)
			return false;
		if (posicionLinea != other.posicionLinea)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UsuarioPartidaVO [linUsuPar=" + linUsuPar + ", usuario=" + usuario.getIdUsuario() + ", partida=" + partida.getIdPartida()
				+ ", posicionLinea=" + posicionLinea + ", posicionBingo=" + posicionBingo + "]";
	}

	
	
}
