package com.company;

import com.company.model.Artist;
import com.company.model.DataSource;
import com.company.model.SongArtist;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        DataSource dataSource = new DataSource();
        if(!dataSource.open())
        {
            System.out.println("Can't open data source");
            return;
        }

        List<Artist> artists = dataSource.queryArtists(DataSource.ORDER_BY_ASCENDING);

        if(artists == null)
        {
            System.out.println("No artists available");
        } else {
            for(Artist artist : artists){
                System.out.println(artist.getId() + " - " + artist.getName());
            }
        }

        List<String> albumsForArtists = dataSource.queryAlbumsForArtists("Pink Floyd", DataSource.ORDER_BY_ASCENDING);

        if(albumsForArtists == null){
            System.out.println("No albums for given artist");
        } else {
            for (String albumName : albumsForArtists) {
                System.out.println(albumName);
            }
        }

        List<SongArtist> songArtists = dataSource.queryArtistForSong("Heartless", DataSource.ORDER_BY_ASCENDING);

        if(songArtists == null)
        {
            System.out.println("Couldn't find the artist for the song");
        } else {
            for(SongArtist artist : songArtists)
            {
                System.out.println("Artist name - " + artist.getArtistName() +
                        "\nAlbum name - " + artist.getAlbumName() +
                        "\nTrack - " + artist.getTrack());
            }
        }

        dataSource.querySongsMetadata();

        int count = dataSource.getCount(DataSource.TABLE_SONGS);
        System.out.println("\nNumber of songs is " + count);

        dataSource.createViewForSongArtists();

        System.out.println("Enter song title: ");
        String title = scan.nextLine();

        songArtists = dataSource.querySongInfoView(title);
        if(songArtists.isEmpty()){
            System.out.println("No such song in database");
        } else {
            for(SongArtist artist : songArtists) {
                System.out.println("Artist = " + artist.getArtistName() +
                                    "\nAlbum = " + artist.getAlbumName() +
                                    "\nTrack = " + artist.getTrack());
            }
        }

        dataSource.insertSong("Touch of Grey", "Grateful Dead", "In The Dark", 1);

        dataSource.close();
    }
}
