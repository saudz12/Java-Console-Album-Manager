package org.example.Modules;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.javatuples.Triplet;
import org.json.JSONObject;

import java.util.*;

public class Single extends RecordBase {
    private Set<String> performers;
    private Set<String> writers;
    private Set<String> producers;

    public Single(String name){
        super(name);
        this.performers = new HashSet<String>();
        this.writers = new HashSet<String>();
        this.producers = new HashSet<String>();
    }

    public Single(String name, Triplet<Integer, Integer, Integer> length){
        super(name, length);
        this.performers = new HashSet<String>();
        this.writers = new HashSet<String>();
        this.producers = new HashSet<String>();
    }

    public Single(String name, Triplet<Integer, Integer, Integer> length, double price){
        super(name, length, price);
        this.performers = new HashSet<String>();
        this.writers = new HashSet<String>();
        this.producers = new HashSet<String>();
    }

    @Override
    public Map<String, Object> MapObject(){
        Map<String, Object> jsonMap = super.MapObject();

        return jsonMap;
    }

    @Override public boolean SetArtists(Set<String> artists, ContributionType contribution){
        switch (contribution){
            case Producer -> this.producers = artists;
            case Writer -> this.writers = artists;
            case Performer -> this.performers = artists;
            case Composer -> {
                return false;
            }
        }
        return true;
    }

    public Set<String> GetPerformer() {
        return this.performers;
    }

    public Set<String> GetWriter() {
        return this.writers;
    }

    public Set<String> GetProducer() {
        return this.producers;
    }

    @Override public boolean AddArtist(String name, ContributionType contribution){
        return switch (contribution) {
            case Writer -> this.writers.add(name);
            case Producer -> this.producers.add(name);
            case Performer -> this.performers.add(name);
            default -> false;
        };
    }
//
    @Override public boolean RemoveContribution(String name, ContributionType contribution){
        return switch (contribution) {
            case Writer -> this.writers.remove(name);
            case Producer -> this.producers.remove(name);
            case Performer -> this.performers.remove(name);
            default -> false;
        };
    }
//
    @Override public boolean RemoveArtist(String name) {
        boolean ok = false;
        ok |= this.writers.remove(name);
        ok |= this.producers.remove(name);
        ok |= this.performers.remove(name);
        return ok;
    }
}
