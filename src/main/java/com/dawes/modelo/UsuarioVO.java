package com.dawes.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
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

@Entity
@Table(name="usuarios")
public class UsuarioVO {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idUsuario;
	
	@ManyToOne
	@JoinColumn(name="idRol", nullable=false, columnDefinition="int default 0")
	private RolVO rol;
	
	@OneToMany(mappedBy = "usuario1", cascade=CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<UsuarioUsuarioVO> lUsuUsuRequest;
	
	@OneToMany(mappedBy = "usuario2", cascade=CascadeType.ALL, orphanRemoval = true)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<UsuarioUsuarioVO> lUsuUsuReceived;
	
	@OneToMany(mappedBy = "usuario", cascade=CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	@NotFound(action = NotFoundAction.IGNORE)
	private List<UsuarioPartidaVO> lUsuPar;
	
	@OneToMany(mappedBy = "usuario", cascade=CascadeType.ALL, orphanRemoval = true)
	@LazyCollection(LazyCollectionOption.FALSE)
	@NotFound(action = NotFoundAction.IGNORE)
	private List<UsuarioSalaVO> lUsuSal;
	
	@OneToMany(mappedBy = "usuario", cascade=CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<UsuarioPremioVO> lUsuPre;
	
	@Column(nullable=false, length=30)
	private String nombre;
	
	@Column(nullable=false)
	private String correo;
	
	@Column(nullable=false)
	private String password;

	@Column(nullable=false)
	private LocalDate fechaRegistro;
	
	@ColumnDefault("'0'")
	private int puntuacionTotal;
	
	@ColumnDefault("'0'")
	private int fichas;
	
	@Lob
	private byte[] imagenPerfil;
	
	@Transient
	CartonVO carton;

	public UsuarioVO() {
		super();
		
		this.lUsuUsuRequest = new ArrayList<UsuarioUsuarioVO>();
		this.lUsuUsuReceived = new ArrayList<UsuarioUsuarioVO>();
		this.lUsuPar = new ArrayList<UsuarioPartidaVO>();
		this.lUsuSal = new ArrayList<UsuarioSalaVO>();
		this.lUsuPre = new ArrayList<UsuarioPremioVO>();
	}

	public UsuarioVO(RolVO rol, List<UsuarioUsuarioVO> lUsuUsuRequest, List<UsuarioUsuarioVO> lUsuUsuReceived,
			List<UsuarioPartidaVO> lUsuPar, List<UsuarioSalaVO> lUsuSal, List<UsuarioPremioVO> lUsuPre, String nombre,
			String correo, String password, LocalDate fechaRegistro, int puntuacionTotal, int fichas,
			byte[] imagenPerfil) {
		super();
		this.rol = rol;
		this.lUsuUsuRequest = lUsuUsuRequest;
		this.lUsuUsuReceived = lUsuUsuReceived;
		this.lUsuPar = lUsuPar;
		this.lUsuSal = lUsuSal;
		this.lUsuPre = lUsuPre;
		this.nombre = nombre;
		this.correo = correo;
		this.password = password;
		this.fechaRegistro = fechaRegistro;
		this.puntuacionTotal = puntuacionTotal;
		this.fichas = fichas;
		this.imagenPerfil = imagenPerfil;
	}

	public UsuarioVO(int idUsuario, RolVO rol, List<UsuarioUsuarioVO> lUsuUsuRequest,
			List<UsuarioUsuarioVO> lUsuUsuReceived, List<UsuarioPartidaVO> lUsuPar, List<UsuarioSalaVO> lUsuSal,
			List<UsuarioPremioVO> lUsuPre, String nombre, String correo, String password, LocalDate fechaRegistro,
			int puntuacionTotal, int fichas, byte[] imagenPerfil) {
		super();
		this.idUsuario = idUsuario;
		this.rol = rol;
		this.lUsuUsuRequest = lUsuUsuRequest;
		this.lUsuUsuReceived = lUsuUsuReceived;
		this.lUsuPar = lUsuPar;
		this.lUsuSal = lUsuSal;
		this.lUsuPre = lUsuPre;
		this.nombre = nombre;
		this.correo = correo;
		this.password = password;
		this.fechaRegistro = fechaRegistro;
		this.puntuacionTotal = puntuacionTotal;
		this.fichas = fichas;
		this.imagenPerfil = imagenPerfil;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public RolVO getRol() {
		return rol;
	}

	public void setRol(RolVO rol) {
		this.rol = rol;
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

	public List<UsuarioSalaVO> getlUsuSal() {
		return lUsuSal;
	}

	public void setlUsuSal(List<UsuarioSalaVO> lUsuSal) {
		this.lUsuSal = lUsuSal;
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

	public byte[] getImagenPerfil() {
		return imagenPerfil;
	}

	public void setImagenPerfil(byte[] imagenPerfil) {
		this.imagenPerfil = imagenPerfil;
	}

	public CartonVO getCarton() {
		return carton;
	}

	public void setCarton(CartonVO carton) throws Exception {
		
		if (carton.isPremium()) {
			
			if (this.fichas >= CartonVO.precio) {
				this.fichas -= CartonVO.precio;
			} else {
				throw new Exception("No se ha podido asignar el carton, fichas innecesarias");
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
		result = prime * result + ((fechaRegistro == null) ? 0 : fechaRegistro.hashCode());
		result = prime * result + fichas;
		result = prime * result + idUsuario;
		result = prime * result + Arrays.hashCode(imagenPerfil);
		result = prime * result + ((lUsuPar == null) ? 0 : lUsuPar.hashCode());
		result = prime * result + ((lUsuPre == null) ? 0 : lUsuPre.hashCode());
		result = prime * result + ((lUsuSal == null) ? 0 : lUsuSal.hashCode());
		result = prime * result + ((lUsuUsuReceived == null) ? 0 : lUsuUsuReceived.hashCode());
		result = prime * result + ((lUsuUsuRequest == null) ? 0 : lUsuUsuRequest.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + puntuacionTotal;
		result = prime * result + ((rol == null) ? 0 : rol.hashCode());
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
		if (fechaRegistro == null) {
			if (other.fechaRegistro != null)
				return false;
		} else if (!fechaRegistro.equals(other.fechaRegistro))
			return false;
		if (fichas != other.fichas)
			return false;
		if (idUsuario != other.idUsuario)
			return false;
		if (!Arrays.equals(imagenPerfil, other.imagenPerfil))
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
		if (lUsuSal == null) {
			if (other.lUsuSal != null)
				return false;
		} else if (!lUsuSal.equals(other.lUsuSal))
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
		if (rol == null) {
			if (other.rol != null)
				return false;
		} else if (!rol.equals(other.rol))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UsuarioVO [idUsuario=" + idUsuario + ", rol=" + rol + ", lUsuUsuRequest=" + lUsuUsuRequest
				+ ", lUsuUsuReceived=" + lUsuUsuReceived + ", lUsuPar=" + lUsuPar + ", lUsuSal=" + lUsuSal
				+ ", lUsuPre=" + lUsuPre + ", nombre=" + nombre + ", correo=" + correo + ", password=" + password
				+ ", fechaRegistro=" + fechaRegistro + ", puntuacionTotal=" + puntuacionTotal + ", fichas=" + fichas
				+ ", imagenPerfil=" + Arrays.toString(imagenPerfil) + ", carton=" + carton + "]";
	}
	
}
