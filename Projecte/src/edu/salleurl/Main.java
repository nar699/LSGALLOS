package edu.salleurl;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe Main que executem
 * @version 5/01/2021
 * @author Marc Postils i Narcís Cisquella
 */
public class Main {

    public static void main(String[] args) {
        Gson gson = new Gson();
        // lectura dels fitxers competició.json i batalles.json.
        try {
            BufferedReader readerCompeticio = Files.newBufferedReader(Path.of("competició.json"));
            FitxerCompeticio fitxerCompeticio = gson.fromJson(readerCompeticio, FitxerCompeticio.class);
            readerCompeticio.close();
            BufferedReader readerBatalla = Files.newBufferedReader(Path.of("batalles.json"));
            FitxerTemes fitxerBatalla = gson.fromJson(readerBatalla, FitxerTemes.class);
            readerBatalla.close();

            fitxerCompeticio.getCompeticio().start(fitxerCompeticio, fitxerBatalla);
        } catch (IOException e) {
            System.out.println("Error de la lectura dels fitxers!");
        }
    }

}
