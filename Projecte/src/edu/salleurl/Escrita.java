package edu.salleurl;

/**
 * Classe on calcularem els punts corresponents de la batalla Escrita.
 * @version 30/12/2020
 * @author Marc Postils i Narcís Cisquella
 */
//Herència de Batalla
public class Escrita extends Batalla {
    @Override
    public String toString() {
        return "Escrita";
    }

    /**
     * Constructor que crea l'objecte Escrita, format pel tema.
     */
    public Escrita(Tema tema) {
        super(tema);
    }

    /**
     * Mètode que s'encarregarà de calcular els punts totals del rapero.
     * @param rimes Conté el número de rimes.
     * @return Retorna els punts totals.
     */
    @Override
    protected double puntosTotales(int rimes) {
        return ((16 + 2 + 128 + 64 + 256 + 4 + 32 + 512 + 1024 + rimes)/(1024 + 128 + 4 + 64 + 16 + 256 + rimes + 2 + 32 + 512)) + (3*rimes);
    }
}
