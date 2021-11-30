package edu.salleurl;

import java.util.List;
import java.util.Map;

/**
 * Classe on fem els getters del nom del tema i de les rimes que i ha al "batalles.json".
 * @version 23/12/2020
 * @author Marc Postils i Narcís Cisquella
 */
public class Tema {
    private String name;
    //Mapa
    private List<Map<Integer, List<String>>> rhymes;

    /**
     * Getter del nom del tema.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter de la rima.
     * @param nivel Conté el nivell de la rima.
     * @param numRima Conté el número de la rima.
     * @return Retorna la rima si no s'ha produit cap error, sino retornarà una cadena buida.
     */
    public String getRhyme(int nivel, int numRima) {
        try{
            return rhymes.get(0).get(nivel).get(numRima);

        } catch (IndexOutOfBoundsException |NullPointerException ex) {
            return "";
        }
    }
}
