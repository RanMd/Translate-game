// Jugador.java
package com.models;

public class Player {
    private String nombre;
    private int puntaje;
    private int puntajeTiempo;
    private int errores;


    public Player(String nombre) {
        this.nombre = nombre;
        this.puntaje = 0;
        this.puntajeTiempo = 0;
        this.errores = 0;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public void setPuntajeTiempo(int puntajeTiempo) {
        this.puntajeTiempo += puntajeTiempo;
    }

    public int getPuntajeTiempo() {return puntajeTiempo;}

    public int getErrores() {
        return errores;
    }

    public void incrementarErrores() {
        this.errores++;
    }

    public void incrementarPuntaje() {
        puntaje++;
    }


}
