package org.example.Modules;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Interfaces.IController;
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

    public boolean RemoveAlbum(String name, String artist) {
        String fullname = name + "-" + artist;
        if(!albums.contains(fullname))
            return false;

        File myFile = new File(Menu.pathname + fullname + ".json");
        myFile.delete();

        if(fullname.equals(activeFullName)){
            activeFullName = null;
            activeAlbum = null;
            activeTrack = null;
        }

        return true;
    }

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

    public boolean AddTrack(String name){
        if (activeAlbum == null)
            return  false;

        activeTrack = new Single(name);

        activeAlbum.AddTrack(activeTrack);

        return true;
    }

    public boolean ChangeDurationToActiveTrack(int h, int m, int s){
        if(activeTrack == null)
            return false;

    }
}
