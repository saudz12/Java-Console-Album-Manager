package org.example.Interfaces;

import org.example.Modules.Album;

import java.util.ArrayList;
import java.util.Map;

public interface IController {
    public ArrayList<String> GetAlbumList();
    public boolean CreateAlbum(String name, String artist);
    public boolean SetReleaseDate(String date);
    public boolean LoadAlbum(String name, String artist);
    public boolean SaveAlbum();
    String GetInfo(String name, String artist);
}
