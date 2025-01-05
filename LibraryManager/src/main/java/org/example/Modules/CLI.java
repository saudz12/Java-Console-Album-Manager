package org.example.Modules;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Interfaces.IView;
import org.javatuples.Triplet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class CLI implements IView {
    Menu menu;

    Scanner in;

    String current_name = null;
    String current_artist = null;

    public CLI(){
        in = new Scanner(System.in);
        menu = new Menu();
    }

    @Override
    public void Flush(){
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    }

    @Override
    public void PrintMenu(){
        System.out.println("Menu:");
        System.out.println("0. Exit");
        System.out.println("1. View Album List");
        System.out.println("2. Add Album");
        System.out.println("3. Edit current");
        System.out.println("4. Save current");
        System.out.println("5. Load Album");
        System.out.println("6. View Album");
        System.out.println("7. Remove Album");
        if(menu.activeFullName != null)
            System.out.println("Current album: " + menu.activeFullName);
    }

    @Override
    public void Run(){
        while (true){
            PrintMenu();
            int option = in.nextInt();
            switch (option){
                case 0:{
                    Flush();
                    //exit
                    System.out.println("Saving, please wait...");
                    return;
                }
                case 1:{
                    Flush();
                    PrintAllNames();
                    //view album list
                    break;
                }
                case 2:{
                    Flush();
                    //add album
                    AddAlbum();
                    break;
                }
                case 3:{
                    Flush();
                    EditCurrentAlbum();
                    break;
                }
                case 4:{
                    Flush();
                    SaveAlbum();
                    break;
                }
                case 5:{
                    Flush();
                    LoadAlbum();
                    break;
                }
                case 6:{
                    Flush();
                    ViewAlbum();
                    //remove album
                    break;
                }
                case 7:{
                    Flush();
                    RemoveAlbum();
                    break;
                }
                case 8:{
                    Flush();
                    ObjectMapper objectMapper = new ObjectMapper();

                    Album s = new Album("Ye", "Kanye West");
                    s.AddTrack(new Single("I thought about killing you", new Triplet<>(0, 4, 12), 0.99));
                    s.AddTrack(new Single("Yikes", new Triplet<>(0, 2, 43), 0.99));
                    s.AddTrack(new Single("All Mine", new Triplet<>(0, 3, 2), 0.99));

                    try {
                        String jsonString = objectMapper.writeValueAsString(s.MapObject());
                        System.out.println(jsonString);
                        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(s.MapObject()));
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                default:{
                    System.out.println("Err: Invalid Option..");
                    break;
                }
            }
        }
    }

    @Override
    public void PrintAllNames() {
        System.out.println("Available albums: ");
        for(String album : this.menu.GetAlbumList()){
            var nameartist = album.split("-");
            System.out.println(nameartist[0] + " by " + nameartist[1] + ", ");
        }
        System.out.print("\b\b");
        System.out.println("\n\n");
    }

    public void AddAlbum(){
        System.out.println("State a name and an artist:");
        String name = in.next(), artist = in.next();
        menu.CreateAlbum(name, artist);
        current_name = name;
        current_artist = artist;
    }

    public void SaveAlbum(){
        if(menu.SaveAlbum())
            System.out.println("Album saved successfully");
    }

    public void LoadAlbum(){
        System.out.println("State a name and an artist:");
        String name = in.next(), artist = in.next();
        if(menu.LoadAlbum(name, artist))
            System.out.println("Album loaded successfully");
        else
            System.out.println("Album not found..");
    }

    @Override
    public void ViewAlbum(){
        System.out.println("State a name and an artist");
        String name = in.next();
        String artist = in.next();
        String result = menu.GetInfo(name, artist);

        if(result == null)
            System.out.println("Album not found..");
        else
            System.out.println(result);
    }

    public void RemoveAlbum(){
        System.out.println("State a name and an artist");
        String name = in.next();
        String artist = in.next();
        if(!menu.RemoveAlbum(name, artist))
            System.out.println("Album not found..");
        else {
            current_name = null;
            current_artist = null;
        }
    }

    @Override
    public void PrintAlbumOptions() {
        System.out.println("Current album: " + menu.activeFullName);
        System.out.println("0. Exit");
        System.out.println("1. See Album info");
        System.out.println("2. View Tracklist");
        System.out.println("3. Add Track");
        System.out.println("4. Remove Track");
        System.out.println("5. Edit Track");
        System.out.println("6. Change Release Date");
    }

    public void EditCurrentAlbum(){
        if(menu.activeFullName == null){
            System.out.println("No album selected.. Select one by adding(creating a new one) or loading one(importing an existing one)");
            return;
        }
        while (true){
            PrintAllNames();
            PrintAlbumOptions();
            int op = in.nextInt();
            switch (op){
                case 0:
                    return;
                case 1:
                {
                    Flush();
                    System.out.println(menu.GetInfo(current_name, current_artist));
                    break;
                }
                case 2:
                {
                    Flush();
                    ViewTracklist();
                    break;
                }
                case 3:
                {
                    Flush();
                    AddTrack();
                    break;
                }
                case 4:
                {
                    Flush();
                    RemoveTrack();
                    break;
                }
                case 5:
                {
                    Flush();
                    EditTrack();
                    break;
                }
                case 6:
                {
                    Flush();
                    ChangeReleaseDate();
                    break;
                }
                default:{
                    Flush();
                }
                System.out.println("");

            }
        }
    }

    public void ViewTracklist(){
        var tl = menu.activeAlbum.GetTracklist();
        System.out.println("Current Tracklist: " + menu.activeAlbum.GetNrOfTracks() + "tracks, runtime of " + menu.activeAlbum.GetLength());
        for(int i = 0; i < tl.size(); i++){
            System.out.println(i + ". " + tl.get(i).GetName());
        }
    }

    public void AddTrack(){
        String name = in.next();
        menu.AddTrack(name);
    }

    public void RemoveTrack(){
        String trackName = in.next();
        if(menu.RemoveTrack(trackName))
        {
            System.out.println("Track " + trackName + " removed successfully. New tracklist: ");
            ViewTracklist();
        }
        else
            System.out.println("Track not found..");
    }

    public void EditTrack(){
        while (true){
            int option = in.nextInt();
            switch (option){
                case 0:
                    return;
                case 1:{
                    Flush();

                }
                case 2:{
                    Flush();

                }
                case 3:{
                    Flush();

                }
                default:{
                    Flush();
                }
                System.out.println("");
            }
        }
    }

    public void ChangeReleaseDate(){
        String releaseDate = in.next();
        if(menu.activeAlbum.SetReleaseDate(releaseDate))
            System.out.println("Release date successfully change to " + releaseDate);
        else
            System.out.println("new release date does not match format [\\d\\d][\\d\\d][\\d\\d\\d\\d]");
    }


    @Override
    public void PrintTrackOptions() {
        System.out.println("Current Song: " + menu.activeSingleFullName);
        System.out.println("0. Exit");
        System.out.println("1. Change Name");
        System.out.println("2. Change Duration");
        System.out.println("3. Change Release Date");
    }

    public void ChangeName(){
        String name = in.next();
        menu.activeTrack.SetName(name);
    }

    public void ChangeDuration(){
        int h = in.nextInt(), m = in.nextInt(), s = in.nextInt();
        if(menu.ChangeDurationToActiveTrack(h, m, s))
        {
            System.out.println("Release date successfully change to " + menu.activeTrack.LengthToString());
            System.out.println(" " + menu.activeTrack.LengthToString());
        }
        else
            System.out.println("Couldn't change track's duration to " + menu.activeTrack.LengthToString());

    }

}
