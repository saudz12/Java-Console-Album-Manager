package org.example.Interfaces;

import org.javatuples.Triplet;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public interface IRecord {
    enum ContributionType{
        Producer,
        Performer,
        Writer,
        Composer,
    }

    boolean SetPrice(double price);
    double GetPrice();

    void SetName(String length);
    String GetName();

    public Triplet<Integer, Integer, Integer> GetLength();
    public boolean SetLength(int hours, int minutes, int seconds);
    public String LengthToString();

    boolean SetReleaseDate(String date);
    String GetReleaseDate();

    void SetGenres(Set<String> genres);
    Set<String> GetGenres();

    public Map<String, Object> MapObject();

    boolean AddArtist(String name, ContributionType contribution);

    boolean SetArtists(Set<String> artists, ContributionType contribution);
    boolean RemoveContribution(String name, ContributionType contribution);
    boolean RemoveArtist(String name);

    boolean AddGenre(String genre);
        boolean RemoveGenre(String genre);

}
