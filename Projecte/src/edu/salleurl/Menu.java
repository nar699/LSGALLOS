package edu.salleurl;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.rmi.server.ExportException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Classe on mostrarem tot lo que estigui relacionat amb els diferents menús, també els missatges d'error quan l'usuari
 * introdueix malament algún camp.
 * @version 13/03/2021
 * @author Marc Postils i Narcís Cisquella
 */
public class Menu {

    private Scanner scanner;
    private DateFormat dateFormat;

    /**
     * Constructor que crea l'objecte Menú, format pel scanner i el dateFormat.
     */
    public Menu() {
        scanner = new Scanner(System.in);
        //Assignem el format correcte de la data.
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    }

    /**
     * Mètode que utilitzem per mostrar les primeres linies que mostrarà el programa al compilar. És la
     * informació referent a la competició.
     * @param competicio Conté tota la informació referent a la competició.
     * @param numParticipants Són els número de participants que hi ha a la competició.
     * @param numFases És el número de fases que té la competició.
     */
    public void mostraInformacio(Competicio competicio, int numParticipants, int numFases) {
        System.out.println("Welcome to competition: " + competicio.getName());
        System.out.println("Starts on: " + dateFormat.format(competicio.getStartDate()));
        System.out.println("Ends on: " + dateFormat.format(competicio.getEndDate()));
        System.out.println("Phases: " + numFases);
        System.out.println("Currently: " + numParticipants + " participants");
        System.out.println();
    }

    /**
     * Mètode que utilitzem per mostrar el menú referent a quan la competició no ha comensat.
     * @return Retorna la opció que ha introduït l'usuari.
     */
    public String abansComensat() {
        System.out.println("1. Register");
        System.out.println("2. Leave");
        System.out.println();
        System.out.print("Option:");
        return scanner.nextLine();
    }

    /**
     * Mètode que utilitzem per mostrar la informació que se li demana al usuari quan s'ha de registrar.
     * @param participants Conté tota la informació referent a cada rapero.
     * @param paisos Conté tots els païssos d'on son els raperos.
     * @return Retorna la informació corresponent al rapero introduït per l'usuari.
     */
    public Rapero register(List<Rapero> participants, List<String> paisos) {
        //Creem una nova instància de rapero.
        Rapero rapero = new Rapero();

        System.out.println("--------------------------------------");
        System.out.println("Please, enter your personal information:");
        String fullName = demanaString("- Full name: ","Error, el nom complet introduït no conté cap caràcter!");
        //Afegim el full name.
        rapero.setRealName(fullName);
        Rapero aux;

        //Bucle que es repeteix sempre que el nom artístic introduït coincideixi amb algun ja existent.
        do {
            String artisticName = demanaString("- Artistic name: ", "Error, el nom artístic introduït no conté cap caràcter!");
            aux = cercaRapero(participants, artisticName);
            //Si el nom artistic coincideix amb el d'algú altre.
            if (aux != null) {
                System.out.println();
                System.out.println("Error, aquest nom artístic ja està ocupat!");
                System.out.println();
                //Si el nom artistic no coincideix amb cap altre.
            } else {
                //Afegim el artistic name.
                rapero.setStageName(artisticName);
            }
        } while (aux!=null);

        Date data;
        //Bucle que es repeteix quan el format de la data és incorrecte.
        do {
            String birthDate = demanaString("- Birth date (dd/MM/YYYY): ", "Error, la data introduïda està buida!");

            try {
                data = dateFormat.parse(birthDate);
            } catch (ParseException e) {
                //Serà null quan el format de la data sigui incorrecte.
                data = null;
            }
            //Si el format de la data és correcte la afegirem.
            if (data != null) {
               rapero.setBirth(data);
                //Si el format de la data és incorrecte...
            } else {
                System.out.println();
                System.out.println("Error, el format de la data introduïda és incorrecte!");
                System.out.println();
            }
        } while (data == null);

        String pais;
        do { //Bucle que es repeteix sempre que el país no sigui vàlid.
            pais = demanaString("- Country:", "Error, el país introduit no conté cap caràcter!");
            //Si el país introduit pertany al fitxer competicio.json...
            if (paisos.contains(pais)) {
                //Afegirem el país.
                rapero.setNationality(pais);
            } else {
                System.out.println();
                System.out.println("Error, el país introduit no és vàlid!");
                System.out.println();
            }
        } while(!paisos.contains(pais));

        String level;
        //Donem valor inicial.
        rapero.setLevel(-1);
        do {
            level = demanaString("- Level:", "Error, el nivell introduit està buit!");
            try {
                rapero.setLevel(Integer.parseInt(level)); //
            } catch (NumberFormatException e) {
                System.out.println();
                System.out.println("Error, el nivell introduit és incorrecte!");
                System.out.println();
                rapero.setLevel(-1);
            }

        } while(rapero.getLevel() == -1);

        String photoUrl = demanaString("- Photo URL:","Error, la url de la foto està buida!");
        //Afegim la url de la foto.
        rapero.setPhoto(photoUrl);

        System.out.println();
        System.out.println(" Registration completed!");
        System.out.println("--------------------------------------");

        return rapero;
    }

    /**
     * Mètode que utilitzem per mirar si el nom artístic introduït per l'usuari ja existeix al
     * "competicio.json".
     * @param participants Conté tota la informació referent a cada rapero.
     * @param nom És el nom artístic introduït per l'usuari.
     * @return Retorna la informació del rapero si el nom artístic introduït coincideix amb un ja existent
     * i retorna null si no coincideix amb cap altre.
     */
    public Rapero cercaRapero(List<Rapero> participants, String nom) {
        //Bucle per tots els raperos existents.
        for(Rapero rapero:participants) {
            //Si el nom artístic coincideix amb un que ja existeix el retornem
            if(rapero.getStageName().equalsIgnoreCase(nom) || rapero.getRealName().equalsIgnoreCase(nom)) {
                return rapero;
            }
        }
        return null;
    }

    /**
     * Mètode que utilitzem per mirar si la informació del rapero introduïda per l'usuari està buida o no.
     * @param text Pregunta que l'usuari haurà de respondre.
     * @param error Frase d'error que mostrarem quan l'usuari no introdueixi cap caràcter.
     * @return Retorna la informació que ha escrit l'usuari sempre i quan no estigui buida.
     */
    public String demanaString(String text, String error) {
        String valor;
        //Bucle que es repetirà fins que l'usuari entri un nom que no estigui buit.
        do {
            System.out.print(text);
            valor = scanner.nextLine();
            System.out.println();
            //Si no escriu res (enter) per pantalla, mostrarà l'error.
            if(valor.isEmpty()) {
                System.out.println(error);
            }

        } while(valor.isEmpty());
        return valor;
    }

    /**
     * Mètode que utilitzem per mostrar el menú del lobby.
     * @return Retorna la opció que ha escrit l'usuari per pantalla.
     */
    public String lobby() {
        //System.out.println();
        System.out.println("1. Start the battle");
        System.out.println("2. Show ranking");
        System.out.println("3. Create profile");
        System.out.println("4. Leave competition");
        System.out.println();
        System.out.print("Choose an option:");

        return scanner.nextLine();
    }

    public String competicioAcabada() {
        System.out.println("1. Start the battle (deactivated)");
        System.out.println("2. Show ranking");
        System.out.println("3. Create profile");
        System.out.println("4. Leave competition");
        System.out.println();
        System.out.print("Choose an option:");

        return scanner.nextLine();
    }

    /**
     * Mètode que utilitzem per mostrar el rànking de la competició i en quina posició està el nostre
     * rapero.
     * @param participants Conté tota la informació referent a cada rapero.
     * @param usuari És el rapero que ha elegit l'usuari.
     */
    public void ranking(List<Rapero> participants, Rapero usuari) {
        participants.sort(new Comparator<Rapero>() {
            @Override
            public int compare(Rapero o1, Rapero o2) {
                return Double.compare(o2.getPunts(), o1.getPunts());
            }
        });

        System.out.println("------------------------");
        System.out.println("Pos. | Name | Score");
        System.out.println("------------------------");
        System.out.println();

        for (int i = 0; i < participants.size(); i++) {
            Rapero rapero = participants.get(i);
            System.out.printf("%d. %s - %d", i+1, rapero.getStageName(), (int)rapero.getPunts());
            if (rapero.equals(usuari)) {
                System.out.println(" <-- You");
            } else {
                System.out.println();
            }

        }

    }

    /**
     * Mètode que utilitzem per configurar la opció de crear el perfil.
     * @param participants Conté tota la informació referent a cada rapero.
     */
    public void creaPerfil(List<Rapero> participants) {
        System.out.println("Enter the name of the rapper: ");
        String nom = scanner.nextLine();
        Rapero rapero = cercaRapero(participants, nom);
        if(rapero == null) {
            System.out.println("Error, no existeix cap rapero amb aquest nom");
        } else{
            System.out.println();
            System.out.println("Getting information about their country of origin (" + rapero.getNationality() + ")");
            PerfilGenerator.generaPerfil(rapero);
        }
    }


}


