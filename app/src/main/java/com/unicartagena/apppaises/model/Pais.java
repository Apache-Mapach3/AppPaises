package com.unicartagena.apppaises.model;

public class Pais {
    private Long id;
    private String nombre;
    private String presidente;
    private String continente;
    private String nombreHimno;
    private int numHabitantes;
    private int numDepartamentos;
    private int numMunicipios;
    private String idiomaPpal;
    private int numUniversidades;
    private String tipoDemocracia;

    // Constructor vacío (necesario para Retrofit/Gson)
    public Pais() {}

    // Constructor completo
    public Pais(Long id, String nombre, String presidente, String continente,
                String nombreHimno, int numHabitantes, int numDepartamentos,
                int numMunicipios, String idiomaPpal, int numUniversidades,
                String tipoDemocracia) {
        this.id = id;
        this.nombre = nombre;
        this.presidente = presidente;
        this.continente = continente;
        this.nombreHimno = nombreHimno;
        this.numHabitantes = numHabitantes;
        this.numDepartamentos = numDepartamentos;
        this.numMunicipios = numMunicipios;
        this.idiomaPpal = idiomaPpal;
        this.numUniversidades = numUniversidades;
        this.tipoDemocracia = tipoDemocracia;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getPresidente() { return presidente; }
    public void setPresidente(String presidente) { this.presidente = presidente; }
    public String getContinente() { return continente; }
    public void setContinente(String continente) { this.continente = continente; }
    public String getNombreHimno() { return nombreHimno; }
    public void setNombreHimno(String nombreHimno) { this.nombreHimno = nombreHimno; }
    public int getNumHabitantes() { return numHabitantes; }
    public void setNumHabitantes(int numHabitantes) { this.numHabitantes = numHabitantes; }
    public int getNumDepartamentos() { return numDepartamentos; }
    public void setNumDepartamentos(int numDepartamentos) { this.numDepartamentos = numDepartamentos; }
    public int getNumMunicipios() { return numMunicipios; }
    public void setNumMunicipios(int numMunicipios) { this.numMunicipios = numMunicipios; }
    public String getIdiomaPpal() { return idiomaPpal; }
    public void setIdiomaPpal(String idiomaPpal) { this.idiomaPpal = idiomaPpal; }
    public int getNumUniversidades() { return numUniversidades; }
    public void setNumUniversidades(int numUniversidades) { this.numUniversidades = numUniversidades; }
    public String getTipoDemocracia() { return tipoDemocracia; }
    public void setTipoDemocracia(String tipoDemocracia) { this.tipoDemocracia = tipoDemocracia; }
}