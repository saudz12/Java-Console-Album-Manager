package org.example.Modules;

import org.example.Interfaces.IView;

import java.util.Scanner;

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
        System.out.println("3. Save current");
        System.out.println("4. Load Album");
        System.out.println("5. View Album");
        System.out.println("6. Remove Album");
        if(menu.activeFullName != null)
            System.out.println("Current album: " + menu.activeFullName);
        System.out.println("\nChose an action: ");

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
                    System.out.println("Save changes before exiting? 1 (YES) / 0 (NO)");
                    int save = in.nextInt();
                    switch (save){
                        case 1:
                            System.out.println("Saving, please wait...");
                            SaveAlbum();
                            break;
                        case 0:
                            break;
                        default:
                            System.out.println("Invalid input, the album will not be saved");
                    }
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
                    EditCurrentAlbum();
                    break;
                }
                //case 3:{
                //    Flush();
                //    EditCurrentAlbum();
                //    System.out.println("Saving, please wait..");
                //    SaveAlbum();
                //    break;
                //}
                case 3:{
                    Flush();
                    SaveAlbum();
                    break;
                }
                case 4:{
                    Flush();
                    LoadAlbum();
                    EditCurrentAlbum();
                    break;
                }
                case 5:{
                    Flush();
                    ViewAlbum();
                    //remove album
                    break;
                }
                case 6:{
                    Flush();
                    RemoveAlbum();
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

    @Override
    public void AddAlbum(){
        System.out.println("State an album name and an artist's name:");
        String name = in.next(), artist = in.next();
        menu.CreateAlbum(name, artist);
        current_name = name;
        current_artist = artist;
    }


    @Override
    public void SaveAlbum(){
        if(menu.SaveAlbum())
            System.out.println("Album saved successfully");
        else
            System.out.println("Nothing to save..");
    }

    @Override
    public void LoadAlbum(){
        PrintAllNames();
        System.out.println("State an album name and an artist's name:");
        String name = in.next(), artist = in.next();
        if(menu.LoadAlbum(name, artist))
            System.out.println("Album loaded successfully");
        else
            System.out.println("Album not found..");
    }

    @Override
    public void ViewAlbum(){
        PrintAllNames();
        System.out.println("State an album name and an artist's name");
        String name = in.next();
        String artist = in.next();
        String result = menu.GetInfo(name, artist);

        if(result == null)
            System.out.println("Album not found..");
        else
            System.out.println(result);
    }

    @Override
    public void RemoveAlbum(){
        PrintAllNames();
        System.out.println("State an album name and an artist's name");
        String name = in.next();
        String artist = in.next();
        if(!menu.RemoveAlbum(name, artist))
            System.out.println("Album not found..");
        else {
            System.out.println("Album succesfully removed..");
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

    @Override
    public void EditCurrentAlbum(){
        if(menu.activeFullName == null){
            System.out.println("No album selected.. Select one by adding(creating a new one) or loading one(importing an existing one)");
            return;
        }
        while (true){
            //PrintAllNames();
            System.out.println(menu.activeAlbum.MapObject());
            PrintAlbumOptions();
            int op = in.nextInt();
            switch (op){
                case 0:
                    System.out.println("Save changes before exiting? 1 (YES) / 0 (NO)");
                    int save = in.nextInt();
                    switch (save){
                        case 1:
                            System.out.println("Saving, please wait...");
                            SaveAlbum();
                            break;
                        case 0:
                            break;
                        default:
                            System.out.println("Invalid input, the album will not be saved");
                    }
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
                    System.out.println("Err: Invalid Option..");
                }
            }
        }
    }

    @Override
    public void ViewTracklist(){
        var tl = menu.activeAlbum.GetTracklist();
        System.out.println("Current Tracklist: " + menu.activeAlbum.GetNrOfTracks() + "tracks, runtime of " + menu.activeAlbum.GetLength());
        for(int i = 0; i < tl.size(); i++){
            System.out.println(i + ". " + tl.get(i).GetName());
        }
    }

    @Override
    public void AddTrack(){
        String name = in.next();
        menu.AddTrack(name);
    }

    @Override
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

    @Override
    public void ChangeReleaseDate(){
        String releaseDate = in.next();
        if(menu.activeAlbum.SetReleaseDate(releaseDate))
            System.out.println("Release date successfully change to " + releaseDate);
        else
            System.out.println("new release date does not match format <\\d\\d/\\d\\d/[1-9]\\d\\d\\d>");
    }


    @Override
    public void PrintTrackOptions() {
        System.out.println("Current Song: " + menu.activeSingleFullName);
        System.out.println("0. Exit");
        System.out.println("1. Change Name");
        System.out.println("2. Change Duration");
        System.out.println("3. Change Release Date");
    }

    @Override
    public void EditTrack(){
        while (true){
            System.out.println(menu.activeTrack.MapObject());
            PrintTrackOptions();
            int option = in.nextInt();
            switch (option){
                case 0:
                    return;
                case 1:{
                    Flush();
                    ChangeTrackName();
                }
                case 2:{
                    Flush();
                    ChangeTrackDuration();
                }
                case 3:{
                    Flush();
                    ChangeTrackReleaseDate();
                }
                default:{
                    Flush();
                    System.out.println("Err: Invalid Option..");
                }
            }
        }
    }

    @Override
    public void ChangeTrackName(){
        System.out.println("Name the track: ");
        String name = in.next();
        menu.activeTrack.SetName(name);
    }

    @Override
    public void ChangeTrackDuration(){
        System.out.println("Read length in {hours, minutes, seconds} for the track: ");
        int h = in.nextInt(), m = in.nextInt(), s = in.nextInt();
        if(menu.ChangeDurationToActiveTrack(h, m, s))
        {
            System.out.println("Length change to " + menu.activeTrack.LengthToString());
            System.out.println(" " + menu.activeTrack.LengthToString());
        }
        else
            System.out.println("Couldn't change track's duration to " + menu.activeTrack.LengthToString());

    }

    @Override
    public void ChangeTrackReleaseDate(){
        String date = in.next();
        if(menu.activeTrack.SetReleaseDate(date))
            System.out.println("Release date successfully change to " + date);
        else
            System.out.println(date + " does not match <\\d\\d/\\d\\d/[1-9]\\d\\d\\d> format");

    }
}
