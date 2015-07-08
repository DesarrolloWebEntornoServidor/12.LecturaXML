package es.dwes.pojos;

public class Empleado {

	private int id;
	private int edad;
	private String nombre;
	private String genero;
	private String oficio;
	
	public Empleado() {
	
	}
	
	public Empleado(int id, int edad, String nombre, String genero, String oficio) {
		super();
		this.id = id;
		this.edad = edad;
		this.nombre = nombre;
		this.genero = genero;
		this.oficio = oficio;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getEdad() {
		return edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public String getOficio() {
		return oficio;
	}

	public void setOficio(String oficio) {
		this.oficio = oficio;
	};
	
	
}
