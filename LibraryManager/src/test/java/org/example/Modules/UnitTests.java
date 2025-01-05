package org.example.Modules;

import org.example.Interfaces.IRecord;
import org.javatuples.Triplet;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class UnitTests {

    ///RECORD BASE
    @Test void RecordNameTests(){
        //init, get, set
        RecordBase s = new Single("test");
        assertEquals("test", s.GetName());
        s.SetName("test2");
        assertEquals("test2", s.GetName());

        RecordBase a = new Album("test2", "test3");
        assertEquals("test2", a.GetName());
        a.SetName("test4");
        assertEquals("test4", a.GetName());
    }

    @Test void RecordPriceTest(){
        RecordBase s = new Single("test");
        assertTrue(s.SetPrice(12.123));
        assertEquals(s.GetPrice(), 12.123);
        assertFalse(s.SetPrice(-0.1));

        RecordBase a = new Album("test2", "test3");
        assertTrue(a.SetPrice(12.123));
        assertEquals(a.GetPrice(), 12.123);
        assertFalse(a.SetPrice(-0.1));
    }

    @Test void RecordLengthTest(){
        Single s = new Single("test");
        assertTrue(s.SetLength(1, 3, 45));
        assertTrue(s.GetLength().getValue0() == 1 && s.GetLength().getValue1() == 3 && s.GetLength().getValue2() == 45);
        assertFalse(s.SetLength(9999, 2, -1));

        Album a = new Album("test2", "test3");
        Triplet<Integer, Integer, Integer> duration = new Triplet<>(0, 0, 0);
        a.AddTrack(s);
        a.AddTrack(s);
        a.AddTrack(s);
        for(var track: a.GetTracklist()){
            duration.setAt0(duration.getValue0() + track.GetLength().getValue0());
            duration.setAt1(duration.getValue0() + track.GetLength().getValue1());
            duration.setAt2(duration.getValue0() + track.GetLength().getValue2());
        }
        assertTrue(duration.getValue0() == a.GetLength().getValue0() && duration.getValue1() == a.GetLength().getValue1() && duration.getValue2() == a.GetLength().getValue2());

    }

    @Test void RecordReleaseTest(){
        RecordBase s = new Single("test");
        assertTrue(s.SetReleaseDate("27/12/2024"));
        assertTrue(s.GetReleaseDate() == "27/12/2024");
        assertFalse(s.SetReleaseDate("1/124-a"));

        Album a = new Album("test2", "test3");
        assertTrue(a.SetReleaseDate("27/12/2024"));
        assertTrue(a.GetReleaseDate() == "27/12/2024");
        assertFalse(a.SetReleaseDate("1/124-a"));
    }

    @Test void RecordGenreTest(){
        RecordBase s = new Single("test");
        Set<String> g = new HashSet<String>();
        g.add("rap");
        g.add("rap2");
        g.add("rock");
        s.SetGenres(g);
        for(var genre : s.GetGenres()){
            assertTrue(g.contains(genre));
        }
        for(var genre : g){
            assertTrue(s.GetGenres().contains(genre));
        }
        assertTrue(s.AddGenre("asdf"));
        assertTrue(s.RemoveGenre("rap2"));

        Album a = new Album("test2", "test3");
        Set<String> f = new HashSet<String>();
        f.add("rap");
        f.add("rap2");
        f.add("rock");
        s.SetGenres(f);
        for(var genre : s.GetGenres()){
            assertTrue(f.contains(genre));
        }
        for(var genre : f){
            assertTrue(s.GetGenres().contains(genre));
        }
        assertTrue(s.AddGenre("asdf"));
        assertTrue(s.RemoveGenre("rap2"));
    }

    ///SINGLE SPECIFIC
    ///
    @Test void SingleAddCollaboratorTest(){
        Single s = new Single("test");

        assertTrue(s.GetPerformer().isEmpty());
        assertTrue(s.GetWriter().isEmpty());
        assertTrue(s.GetProducer().isEmpty());

        assertFalse(s.AddArtist("artist0", IRecord.ContributionType.Composer));
        assertTrue(s.AddArtist("artist1", IRecord.ContributionType.Performer));
        assertTrue(s.AddArtist("artist2", IRecord.ContributionType.Writer));
        assertTrue(s.AddArtist("artist3", IRecord.ContributionType.Producer));
        assertTrue(s.AddArtist("artist4", IRecord.ContributionType.Performer));
        assertTrue(s.AddArtist("artist5", IRecord.ContributionType.Writer));
        assertTrue(s.AddArtist("artist6", IRecord.ContributionType.Producer));

        assertTrue(s.GetPerformer().contains("artist1") && s.GetPerformer().contains("artist4"));
        assertTrue(s.GetWriter().contains("artist2") && s.GetWriter().contains("artist5"));
        assertTrue(s.GetProducer().contains("artist3") && s.GetProducer().contains("artist6"));
        assertTrue(s.GetProducer().size() == 2 && s.GetWriter().size() == 2 && s.GetPerformer().size() == 2);

        assertFalse(s.AddArtist("artistProst", IRecord.ContributionType.Composer));
    }

    @Test void SingleRemoveCollaboratorTest(){
        Single s = new Single("test");
        s.AddArtist("artist1", IRecord.ContributionType.Performer);
        s.AddArtist("artist2", IRecord.ContributionType.Performer);
        s.AddArtist("artist1", IRecord.ContributionType.Writer);
        s.AddArtist("artist3", IRecord.ContributionType.Writer);
        s.AddArtist("artist1", IRecord.ContributionType.Producer);
        s.AddArtist("artist4", IRecord.ContributionType.Producer);

        assertFalse(s.RemoveContribution("artist5", IRecord.ContributionType.Performer));
        assertTrue(s.RemoveContribution("artist2", IRecord.ContributionType.Performer));
        assertFalse(s.RemoveContribution("artist5", IRecord.ContributionType.Writer));
        assertTrue(s.RemoveContribution("artist3", IRecord.ContributionType.Writer));
        assertFalse(s.RemoveContribution("artist5", IRecord.ContributionType.Producer));
        assertTrue(s.RemoveContribution("artist4", IRecord.ContributionType.Producer));

        assertFalse(s.GetPerformer().contains("artist2"));
        assertFalse(s.GetWriter().contains("artist3"));
        assertFalse(s.GetProducer().contains("artist4"));

        assertFalse(s.RemoveArtist("artist5"));
        assertTrue(s.RemoveArtist("artist1"));

        assertFalse(s.GetPerformer().contains("artist1"));
        assertFalse(s.GetWriter().contains("artist1"));
        assertFalse(s.GetProducer().contains("artist1"));
    }

    ///ALBUM SPECIFIC

    @Test void AlbumAddCollaboratorTest(){
        Album a = new Album("album1", "artist1");
        assertEquals("album1", a.GetName());
        Set<String> artists = new HashSet<String>();
        artists.add("artist2");
        artists.add("artist3");
        artists.add("artist4");
        a.SetArtists(artists, IRecord.ContributionType.Composer);
        assertFalse(a.GetComposers().contains("artist1"));
        assertFalse(a.AddArtist("artist1", IRecord.ContributionType.Performer));
        assertTrue(a.AddArtist("artist1", IRecord.ContributionType.Composer));
    }

    @Test void AlbumAddSingleTest(){
        Album a = new Album("album1", "artist1");

        a.AddTrack(new Single("track1", new Triplet<>(1, 3, 34)));
        a.AddTrack(new Single("track3", new Triplet<>(0, 4, 23)));
        a.AddTrack(new Single("track2", new Triplet<>(1, 12, 4)), 2);

        assertEquals(a.GetTracklist().get(0).GetName(), "track1");
        assertEquals(a.GetTracklist().get(1).GetName(), "track2");
        assertEquals(a.GetTracklist().get(2).GetName(), "track3");

        int nr = a.GetNrOfTracks();
        assertFalse(a.RemoveTrack("track4"));
        a.RemoveTrack("track1");
        for(var track : a.GetTracklist())
            assertNotEquals(track.GetName(), "track1");
        assertEquals(a.GetNrOfTracks(), nr-1);
    }

    ///CONTROLLER SPECIFIC
}