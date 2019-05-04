package com.company;

import com.company.model.Artist;
import com.company.model.DataSource;
import com.company.model.SongArtist;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static Scanner scan = new Scanner(System.in);
    private static DataSource dataSource = new DataSource();

    public static void main(String[] args) {

        if(!dataSource.open())
        {
            System.out.println("Can't open data source");
            return;
        }
        // creates a view from tables songs and artists, for easier use of queries
        dataSource.createViewForSongArtists();

        int userChoice;
        boolean quit = false;
        do{
            printMenu();

            System.out.println("\nEnter option number: ");
            userChoice = scan.nextInt();
            switch (userChoice){
                case 1:
                    printArtists();
                    break;
                case 2:
                    searchAlbumByArtist(); // e.g. Pink Floyd
                    break;
                case 3:
                    searchArtistBySong();// e.g. Heartless
                    break;
                case 4:
                    searchDatabaseForSong(); // e.g. Stairway To Heaven
                    break;
                case 5:
                    getSongCount();
                    break;
                case 6:
                    insertSong();
                    break;
                case 7:
                    dataSource.querySongsMetadata();
                    break;
                case 8:
                    quit = true;
                    break;

            }
        } while (!quit);

        dataSource.close();
    }

    public static void printArtists(){
        List<Artist> artists = dataSource.queryArtists(DataSource.ORDER_BY_ASCENDING);

        if(artists == null)
        {
            System.out.println("No artists available");
        } else {
            for(Artist artist : artists){
                System.out.println(artist.getId() + " - " + artist.getName());
            }
        }
    }

    public static void searchAlbumByArtist(){

        scan.nextLine();
        System.out.println("Enter artist to find corresponding albums: ");
        String artist = scan.nextLine();
        List<String> albumsForArtists = dataSource.queryAlbumsForArtists(artist, DataSource.ORDER_BY_ASCENDING);

        if(albumsForArtists == null){
            System.out.println("No albums for given artist");
        } else {
            for (String albumName : albumsForArtists) {
                System.out.println(albumName);
            }
        }
    }

    public static void searchArtistBySong(){
        scan.nextLine();
        System.out.println("Enter song to find artist: ");
        String song = scan.nextLine();
        List<SongArtist> songArtists = dataSource.queryArtistForSong(song, DataSource.ORDER_BY_ASCENDING);

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
    }

    public static void getSongCount(){
        int count = dataSource.getCount(DataSource.TABLE_SONGS);
        System.out.println("\nNumber of songs is " + count);
    }

    public static void searchDatabaseForSong(){
        scan.nextLine();
        System.out.println("Enter song title: ");
        String title = scan.nextLine();

        List<SongArtist> songArtists = dataSource.querySongInfoView(title);
        if(songArtists.isEmpty()){
            System.out.println("No such song in database");
        } else {
            for(SongArtist artist : songArtists) {
                System.out.println("Artist = " + artist.getArtistName() +
                        "\nAlbum = " + artist.getAlbumName() +
                        "\nTrack = " + artist.getTrack());
            }
        }
    }

    public static void insertSong(){
        System.out.println("Enter song title: ");
        String title = scan.nextLine();
        System.out.println("Enter song album name: ");
        String album = scan.nextLine();
        System.out.println("Enter artist: ");
        String artist = scan.nextLine();
        System.out.println("Enter track number: ");
        int trackNum = scan.nextInt();
        dataSource.insertSong(title, album, artist, trackNum);
    }

    public static void printMenu(){
        System.out.println("1. Print all artists\n"+
                            "2. Search for album by given artist\n" +
                            "3. Search for artist by song name\n" +
                            "4. Search for song in database\n" +
                            "5. Print total number of songs\n" +
                            "6. Insert song into database\n" +
                            "7. Show column name and id for songs table\n" +
                            "8. Exit\n");
    }
}
