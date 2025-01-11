package org.example.Modules;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Interfaces.IController;
import org.example.Interfaces.IRecord;
import org.javatuples.Triplet;
import org.json.JSONObject;

import java.io.*;
import java.util.*;


public class Menu implements IController {
    public ArrayList<String> albums;

    public Album activeAlbum = null;
    public String activeFullName = null;
    public Single activeTrack = null;
    public String activeSingleFullName = null;
    static String pathname = "src/main/resources/";

    public Menu(){
        albums = new ArrayList<>();
        JSONObject jsonObject;
        try {
            File myObj = new File(Menu.pathname + "source.json");
            if (!myObj.createNewFile()) {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, List<String>> jsonMap = objectMapper.readValue(
                        new File(Menu.pathname + "source.json"),
                        Map.class
                );

                // Extract the "albums" list from the Map
                List<String> albumsList = jsonMap.get("albums");

                // Convert the list to an ArrayList
                this.albums = new ArrayList<>(albumsList);

            }
            else {
                System.out.println("File created!");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<String> GetAlbumList(){
        return this.albums;
    }

    public void Reload(){
        ObjectMapper objectMapper = new ObjectMapper();

        try{
            Map<String, Object> albumsWrapper = new HashMap<>();
            albumsWrapper.put("albums", this.albums);
            objectMapper.writeValue(new File(Menu.pathname + "source.json"), albumsWrapper);
            String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(albumsWrapper);
            System.out.println(jsonString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean CreateAlbum(String name, String artist) {
        if(this.albums.contains(name + "-" + artist))
            return false;
        if(this.activeAlbum != null){
            System.out.println("Save current before creating a new album to manage? 1 (YES) / 0 (NO)");
            Scanner in = new Scanner(System.in);
            int option = in.nextInt();
            switch (option){
                case 0:{
                    this.activeAlbum = new Album(name, artist);
                    this.activeFullName = name + "-" + artist;

                    return true;
                }
                case 1:{
                    SaveAlbum();
                    this.activeAlbum = new Album(name, artist);
                    this.activeFullName = name + "-" + artist;
                    return true;
                }
                default:{
                    System.out.println("Provided value non-binary.. Not proceeding with the creation process..");
                    return false;
                }
            }
        }

        this.activeAlbum = new Album(name, artist);
        this.activeFullName = name + "-" + artist;
        return true;
    }

    @Override
    public boolean SetReleaseDate(String date){
        System.out.println("Date of incorect format.. Must be [\\d\\d:\\d\\d:\\d\\d\\d\\d]");
        return activeAlbum.SetReleaseDate(date);
    }

    @Override
    public boolean LoadAlbum(String name, String artist) {
        if(!albums.contains(name + "-" + artist))
            return false;
        File myFile = new File(pathname + name + "-" + artist + ".json");
        try {
            if(!myFile.exists())
                return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> jsonMap = objectMapper.readValue(myFile, Map.class);

            Album parsedAlbum = new Album(name, artist);

            String parsedName = (String) jsonMap.get("name");
            parsedAlbum.SetName(parsedName);

            double parsedPrice = (Double) jsonMap.get("price");
            parsedAlbum.SetPrice(parsedPrice);

            String parsedRelease = (String) jsonMap.get("release_date");
            parsedAlbum.SetReleaseDate(parsedRelease);

            //String parsedLength = (String) jsonMap.get("length");
            //String[] vals = parsedLength.split(":");
            //int h = Integer.parseInt(vals[0]);
            //int m = Integer.parseInt(vals[1]);
            //int s = Integer.parseInt(vals[2]);
            //parsedAlbum.SetLength(h, m, s);

            ArrayList<Single> parsedTracklsit = new ArrayList<>();

            Map<String, Object> singleDetails = (Map<String, Object>) jsonMap.get("tracklist");
            for(String key : singleDetails.keySet()){
                Map<String, Object> trackDetail = (Map<String, Object>) singleDetails.get(key);

                Single parsedSingle = new Single(key);

                String parsedSingleName = (String) trackDetail.get("name");
                parsedSingle.SetName(parsedSingleName);

                double parsedSinglePrice = (Double) trackDetail.get("price");
                parsedSingle.SetPrice(parsedSinglePrice);

                String parsedSingleRelease = (String) trackDetail.get("release_date");
                parsedSingle.SetReleaseDate(parsedSingleRelease);

                String parsedSingleLength = (String) trackDetail.get("length");
                String[] vals2 = parsedSingleLength.split(":");
                int sh = Integer.parseInt(vals2[0]);
                int sm = Integer.parseInt(vals2[1]);
                int ss = Integer.parseInt(vals2[2]);
                parsedSingle.SetLength(sh, sm, ss);

                Set<String> performerSet = (Set<String>) trackDetail.get("performers");
                parsedSingle.SetArtists(performerSet, IRecord.ContributionType.Performer);

                Set<String> writerSet = (Set<String>) trackDetail.get("writers");
                parsedSingle.SetArtists(writerSet, IRecord.ContributionType.Writer);

                Set<String> producerSet = (Set<String>) trackDetail.get("producers");
                parsedSingle.SetArtists(producerSet, IRecord.ContributionType.Producer);

                parsedTracklsit.add(parsedSingle);
            }
            parsedAlbum.SetTracklist(parsedTracklsit);

            Set<String> composerSet = (Set<String>) jsonMap.get("composers");
            parsedAlbum.SetArtists(composerSet, IRecord.ContributionType.Composer);

            activeAlbum = parsedAlbum;
            activeFullName = name + "-" + artist;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }



        return true;
    }

    @Override
    public boolean SaveAlbum(){
        if(activeAlbum == null)
            return false;

        File myFile = new File(pathname + activeFullName + ".json");

        try{
            if(!myFile.createNewFile()){
                myFile.delete();
                myFile.createNewFile();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try{
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(myFile, activeAlbum.MapObject());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(!albums.contains(activeFullName))
            albums.add(activeFullName);

        Reload();

        return true;
    }

    @Override
    public boolean RemoveAlbum(String name, String artist) {
        String fullname = name + "-" + artist;
        if(!albums.contains(fullname))
            return false;

        File myFile = new File(Menu.pathname + fullname + ".json");
        myFile.delete();
        albums.remove(fullname);

        if(fullname.equals(activeFullName)){
            activeFullName = null;
            activeSingleFullName = null;
            activeAlbum = null;
            activeTrack = null;
        }

        Reload();

        return true;
    }

    @Override
    public boolean RemoveTrack(String trackName){
        if (activeAlbum == null)
            return false;

        return activeAlbum.RemoveTrack(trackName);
    }

    @Override
    public String GetInfo(String name, String artist) {
        if(!this.albums.contains(name + "-" + artist))
            return null;
        try{
            System.out.println(activeFullName);
            File albumData = new File(Menu.pathname + name + "-" + artist + ".json");
            albumData.createNewFile();

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> jsonMap = objectMapper.readValue(albumData, Map.class);

            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonMap);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean AddTrack(String name){
        if (activeAlbum == null)
            return  false;

        Single toAdd = new Single(name);

        return activeAlbum.AddTrack(toAdd);
    }

    @Override
    public boolean AddGenreToActiveAlbum(String genre){
        return activeAlbum.AddGenre(genre);
    }

    @Override
    public boolean ViewTrackInfo(String name){
        for(Single track : activeAlbum.GetTracklist()){
            if(track.GetName() == name){
                System.out.println("Name: " + track.GetName());
                System.out.println("Release date: " + track.GetReleaseDate());
                System.out.println("Length: " + track.GetLength());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean ChangeActiveTrackName(String name){
        for(Single track : activeAlbum.GetTracklist())
            if(track.GetName() == name)
                return false;
        return true;
    }

    @Override
    public boolean ChangeDurationToActiveTrack(int h, int m, int s){
        if(activeTrack == null)
            return false;
        if(!activeTrack.SetLength(h, m, s))
            return false;

        int dH, dM, dS;
        dH = activeTrack.GetLength().getValue0();
        dM = activeTrack.GetLength().getValue1();
        dS = activeTrack.GetLength().getValue2();

        int oldH, oldM, oldS;
        oldH = activeAlbum.GetLength().getValue0();
        oldM = activeAlbum.GetLength().getValue1();
        oldS = activeAlbum.GetLength().getValue2();

        int newH = oldH, newM = oldM, newS = oldS;

        newS = newS - dS + s;
        while (newS < 0){
            newS += 60;
            newM -= 1;
        }
        newM = newM - dM + m;
        while (newM < 0){
            newM += 60;
            newH -= 1;
        }
        newH = newH - dH + h;
        if(newH < 0) {
            activeTrack.SetLength(dH, dM, dS);
            return false;
        }

        activeAlbum.SetLength(newH, newM, newS);
        return  true;
    }

    @Override
    public boolean AddContributionToActiveTrack(String name, int contributionIdx){
        return switch (contributionIdx) {
            case 1 -> activeTrack.AddArtist(name, IRecord.ContributionType.Performer);
            case 2 -> activeTrack.AddArtist(name, IRecord.ContributionType.Writer);
            case 3 -> activeTrack.AddArtist(name, IRecord.ContributionType.Producer);
            default -> false;
        };
    }

    @Override
    public boolean SelectTrack(String name){
        for(Single track : activeAlbum.GetTracklist())
            if(track.GetName() == name){
                activeTrack = track;
                return true;
            }
        activeTrack = null;
        return true;
    }

    @Override
    public boolean AddGenreToActiveTrack(String genre){
        return activeTrack.AddGenre(genre);
    }

}
