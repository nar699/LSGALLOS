package edu.salleurl;

import java.util.*;

/**
 * Classe on gestionarem tot el tema de les batalles, ja sigui la suma dels punts com l'emparellament i el torn dels
 * raperos.
 * @version 10/02/2020
 * @author Marc Postils i Narcís Cisquella
 */
public abstract class Batalla { // super classe batalla //
    private Tema tema;

    /**
     * Constructor que crea l'objecte Batalla, format pel tema.
     * @param tema És el tema sobre que es rapejarà durant de la batalla.
     */
    protected Batalla(Tema tema) {
        this.tema = tema;
    }

    /**
     * Mètode que utilitzem per seleccionar un tema aleatòriament per la batalla.
     * @param temes Llista de temes existents.
     * @return Retorna el tema ja sigui una batalla Acapella, Sangre o Escrita.
     */
    public static Batalla aleatorio(List<Tema> temes) {
        Random random = new Random();
        Tema tema = temes.get(random.nextInt(temes.size()));
        switch (random.nextInt(3)) {
            case 0:
            return new Acapella(tema);

            case 1:
            return new Sangre(tema);

            case 2:
            return new Escrita(tema);

        }
        return null;
    }

    protected abstract double puntosTotales(int rimes);

    /**
     * Mètode que utilitzem per simular totes les batalles que no tinguin res a veure amb el nostre rapero.
     * @param lluites Llista on hi ha tots els raperos de la competició.
     * @param usuari És el nostre rapero.
     */
    public void simularBatalla(List<Rapero[]> lluites, Rapero usuari) {
        Random random = new Random();
        int rimesAleatories;

        for (Rapero[] lluita: lluites) {
            //Si en aquesta lluita no conté el nostre rapero
            if (!Arrays.asList(lluita).contains(usuari)) {
                for (Rapero rapero: lluita) {
                    for (int i = 0; i < 2; i++) {
                        //Generarà entre 0 i 4 rimes aleatòries
                        rimesAleatories = random.nextInt(5);
                        rapero.sumarPunts(puntosTotales(rimesAleatories));
                    }
                }
            }
        }
    }

    /**
     * Mètode que utilitzem per triar aleatòriament quin dels dos raperos comença a rapejar, si el nostre o el
     * contrincant. Si és el torn del contrincant s'escriuran automáticament les rimes corresponents. Si és el nostre
     * torn, escriurem manualment la rima.
     * @param usuari És el nostre rapero.
     * @param lluites Llista on hi ha tots els raperos de la competició.
     */
    public void executa(Rapero usuari, List<Rapero[]> lluites) {
        Rapero contrincant = trobaContrincant(lluites, usuari);
        Random random = new Random();
        Rapero[] turns = new Rapero[2];
        int rimaActual = 0;
        Scanner scanner = new Scanner(System.in);

        if(random.nextBoolean()) {
            turns[0] = usuari;
            turns[1] = contrincant;
        } else{
            turns[0] = contrincant;
            turns[1] = usuari;
        }

        System.out.println();
        System.out.println("Topic: " + tema.getName());
        System.out.println();
        for (int i = 0; i < 4; i++) {
            Rapero rapero = turns[i%2]; // 0 1 0 1
            String rima = "";

            if (rapero.equals(usuari)) {
                System.out.println("Your turn!");
                System.out.println("Enter your verse:");
                for (int j = 0; j < 4; j++) {
                    rima += scanner.nextLine() + "\n";
                }
                System.out.println();
            } else {
                rima = tema.getRhyme(rapero.getLevel(), rimaActual);
                rimaActual++;
                //System.out.println();
                System.out.println(rapero.getStageName() + ":");
                System.out.println(rima);
                System.out.println();
            }

            int rimesTotals = contaRimes(rima);
            rapero.sumarPunts(puntosTotales(rimesTotals));
        }
    }

    /**
     * Mètode que utilitzem per contar el número de rimes que el rapero ha conseguit rimar durant la batalla.
     * @param rima És la rima que ha escrit el contrincant i després el nostre usuari. L'ordre depèn del seu torn.
     * @return Retorna la quantitat de rimes que han pogut fer.
     */
    private int contaRimes(String rima) {
        int numTotal = 0;
        //Ens separa les linies per \n.
        String[] linies = rima.split("\n");
        Map<String, Integer> comptador = new HashMap<>();

        if (!rima.isEmpty() && linies.length > 1) {
            for (int i = 0; i < linies.length; i++) {
                //Treu tot lo que no siguin lletres i ho substitueix per "".
                String linia = linies[i].replaceAll("[^a-zA-Z]", "").toLowerCase();

                if (linia.length() >= 2) {
                    //Úlitms 2 caràcters.
                    linia = linia.substring(linia.length() - 2);
                    Integer actual = comptador.get(linia);
                    if (actual == null) {
                        actual = 1;
                    } else {
                        actual++;
                    }
                    comptador.put(linia, actual);
                }

            }
        }

        for (String clau : comptador.keySet()) {
            if (comptador.get(clau) > 1) {
                numTotal += comptador.get(clau);
            }
        }

        return numTotal;
    }

    /**
     * Mètode que utilitzem per trobar-li contrincant al nostre usuari.
     * @param lluites Llista on hi ha tots els raperos de la competició.
     * @param usuari És el nostre rapero.
     * @return Retorna el rapero contra el que batallarem.
     */
    public Rapero trobaContrincant(List<Rapero[]> lluites, Rapero usuari) {
        for (Rapero[] lluita: lluites) {
                for (Rapero rapero: lluita) {
                    if(!rapero.equals(usuari)) {
                        return rapero;
                    }
                }
            }
        return null;
    }
}
