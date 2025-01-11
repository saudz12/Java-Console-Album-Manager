package org.example.Modules;

import org.example.Interfaces.IRecord;
import org.javatuples.Triplet;
import org.json.JSONObject;

import java.util.*;

public class Album extends RecordBase{
    private int nrOfTracks;
    private ArrayList<Single> tracklist;
    private Set<String> composers;

    public Album(String name, String artist){
        super(name);
        if(artist == "")
            throw new RuntimeException("Can't have empty artist name");
        this.tracklist = new ArrayList<Single>();
        this.composers = new HashSet<String>();
        this.composers.add(artist);
    }

    @Override
    public Map<String, Object> MapObject(){
        Map<String, Object> jsonMap = super.MapObject();
        jsonMap.put("nr_of_tracks", this.nrOfTracks);

        Map<String, Object> trackMap = new HashMap<>();

        for (Single s  : tracklist){
            trackMap.put(s.GetName(), s.MapObject());
        }

        jsonMap.put("tracklist", trackMap);
        jsonMap.put("composers", composers.toArray());

        return jsonMap;
    }

    public int GetNrOfTracks() {
        return this.nrOfTracks;
    }

    @Override public boolean SetArtists(Set<String> artists, ContributionType contribution){
        switch (contribution){
            case Composer -> this.composers = artists;
            default -> {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean AddArtist(String name, ContributionType contribution) {
        switch (contribution){
            case Composer:{
                return this.composers.add(name);
            }
            default:
                return false;
        }
    }
//
    @Override
    public boolean RemoveContribution(String name, ContributionType contribution) {
        boolean ok = false;
        switch (contribution){
            case Composer:{
                return this.composers.remove(name);
            }
            default:
                for(var song : tracklist)
                    ok |= song.RemoveContribution(name, contribution);
        }
        return ok;
    }

    public boolean RemoveComposer(String name){
        return this.composers.remove(name);
    }

    @Override
    public boolean RemoveArtist(String name) {
        boolean ok = false;
        ok |= this.composers.remove(name);
        for(var song : tracklist)
            ok |= song.RemoveArtist(name);
        return false;
    }

    public void SetTracklist(ArrayList<Single> tracklist){
        this.nrOfTracks = tracklist.size();
        this.tracklist = tracklist;

        int h = 0, m = 0, s = 0;

        super.GetGenres().clear();
        for(var track : tracklist){
            super.GetGenres().addAll(track.GetGenres());
            Triplet<Integer,Integer,Integer> trackDuration = track.GetLength();
            s += trackDuration.getValue2();
            m += s / 60 + trackDuration.getValue1();
            h += m / 60 + trackDuration.getValue0();
            m %= 60;
            s %= 60;
        }

        this.SetLength(h, m, s);
    }

    public boolean AddTrack(Single track){
        for(Single s :tracklist)
            if(s.GetName() == track.GetName())
                return false;
        this.tracklist.add(track);
        super.GetGenres().addAll(track.GetGenres());
        this.nrOfTracks++;
        return true;
    }

    public boolean AddTrack(Single track, int pos){
        if(pos < 1)
            return false;
        if(pos == nrOfTracks)
            return AddTrack(track);
        for(Single s :tracklist)
            if(s.GetName() == track.GetName())
                return false;
        this.tracklist.add(pos - 1, track);
        super.GetGenres().addAll(track.GetGenres());
        this.nrOfTracks++;
        return true;
    }

    public Set<String> GetComposers(){
        return this.composers;
    }

    public ArrayList<Single> GetTracklist(){
        return this.tracklist;
    }

    public boolean RemoveTrack(String singleName){
        for(var single : this.tracklist){
            if(single.GetName() == singleName){
                this.tracklist.remove(single);
                this.nrOfTracks--;
                return true;
            }
        }
        return false;
    }
}
