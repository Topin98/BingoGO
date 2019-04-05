package com.dawes.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="usuarios")
public class UsuarioVO {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idUsuario;
	
	//para borrar un rol de la lista hay que actualiarla desde la lista de RolVO
	@OneToMany(mappedBy = "usuario", orphanRemoval = true)
	@LazyCollection(LazyCollectionOption.FALSE)
	@JsonIgnore
	private List<UsuarioRolVO> lUsuarioRol;
	
	@OneToMany(mappedBy = "usuario1", cascade=CascadeType.ALL, orphanRemoval = true)
	@LazyCollection(LazyCollectionOption.FALSE)
	@JsonIgnore
	private List<UsuarioUsuarioVO> lUsuUsuRequest;
	
	@OneToMany(mappedBy = "usuario2", cascade=CascadeType.ALL, orphanRemoval = true)
	@LazyCollection(LazyCollectionOption.FALSE)
	@JsonIgnore
	private List<UsuarioUsuarioVO> lUsuUsuReceived;
	
	@OneToMany(mappedBy = "usuario", cascade=CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	@JsonIgnore
	private List<UsuarioPartidaVO> lUsuPar;
	
	@OneToMany(mappedBy = "usuario", cascade=CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	@JsonIgnore
	private List<UsuarioPremioVO> lUsuPre;
	
	@OneToMany(mappedBy = "usuario1", cascade=CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	@JsonIgnore
	private List<ReportVO> lReportsEmitidos;
	
	@OneToMany(mappedBy = "usuario2", cascade=CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	@JsonIgnore
	private List<ReportVO> lReportsRecibidos;
	
	@ManyToOne
	@JoinColumn(name="idSala")
	@JsonIgnore
	private SalaVO sala;
	
	@Column(nullable=false, unique=true, length=30)
	private String nombre;
	
	@Column(nullable=false, unique=true)
	@JsonIgnore
	private String correo;
	
	@Column(nullable=false)
	@JsonIgnore
	private String password;

	@Column(nullable=false)
	private LocalDate fechaRegistro;
	
	@ColumnDefault("'0'")
	private int puntuacionTotal;
	
	@ColumnDefault("'0'")
	private int fichas;
	
	@ColumnDefault("true")
	@JsonIgnore
	private boolean enabled;
	
	@Lob
	private String imagenPerfil;
	
	@Transient
	CartonVO carton;

	public UsuarioVO() {
		super();
		
		this.lUsuarioRol = new ArrayList<UsuarioRolVO>();
		this.lUsuUsuRequest = new ArrayList<UsuarioUsuarioVO>();
		this.lUsuUsuReceived = new ArrayList<UsuarioUsuarioVO>();
		this.lUsuPar = new ArrayList<UsuarioPartidaVO>();
		this.lUsuPre = new ArrayList<UsuarioPremioVO>();
		this.lReportsEmitidos = new ArrayList<ReportVO>();
		this.lReportsRecibidos = new ArrayList<ReportVO>();
	}
	
	public UsuarioVO(List<UsuarioRolVO> lUsuarioRol, List<UsuarioUsuarioVO> lUsuUsuRequest,
			List<UsuarioUsuarioVO> lUsuUsuReceived, List<UsuarioPartidaVO> lUsuPar, List<UsuarioPremioVO> lUsuPre,
			List<ReportVO> lReportsEmitidos, List<ReportVO> lReportsRecibidos, SalaVO sala, String nombre,
			String correo, String password, LocalDate fechaRegistro, int puntuacionTotal, int fichas, boolean enabled,
			String imagenPerfil, CartonVO carton) {
		super();
		this.lUsuarioRol = lUsuarioRol;
		this.lUsuUsuRequest = lUsuUsuRequest;
		this.lUsuUsuReceived = lUsuUsuReceived;
		this.lUsuPar = lUsuPar;
		this.lUsuPre = lUsuPre;
		this.lReportsEmitidos = lReportsEmitidos;
		this.lReportsRecibidos = lReportsRecibidos;
		this.sala = sala;
		this.nombre = nombre;
		this.correo = correo;
		this.password = password;
		this.fechaRegistro = fechaRegistro;
		this.puntuacionTotal = puntuacionTotal;
		this.fichas = fichas;
		this.enabled = enabled;
		this.imagenPerfil = imagenPerfil;
		this.carton = carton;
	}
	
	public UsuarioVO(int idUsuario, List<UsuarioRolVO> lUsuarioRol, List<UsuarioUsuarioVO> lUsuUsuRequest,
			List<UsuarioUsuarioVO> lUsuUsuReceived, List<UsuarioPartidaVO> lUsuPar, List<UsuarioPremioVO> lUsuPre,
			List<ReportVO> lReportsEmitidos, List<ReportVO> lReportsRecibidos, SalaVO sala, String nombre,
			String correo, String password, LocalDate fechaRegistro, int puntuacionTotal, int fichas, boolean enabled,
			String imagenPerfil, CartonVO carton) {
		super();
		this.idUsuario = idUsuario;
		this.lUsuarioRol = lUsuarioRol;
		this.lUsuUsuRequest = lUsuUsuRequest;
		this.lUsuUsuReceived = lUsuUsuReceived;
		this.lUsuPar = lUsuPar;
		this.lUsuPre = lUsuPre;
		this.lReportsEmitidos = lReportsEmitidos;
		this.lReportsRecibidos = lReportsRecibidos;
		this.sala = sala;
		this.nombre = nombre;
		this.correo = correo;
		this.password = password;
		this.fechaRegistro = fechaRegistro;
		this.puntuacionTotal = puntuacionTotal;
		this.fichas = fichas;
		this.enabled = enabled;
		this.imagenPerfil = imagenPerfil;
		this.carton = carton;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public List<UsuarioRolVO> getlUsuarioRol() {
		return lUsuarioRol;
	}

	public void setlUsuarioRol(List<UsuarioRolVO> lUsuarioRol) {
		this.lUsuarioRol = lUsuarioRol;
	}

	public List<UsuarioUsuarioVO> getlUsuUsuRequest() {
		return lUsuUsuRequest;
	}

	public void setlUsuUsuRequest(List<UsuarioUsuarioVO> lUsuUsuRequest) {
		this.lUsuUsuRequest = lUsuUsuRequest;
	}

	public List<UsuarioUsuarioVO> getlUsuUsuReceived() {
		return lUsuUsuReceived;
	}

	public void setlUsuUsuReceived(List<UsuarioUsuarioVO> lUsuUsuReceived) {
		this.lUsuUsuReceived = lUsuUsuReceived;
	}

	public List<UsuarioPartidaVO> getlUsuPar() {
		return lUsuPar;
	}

	public void setlUsuPar(List<UsuarioPartidaVO> lUsuPar) {
		this.lUsuPar = lUsuPar;
	}

	public SalaVO getSala() {
		return sala;
	}

	public void setSala(SalaVO sala) {
		this.sala = sala;
	}

	public List<UsuarioPremioVO> getlUsuPre() {
		return lUsuPre;
	}

	public void setlUsuPre(List<UsuarioPremioVO> lUsuPre) {
		this.lUsuPre = lUsuPre;
	}
	
	public List<ReportVO> getlReportsEmitidos() {
		return lReportsEmitidos;
	}

	public void setlReportsEmitidos(List<ReportVO> lReportsEmitidos) {
		this.lReportsEmitidos = lReportsEmitidos;
	}

	public List<ReportVO> getlReportsRecibidos() {
		return lReportsRecibidos;
	}

	public void setlReportsRecibidos(List<ReportVO> lReportsRecibidos) {
		this.lReportsRecibidos = lReportsRecibidos;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public LocalDate getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(LocalDate fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public int getPuntuacionTotal() {
		return puntuacionTotal;
	}

	public void setPuntuacionTotal(int puntuacionTotal) {
		this.puntuacionTotal = puntuacionTotal;
	}

	public int getFichas() {
		return fichas;
	}

	public void setFichas(int fichas) {
		this.fichas = fichas;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getImagenPerfil() {
		return imagenPerfil;
	}

	public void setImagenPerfil(String imagenPerfil) {
		this.imagenPerfil = imagenPerfil;
	}

	public CartonVO getCarton() {
		return carton;
	}

	public void setCarton(CartonVO carton) throws Exception {
		
		if (carton.isPremium()) {
			
			if (this.fichas >= CartonVO.PRECIO) {
				this.fichas -= CartonVO.PRECIO;
			} else {
				throw new Exception("No se ha podido asignar el carton, fichas insuficientes");
			}
		}
		
		this.carton = carton;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((carton == null) ? 0 : carton.hashCode());
		result = prime * result + ((correo == null) ? 0 : correo.hashCode());
		result = prime * result + (enabled ? 1231 : 1237);
		result = prime * result + ((fechaRegistro == null) ? 0 : fechaRegistro.hashCode());
		result = prime * result + fichas;
		result = prime * result + idUsuario;
		result = prime * result + ((imagenPerfil == null) ? 0 : imagenPerfil.hashCode());
		result = prime * result + ((lReportsEmitidos == null) ? 0 : lReportsEmitidos.hashCode());
		result = prime * result + ((lReportsRecibidos == null) ? 0 : lReportsRecibidos.hashCode());
		result = prime * result + ((lUsuPar == null) ? 0 : lUsuPar.hashCode());
		result = prime * result + ((lUsuPre == null) ? 0 : lUsuPre.hashCode());
		result = prime * result + ((lUsuUsuReceived == null) ? 0 : lUsuUsuReceived.hashCode());
		result = prime * result + ((lUsuUsuRequest == null) ? 0 : lUsuUsuRequest.hashCode());
		result = prime * result + ((lUsuarioRol == null) ? 0 : lUsuarioRol.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + puntuacionTotal;
		result = prime * result + ((sala == null) ? 0 : sala.hashCode());
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
		UsuarioVO other = (UsuarioVO) obj;
		if (carton == null) {
			if (other.carton != null)
				return false;
		} else if (!carton.equals(other.carton))
			return false;
		if (correo == null) {
			if (other.correo != null)
				return false;
		} else if (!correo.equals(other.correo))
			return false;
		if (enabled != other.enabled)
			return false;
		if (fechaRegistro == null) {
			if (other.fechaRegistro != null)
				return false;
		} else if (!fechaRegistro.equals(other.fechaRegistro))
			return false;
		if (fichas != other.fichas)
			return false;
		if (idUsuario != other.idUsuario)
			return false;
		if (imagenPerfil == null) {
			if (other.imagenPerfil != null)
				return false;
		} else if (!imagenPerfil.equals(other.imagenPerfil))
			return false;
		if (lReportsEmitidos == null) {
			if (other.lReportsEmitidos != null)
				return false;
		} else if (!lReportsEmitidos.equals(other.lReportsEmitidos))
			return false;
		if (lReportsRecibidos == null) {
			if (other.lReportsRecibidos != null)
				return false;
		} else if (!lReportsRecibidos.equals(other.lReportsRecibidos))
			return false;
		if (lUsuPar == null) {
			if (other.lUsuPar != null)
				return false;
		} else if (!lUsuPar.equals(other.lUsuPar))
			return false;
		if (lUsuPre == null) {
			if (other.lUsuPre != null)
				return false;
		} else if (!lUsuPre.equals(other.lUsuPre))
			return false;
		if (lUsuUsuReceived == null) {
			if (other.lUsuUsuReceived != null)
				return false;
		} else if (!lUsuUsuReceived.equals(other.lUsuUsuReceived))
			return false;
		if (lUsuUsuRequest == null) {
			if (other.lUsuUsuRequest != null)
				return false;
		} else if (!lUsuUsuRequest.equals(other.lUsuUsuRequest))
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
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (puntuacionTotal != other.puntuacionTotal)
			return false;
		if (sala == null) {
			if (other.sala != null)
				return false;
		} else if (!sala.equals(other.sala))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UsuarioVO [idUsuario=" + idUsuario + ", lUsuarioRol=" + lUsuarioRol + ", lUsuUsuRequest="
				+ lUsuUsuRequest + ", lUsuUsuReceived=" + lUsuUsuReceived + ", lUsuPar=" + lUsuPar + ", lUsuPre="
				+ lUsuPre + ", lReportsEmitidos=" + lReportsEmitidos + ", lReportsRecibidos=" + lReportsRecibidos
				+ ", sala=" + sala + ", nombre=" + nombre + ", correo=" + correo + ", password=" + password
				+ ", fechaRegistro=" + fechaRegistro + ", puntuacionTotal=" + puntuacionTotal + ", fichas=" + fichas
				+ ", enabled=" + enabled + ", imagenPerfil=" + imagenPerfil + ", carton=" + carton + "]";
	}

}