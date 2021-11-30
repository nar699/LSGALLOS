package edu.salleurl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Classe on ens encarragarem de gestionar el menú un cop l'usuari s'ha loggejat i també d'organitzar les batalles
 * segons la fase en que ens trobem i el número de raperos que quedin.
 * @version 05/02/2020
 * @author Marc Postils i Narcís Cisquella
 */
public class Fase {
    private double budget;
    private String country;
    private transient Menu menu;
    //Static perquè no pertany a la instància i pertany a la classe.
    private static int faseActual = 1;

    /**
     * Constructor que crea l'objecte Fase, format pel menú.
     */
    public Fase() {
        menu = new Menu();
    }

    /**
     * Getter del pressupost.
     */
    public double getBudget() {
        return budget;
    }

    /**
     * Getter del nom de la competició.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Mètode que descarta un rapero si el nombre de raperos és imparell i genera batalles aleatòries, implementem la
     * opció del menú despres de fer el login i ordenem els raperos segons els seus punts
     * @param participants Conté una llista dels raperos que participen a la competició.
     * @param usuari És el nostre rapero.
     * @param temes Conté una llista de tots els temes.
     * @param fasesTotals Número de fases totals.
     * @return Retorna una llista amb els participants que avançen a la següent fase.
     */
    public List<Rapero> executarFase(List<Rapero> participants, Rapero usuari, List<Tema> temes, int fasesTotals) {

        if (participants.size()%2 == 1) {
            Rapero raperoAleatori;
            Random random = new Random();
            do {
                raperoAleatori = participants.get(random.nextInt(participants.size()));
            } while(raperoAleatori.equals(usuari)); // per no descartar el nostre
            participants.remove(raperoAleatori);
        }

        //Genera 2 batalles aletaroies amb un tema cada un.
        List<Batalla> batalles = List.of(Batalla.aleatorio(temes), Batalla.aleatorio(temes));
        List<Rapero[]> lluites = lluitesAleatories(participants);
        String opcio;

        for (Batalla batalla:batalles) {
            batalla.simularBatalla(lluites, usuari);
        }

        int i = 0;


        if (participants.contains(usuari)) {
            do {
                System.out.println("--------------------------------------------------------------------------");
                System.out.println("Phase: " + faseActual + '/' + fasesTotals + " | " + "Score: " + (int) usuari.getPunts() + " | " + "Battle: " + (i + 1) + "/2: " + batalles.get(i).toString() + " | " + "Rival: " + batalles.get(i).trobaContrincant(lluites, usuari).getStageName());
                System.out.println("--------------------------------------------------------------------------");
                opcio = menu.lobby();
                switch (opcio) {
                    case "1":
                        batalles.get(i).executa(usuari, lluites);
                        i++;
                        break;
                    case "2":
                        System.out.println();
                        menu.ranking(participants, usuari);
                        System.out.println();
                        break;
                    case "3":
                        menu.creaPerfil(participants);
                        System.out.println();
                        break;
                    case "4":
                        System.out.println();
                        System.out.println("Sortint de la competició...");
                        System.out.println();
                        break;
                    default:
                        System.out.println();
                        System.out.println("Error, opció incorrecta!");
                        System.out.println();
                }
            } while (!opcio.equals("4") && i < batalles.size());
        }
        participants.sort(new Comparator<Rapero>() {
            @Override
            public int compare(Rapero o1, Rapero o2) {
                return Double.compare(o2.getPunts(), o1.getPunts());
            }
        });
        List<Rapero> seguentsParticipants = participants;
        if (faseActual == 1 && fasesTotals == 3) {
            seguentsParticipants = participants.subList(0, participants.size()/2);
        } else if ((faseActual == 2 && fasesTotals == 3) || (faseActual == 1 && fasesTotals == 2)) {
            seguentsParticipants = participants.subList(0, 2);
        }

        faseActual++;

        return new ArrayList<>(seguentsParticipants);
    }

    /**
     * Mètode que crea les lluites aleatòries.
     * @param participants Conté una llista dels raperos que participen a la competició.
     * @return Retorna una llista de totes les lluites.
     */
    private List<Rapero[]> lluitesAleatories(List<Rapero> participants) {
        //1a casella el rapero que va primer, 2a el segon...
        List<Rapero[]> lluites = new ArrayList<>();
        participants = new ArrayList<>(participants);
        Random random = new Random(System.currentTimeMillis());

        while (!participants.isEmpty()) {
            Rapero rapero1 = participants.get(random.nextInt(participants.size()));
            participants.remove(rapero1);
            Rapero rapero2 = participants.get(random.nextInt(participants.size()));
            participants.remove(rapero2);
            lluites.add(new Rapero[]{rapero1, rapero2});
        }

        return lluites;
    }

}
