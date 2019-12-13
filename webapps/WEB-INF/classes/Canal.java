import java.util.ArrayList;

import org.w3c.dom.Element;

public class Canal{
	private String lang;
	
	private String idCanal;
	
	private String nombreCanal;
    private String grupo;
    private ArrayList<Programa> programas = new ArrayList<Programa>();
    
    
    
	public Canal(){}
	
	public Canal(String lang, String idCanal, String nombreCanal, String grupo, ArrayList<Programa> programas) {
		this.lang = lang;
		this.idCanal = idCanal;
		this.nombreCanal = nombreCanal;
		this.grupo = grupo;
		this.programas = programas;
	}



	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getIdCanal() {
		return idCanal;
	}

	public void setIdCanal(String idCanal) {
		this.idCanal = idCanal;
	}
	public String getNombreCanal() {
		return nombreCanal;
	}
	public void setNombreCanal(String nombreCanal) {
		this.nombreCanal = nombreCanal;
	}
	public String getGrupo() {
		return grupo;
	}
	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	public ArrayList<Programa> getProgramas() {
		return programas;
	}

	public void setProgramas(ArrayList<Programa> programas) {
		this.programas = programas;
	}
	
   
    
}