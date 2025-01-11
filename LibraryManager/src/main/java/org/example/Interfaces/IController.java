package org.example.Interfaces;

import org.example.Modules.Album;
import org.example.Modules.Single;

import java.util.ArrayList;
import java.util.Map;

public interface IController {
    public ArrayList<String> GetAlbumList();
    public boolean CreateAlbum(String name, String artist);
    public boolean SetReleaseDate(String date);
    String GetInfo(String name, String artist);
    public boolean LoadAlbum(String name, String artist);
    public boolean SaveAlbum();
    public boolean RemoveAlbum(String name, String artist);
    public boolean RemoveTrack(String trackName);
    public boolean AddTrack(String name);
    public boolean ViewTrackInfo(String name);
    public boolean ChangeActiveTrackName(String name);
    public boolean ChangeDurationToActiveTrack(int h, int m, int s);
}
