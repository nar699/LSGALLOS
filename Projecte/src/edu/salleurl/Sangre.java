package edu.salleurl;

/**
 * Classe on calcularem els punts corresponents de la batalla Sangre.
 * @version 30/12/2020
 * @author Marc Postils i Narcís Cisquella
 */
//Herència de Batalla
public class Sangre extends Batalla {
    @Override
    public String toString() {
        return "Sangre";
    }

    /**
     * Constructor que crea l'objecte Sangre, format pel tema.
     */
    public Sangre(Tema tema) {
        super(tema);
    }

    /**
     * Mètode que s'encarregarà de calcular els punts totals del rapero.
     * @param rimes Conté el número de rimes.
     * @return Retorna els punts totals.
     */
    @Override
    protected double puntosTotales(int rimes) {
        return Math.PI*Math.pow(rimes,2)/4;
    }
}
