package org.example.Modules;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Interfaces.IRecord;
import org.javatuples.Triplet;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class RecordBase implements IRecord {
    private String name;
    private String releaseDate;
    private Triplet<Integer, Integer, Integer> length;
    private Set<String> genres;
    private double price;

    public RecordBase(String name){
        if(name.isEmpty())
            throw new RuntimeException("Can't have empty album name");
        this.name = name;
        this.releaseDate = "";
        this.length = new Triplet<>(0, 0, 0);
        this.genres = new HashSet<String>();
        this.price = 0;
    }

    public RecordBase(String name, Triplet<Integer,Integer,Integer> length){
        if(name.isEmpty())
            throw new RuntimeException("Can't have empty album name");
        this.name = name;
        this.releaseDate = "";
        this.length = length;
        this.genres = new HashSet<String>();
        this.price = 0;
    }

    public RecordBase(String name, Triplet<Integer,Integer,Integer> length, double price){
        if(name.isEmpty())
            throw new RuntimeException("Can't have empty album name");
        this.name = name;
        this.releaseDate = "";
        this.length = length;
        this.genres = new HashSet<String>();
        this.price = price;
    }

    @Override
    public Map<String, Object> MapObject(){
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("name", this.GetName());
        jsonMap.put("release_date", this.GetReleaseDate());
        jsonMap.put("length", this.LengthToString());
        jsonMap.put("price", this.GetPrice());
        jsonMap.put("genres", this.GetGenres().toArray());


        return jsonMap;
    }

    @Override
    public void SetName(String name){
        this.name = name;
    }

    @Override
    public String GetName(){
        return this.name;
    }

    @Override
    public boolean SetPrice(double price){
        if(price < 0)
            return false;
        this.price = price;
        return true;
    }

    @Override
    public double GetPrice(){
        return this.price;
    }

    @Override
    public String LengthToString(){
        var length = this.GetLength();
        return "" + length.getValue0() + ":" + length.getValue1() + ":" + length.getValue2();
    }

    @Override
    public boolean SetLength(int hours, int minutes, int seconds){
        if(hours < 0 || minutes < 0 || seconds < 0)
            return false;
        this.length = new Triplet<>(hours, minutes, seconds);
        return true;
    }

    @Override
    public Triplet<Integer, Integer, Integer> GetLength(){
        return this.length;
    }

    @Override
    public boolean SetReleaseDate(String date){
        Pattern pattern = Pattern.compile("\\d\\d/\\d\\d/\\d\\d\\d\\d", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(date);
        if(matcher.find()) {
            this.releaseDate = date;
            return true;
        }
        return false;
    }

    @Override
    public String GetReleaseDate(){
        return this.releaseDate;
    }

    @Override
    public void SetGenres(Set<String> genres){
        this.genres = genres;
    }

    @Override
    public Set<String> GetGenres(){
        return this.genres;
    }

    @Override public boolean AddGenre(String genre){
        return this.genres.add(genre);
    }

    @Override public boolean RemoveGenre(String genre){
        return this.genres.remove(genre);
    }
}
