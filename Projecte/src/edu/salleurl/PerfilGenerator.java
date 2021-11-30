package edu.salleurl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import edu.salleurl.profile.Profile;
import edu.salleurl.profile.ProfileFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Classe on generarem el perfil del rapero elegit per l'usuari i on generarà un html amb la informació del rapero.
 * @version 12/03/2021
 * @author Marc Postils i Narcís Cisquella
 */
public class PerfilGenerator {

    /**
     * Mètode que segons el país que sigui el rapero, visitarà el url corresponent i agafarà el contingut que hi ha.
     * @param pais Conté el país.
     * @return Retorna error si el status es diferent de 200 i retorna el contingut si el status és 200.
     */
    private static String obtenirJsonPais(String pais) {
        try {
            //Si té espais canvia per %20 i accents i tal.
            URL url = new URL("https://restcountries.eu/rest/v2/name/" + URLEncoder.encode(pais, StandardCharsets.UTF_8.toString()));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            int status = con.getResponseCode();
            //Si el status dona 200, significa que no dona cap error.
            if (status != 200) {
                throw new Exception("Error, resposta del servidor errònia!");
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();
            return content.toString();
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Mètode que utlitzem per afegir la informació del país del rapero i els seus punts
     * @param rapero És el rapero que ha elegit l'usuari
     */
    public static void generaPerfil(Rapero rapero) {
        String json = obtenirJsonPais(rapero.getNationality());
        if (json == null) {
            System.out.println("Error al obtenir la info del pais");
        } else {
            JsonObject info = new Gson().fromJson(json, JsonArray.class).get(0).getAsJsonObject();
            String nomFitxer = toLowerCamelCase(rapero.getStageName()) + ".html";
            Profile perfil = ProfileFactory.createProfile(nomFitxer, rapero);
            perfil.setCountry(rapero.getNationality());
            perfil.setFlagUrl(info.get("flag").getAsString());
            for(JsonElement element: info.get("languages").getAsJsonArray()) {
                perfil.addLanguage(element.getAsJsonObject().get("name").getAsString());
            }
            perfil.addExtra("Points", String.valueOf((int)rapero.getPunts()));
            try {
                perfil.writeAndOpen();
            } catch (IOException e) {
                System.out.println("Error!");
            }
        }
    }

    /**
     * Mètode que utlitzem per posar el nom del fitxer .html en minúscules.
     * @param text Nom del fitxer
     * @return Retorna la url del fitxer.
     */
    private static String toLowerCamelCase(String text) {
        String[] parts = text.split(" ");
        String resultat = parts[0].toLowerCase();
        for (int i = 1; i < parts.length; i++) {
            resultat += parts[i].substring(0, 1).toUpperCase() + parts[i].substring(1).toLowerCase();
        }
        return resultat;
    }

}
