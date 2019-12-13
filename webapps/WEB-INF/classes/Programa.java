import org.w3c.dom.Element;

public class Programa{
	private String edadMinima;
	private String langs;

	private String horaFin;
	private String nombrePrograma;
	private String categoria;
	private String horaInicio;
	private String duracion;
	
	public Programa() {}
	
	
	public Programa(String edadMinima, String langs, String nombrePrograma, String categoria, String horaInicio,
			String duracion, String horaFin) {
		this.edadMinima = edadMinima;
		this.langs = langs;
		this.nombrePrograma = nombrePrograma;
		this.categoria = categoria;
		this.horaInicio = horaInicio;
		this.duracion = duracion;
		this.horaFin = horaFin;
	}


	public String getEdadMinima() {
		return edadMinima;
	}


	public void setEdadMinima(String edadMinima) {
		this.edadMinima = edadMinima;
	}


	public String getLangs() {
		return langs;
	}


	public void setLangs(String langs) {
		this.langs = langs;
	}


	public String getNombrePrograma() {
		return nombrePrograma;
	}
	public void setNombrePrograma(String nombrePrograma) {
		this.nombrePrograma = nombrePrograma;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public String getHoraInicio() {
		return horaInicio;
	}
	public void setHoraInicio(String horaInicio) {
		this.horaInicio = horaInicio;
	}
	public String getDuracion() {
		return duracion;
	}
	public void setDuracion(String duracion) {
		this.duracion = duracion;
	}
	public String getHoraFin() {
		return horaFin;
	}
	public void setHoraFin(String horaFin) {
		this.horaFin = horaFin;
	}
}