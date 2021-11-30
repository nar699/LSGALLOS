package edu.salleurl;

import java.util.List;

/**
 * Classe on fem els getters de la informació de la competició, els païssos i dels raperos que hi ha al "competició.json".
 * @version 20/12/2020
 * @author Marc Postils i Narcís Cisquella
 */
public class FitxerCompeticio {
    private Competicio competition;
    private List<String> countries;
    private List<Rapero> rappers;

    /**
     * Getter de la competicio.
     */
    public Competicio getCompeticio() {
        return competition;
    }

    /**
     * Getter dels païssos.
     */
    public List<String> getCountries() {
        return countries;
    }

    /**
     * Getter dels raperos.
     */
    public List<Rapero> getRappers() {
        return rappers;
    }
}
