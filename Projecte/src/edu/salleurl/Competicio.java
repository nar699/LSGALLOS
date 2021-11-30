package edu.salleurl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

/**
 * Classe on enregistrarem un rapero (si la competició no ha comensat) si així ho indica l'usuari i si es aíxí, l'afegirem
 * al "competicio.json". En canvi, si la competició ja ha comensat, l'usuari es loggejarà.
 * @version 22/12/2020
 * @author Marc Postils i Narcís Cisquella
 */
public class Competicio {
    private String name;
    private Date startDate;
    private Date endDate;
    private List<Fase> phases;
    // tret de internet, al llegir i escriure el json l'ignorarà
    private transient Menu menu;

    /**
     * Constructor que crea l'objecte Competició, format pel menú.
     */
    public Competicio() {
        menu = new Menu();
    }

    /**
     * Getter del nom de la competició.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter de la data d'inici.
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Getter de la data de finalització.
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Getter de les fases.
     */
    public List<Fase> getPhases() {
        return phases;
    }

    /**
     * Mètode que utilitzem per organitzar tot, per si la competició no ha comensat que cridi a un mètode i gestionar les
     * opcions Register o Leave, si la competició ha comensat però no ha acabat que cridi a un altre mètode i si la
     * competició ha acabat no mostrar res.
     * @param fitxerCompeticio Conté tota la informació del fitxer "competició.json".
     * @param fitxerTemes Conté tota la informació del fitxer "batalles.json".
     */
    public void start(FitxerCompeticio fitxerCompeticio, FitxerTemes fitxerTemes) {
        //Dona la data d'avui. Canviar-la manualment per veure les diferents opcions de menú.
        Date avui = new Date(2020-1900,11,15);
        menu.mostraInformacio(this, fitxerCompeticio.getRappers().size(), phases.size());
        if (avui.before(startDate)) {
            System.out.println("Competition hasn't started yet. Do you want to:");
            System.out.println();

            String opcio;
            do {
                opcio = menu.abansComensat();
                switch (opcio) {
                    case "1":
                        Rapero raperoRegistrat = menu.register(fitxerCompeticio.getRappers(), fitxerCompeticio.getCountries());
                        // afegirà el nou rapero introduit l'usuari a competicio.json
                        escriuJson(fitxerCompeticio, raperoRegistrat);
                        break;
                    case "2":
                        System.out.println();
                        System.out.println("Fins la propera!");
                        break;
                    default:
                        System.out.println();
                        System.out.println("Error, opció incorrecta!");
                        System.out.println();
                }
            } while (!opcio.equals("2"));
            // si la competició ha comensat però no ha acabat...
        } else if(avui.before(endDate)) {
            System.out.println("Competition started. Do you want to:");
            System.out.println();
            login(fitxerCompeticio, fitxerTemes);
            // si la competició ha acabat...
        } else {
            System.out.println("La competició ha acabat, a reveure!");
        }

    }

    /**
     * Mètode que utilitzem per escriure al fitxer "competició.json" la informació del nou rapero enregistrat per l'usuari.
     * @param fitxerCompeticio Conté tota la informació del fitxer "competició.json".
     * @param register Conté tota la informació del rapero introduïda per l'usuari.
     */
    private void escriuJson(FitxerCompeticio fitxerCompeticio, Rapero register) {
        fitxerCompeticio.getRappers().add(register);
        //El pretty printing és per què no surti tot escrit en una sola línia. El date format és perquè surti bé el format de la data.
        Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd").create();

        try {
            Writer writer = new FileWriter("competició.json");
            gson.toJson(fitxerCompeticio, writer);
            writer.close();
        } catch (IOException e) {
            System.out.println("Error, no s'ha pogut afegir el nou rapero!");
        }

    }

    /**
     * Mètode que utilitzem per implementar les opcions de fer Login o Leave, controlant que el nom del rapero introduït
     * coincideixi amb un existent.
     * @param fitxerCompeticio Conté tota la informació del fitxer "competició.json".
     * @param fitxerTemes Conté tota la informació del fitxer "batalles.json".
     */
    private void login(FitxerCompeticio fitxerCompeticio, FitxerTemes fitxerTemes) {
        String opcio;
        String valor;

        int j = 0;

        do {

            System.out.println("1. Login");
            System.out.println("2. Leave");
            System.out.println();
            opcio = menu.demanaString("Choose an option:", "Error, la cadena està buida.");

            switch (opcio) {
                case "1":
                    String nom;
                    Rapero usuari;

                    nom = menu.demanaString("Enter your artistic name: ", "Error, la cadena està buida.");
                    //System.out.println();
                    usuari = menu.cercaRapero(fitxerCompeticio.getRappers(), nom);
                    if (usuari == null) {
                        System.out.println("Yo' bro, there's no " + nom + " in ma' list.");
                        System.out.println();
                    } else {
                        List<Rapero> participants = new ArrayList<>(fitxerCompeticio.getRappers());
                        for (Fase fase:phases) {
                            participants = fase.executarFase(participants, usuari, fitxerTemes.getThemes(), phases.size());
                        }

                        //colocar el ultim menu, quan shan acabat totes les fases
                        List<Rapero> resultat = new ArrayList<>(fitxerCompeticio.getRappers());
                        resultat.sort(new Comparator<Rapero>() {
                            @Override
                            public int compare(Rapero o1, Rapero o2) {
                                return Double.compare(o2.getPunts(), o1.getPunts());
                            }
                        });

                        Rapero guanyador = resultat.get(0);

                        do {
                            System.out.println("---------------------------------------------------------------------------------");
                            System.out.print("Phase: " + phases.size() + "/" + phases.size() + " | " + " Score: " + (int) usuari.getPunts() + " | ");
                            if (guanyador.equals(usuari)) {
                                System.out.println(" You have win, congratulations bro!");
                                System.out.println("---------------------------------------------------------------------------------");
                            } else {
                                System.out.println("You've lost kid, I'm sure you'll do better next time...");
                                System.out.println("---------------------------------------------------------------------------------");
                            }
                            valor = menu.competicioAcabada();
                            switch (valor) {
                                case "1":
                                    System.out.println();
                                    System.out.println("Competition ended. You cannot battle anyone else!");
                                    System.out.println();
                                    break;
                                case "2":
                                    System.out.println();
                                    menu.ranking(resultat, usuari);
                                    System.out.println();
                                    break;
                                case "3":
                                    menu.creaPerfil(resultat);
                                    System.out.println();
                                    break;
                                case "4":
                                    System.exit(0);
                                    break;
                                default:
                                    System.out.println("Error, opció incorrecta!");
                            }
                        } while (!valor.equals("4"));

                    }

                    break;
                case "2":
                    System.out.println("Fins la propera!");
                    break;
                default:
                    System.out.println("Error! Opció incorrecta");
            }
        } while (!opcio.equals("2"));
    }
}
