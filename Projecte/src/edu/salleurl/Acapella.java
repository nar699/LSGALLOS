package edu.salleurl;

/**
 * Classe on calcularem els punts corresponents de la batalla ACapella
 * @version 30/12/2020
 * @author Marc Postils i Narcís Cisquella
 */
//Herència de batalla.
public class Acapella extends Batalla {
    @Override
    public String toString() {
        return "Acapella";
    }

    /**
     * Constructor que crea l'objecte Acapella, format pel tema.
     */
    public Acapella(Tema tema) {
        super(tema);
    }

    /**
     * Mètode que s'encarregarà de calcular els punts totals del rapero.
     * @param rimes Conté el número de rimes.
     * @return Retorna els punts totals.
     */
    @Override
    protected double puntosTotales(int rimes) {
        return (6*Math.sqrt(rimes)+3)/2;
    }
}
