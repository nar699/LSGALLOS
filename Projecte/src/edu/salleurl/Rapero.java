package edu.salleurl;

import edu.salleurl.profile.Profileable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * Classe on fem els getters i setters referents a la informació del rapero i també una part de lo referent a la
 * generació del perfil del rapero.
 * @version 10/03/2020
 * @author Marc Postils i Narcís Cisquella
 */
public class Rapero implements Profileable {
    private String realName;
    private String stageName;
    private Date birth;
    private String nationality;
    private int level;
    private String photo;
    private transient double punts;

    /**
     * Getter del nom complet del rapero.
     */
    public String getRealName() {
        return realName;
    }

    /**
     * Getter del nom artístic del rapero.
     */
    public String getStageName() {
        return stageName;
    }

    /**
     * Getter de la data de naixament del rapero.
     */
    public Date getBirth() {
        return birth;
    }

    /**
     * Getter de la nacionalitat del rapero.
     */
    public String getNationality() {
        return nationality;
    }

    /**
     * Getter del nivell del rapero.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Getter del url de la foto del rapero.
     */
    public String getPhoto() {
        return photo;
    }

    /**
     * Getter dels punts del rapero.
     */
    public double getPunts() {
        return punts;
    }

    /**
     * Setter del nom complet del rapero.
     * @param realName Nom complet del rapero.
     */
    public void setRealName(String realName) {
        this.realName = realName;
    }

    /**
     * Setter del stageName del rapero.
     * @param stageName Nom artístic del rapero.
     */
    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    /**
     * Setter de la data de naixament del rapero.
     * @param birth Data de naixament del rapero.
     */
    public void setBirth(Date birth) {
        this.birth = birth;
    }

    /**
     * Setter de la nacionalitat del rapero.
     * @param nationality Nacionalitat del rapero.
     */
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    /**
     * Setter del nivell del rapero.
     * @param level Nivell del rapero.
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Setter del url de la foto del rapero.
     * @param photo Url de la foto del rapero.
     */
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rapero rapero = (Rapero) o;
        return level == rapero.level &&
                Objects.equals(realName, rapero.realName) &&
                Objects.equals(stageName, rapero.stageName) &&
                Objects.equals(birth, rapero.birth) &&
                Objects.equals(nationality, rapero.nationality) &&
                Objects.equals(photo, rapero.photo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(realName, stageName, birth, nationality, level, photo);
    }

    /**
     * Mètode que utilitzem per anar sumant els punts a mesura que avançen les batalles.
     * @param punts Quantitat de punts obtinguts pel rapero.
     */
    public void sumarPunts (double punts) {
        this.punts += punts;
    }

    @Override
    public String getName() {
        return realName;
    }

    @Override
    public String getNickname() {
        return stageName;
    }

    @Override
    public String getBirthdate() {
        return new SimpleDateFormat("dd/MM/yyyy").format(birth);
    }

    @Override
    public String getPictureUrl() {
        return photo;
    }
}
