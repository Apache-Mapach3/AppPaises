package com.unicartagena.apppaises.model;

public class Ciudad implements java.io.Serializable {
    private Long id;
    private String nombre;
    private String paisPertenece;
    private String alcalde;
    private String clima;

    public Ciudad() {}

    public Ciudad(Long id, String nombre, String paisPertenece, String alcalde, String clima) {
        this.id = id;
        this.nombre = nombre;
        this.paisPertenece = paisPertenece;
        this.alcalde = alcalde;
        this.clima = clima;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getPaisPertenece() { return paisPertenece; }
    public void setPaisPertenece(String paisPertenece) { this.paisPertenece = paisPertenece; }
    public String getAlcalde() { return alcalde; }
    public void setAlcalde(String alcalde) { this.alcalde = alcalde; }
    public String getClima() { return clima; }
    public void setClima(String clima) { this.clima = clima; }
}