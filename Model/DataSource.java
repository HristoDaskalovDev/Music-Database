package com.company.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataSource {
    public static final String DB_NAME = "music.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:C:\\Users\\Hristo\\Desktop\\JavaProjects\\Tim Buchalka (Databases) Music\\" + DB_NAME;
    public static final String TABLE_ALBUMS = "albums";
    public static final String COLUMN_ALBUM_ID = "_id";
    public static final String COLUMN_ALBUM_NAME = "name";
    public static final String COLUMN_ALBUM_ARTIST = "artist";
    public static final int INDEX_ALBUM_ID = 1;
    public static final int INDEX_ALBUM_NAME = 2;
    public static final int INDEX_ALBUM_ARTIST = 3;

    public static final String TABLE_ARTISTS = "artists";
    public static final String COLUMN_ARTIST_ID = "_id";
    public static final String COLUMN_ARTIST_NAME = "name";
    public static final int INDEX_ARTIST_ID = 1;
    public static final int INDEX_ARTIST_NAME = 2;

    public static final String TABLE_SONGS = "songs";
    public static final String COLUMN_SONG_ID = "_id";
    public static final String COLUMN_SONG_TRACK = "track";
    public static final String COLUMN_SONG_TITLE = "title";
    public static final String COLUMN_SONG_ALBUM = "album";
    public static final int INDEX_SONG_ID = 1;
    public static final int INDEX_SONG_TRACK = 2;
    public static final int INDEX_SONG_TITLE = 3;
    public static final int INDEX_SONG_ALBUM = 4;

    public static final int ORDER_BY_NONE = 1;
    public static final int ORDER_BY_ASCENDING = 2;
    public static final int ORDER_BY_DESCENDING = 3;

    public static final String QUERY_ALBUMS_BY_ARTIST_START = "SELECT " + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + " FROM "
            + TABLE_ALBUMS + " INNER JOIN " + TABLE_ARTISTS + " ON " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ARTIST + " = " + TABLE_ARTISTS +
            "." + COLUMN_ARTIST_ID + " WHERE " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + " = \"";

    public static final String QUERY_ALBUMS_BY_ARTIST_SORT = " ORDER BY " + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + " COLLATE NOCASE ";

    public static final String QUERY_ARTIST_OR_SONG_START = "SELECT " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + ", " + TABLE_ALBUMS +
            "." + COLUMN_ALBUM_NAME + ", " + TABLE_SONGS + "." + COLUMN_SONG_TRACK + " FROM " + TABLE_SONGS + " INNER JOIN " + TABLE_ALBUMS +
            " ON " + TABLE_SONGS + "." + COLUMN_SONG_ALBUM + " = " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ID + " INNER JOIN " + TABLE_ARTISTS +
            " ON " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ARTIST + " = " + TABLE_ARTISTS + "." + COLUMN_ARTIST_ID + " WHERE " + TABLE_SONGS +
            "." + COLUMN_SONG_TITLE + " = \"";

    public static final String QUERY_ARTIST_FOR_SONG_SORT = " ORDER BY " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + ", " + TABLE_ALBUMS +
            "." + COLUMN_ALBUM_NAME + " COLLATE NOCASE ";

    public static final String TABLE_ARTIST_SONG_VIEW = "artist_list";
    public static final String CREATE_ARTIST_FOR_SONG_VIEW = "CREATE VIEW IF NOT EXISTS " + TABLE_ARTIST_SONG_VIEW + " AS SELECT " + TABLE_ARTISTS +
            "." + COLUMN_ARTIST_NAME + ", " + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + " AS " + COLUMN_SONG_ALBUM + ", " + TABLE_SONGS + "." +
            COLUMN_SONG_TRACK + ", " + TABLE_SONGS + "." + COLUMN_SONG_TITLE + " FROM " + TABLE_SONGS + " INNER JOIN " + TABLE_ALBUMS + " ON " +
            TABLE_SONGS + "." + COLUMN_SONG_ALBUM + " = " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ID + " INNER JOIN " + TABLE_ARTISTS + " ON " +
            TABLE_ALBUMS + "." + COLUMN_ALBUM_ARTIST + " = " + TABLE_ARTISTS + "." + COLUMN_ARTIST_ID + " ORDER BY " + TABLE_ARTISTS + "." +
            COLUMN_ARTIST_NAME + ", " + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + ", " + TABLE_SONGS + "." + COLUMN_SONG_TRACK;

    public static final String QUERY_VIEW_SONG_INFO = "SELECT " + COLUMN_ARTIST_NAME + ", " + COLUMN_SONG_ALBUM + ", " + COLUMN_SONG_TRACK + " FROM " +
            TABLE_ARTIST_SONG_VIEW + " WHERE " + COLUMN_SONG_TITLE + " = \"";

    public static final String QUERY_VIEW_SONG_INFO_PREP = "SELECT " + COLUMN_ARTIST_NAME + ", " + COLUMN_SONG_ALBUM + ", " + COLUMN_SONG_TRACK + " FROM " +
            TABLE_ARTIST_SONG_VIEW + " WHERE " + COLUMN_SONG_TITLE + " = ?";

    public static final String INSERT_ARTIST = "INSERT INTO " + TABLE_ARTISTS + "(" + COLUMN_ARTIST_NAME + ") VALUES (?)";

    public static final String INSERT_ALBUM = "INSERT INTO " + TABLE_ALBUMS + "(" + COLUMN_ALBUM_NAME + ", " + COLUMN_ALBUM_ARTIST + ") VALUES (?, ?)";

    public static final String INSERT_SONG = "INSERT INTO " + TABLE_SONGS + "(" + COLUMN_SONG_TRACK + ", " + COLUMN_SONG_TITLE + ", " + COLUMN_SONG_ALBUM +
            ") VALUES (?, ?, ?)";

    public static final String QUERY_ARTIST = "SELECT " + COLUMN_ARTIST_ID + " FROM " + TABLE_ARTISTS + " WHERE " + COLUMN_ARTIST_NAME + " = ?";

    public static final String QUERY_ALBUM = "SELECT " + COLUMN_ALBUM_ID + " FROM " + TABLE_ALBUMS + " WHERE " + COLUMN_ALBUM_NAME + " = ?";



    private PreparedStatement querySongInfoView;
    private PreparedStatement insertIntoArtists;
    private PreparedStatement insertIntoAlbums;
    private PreparedStatement insertIntoSongs;

    private PreparedStatement queryArtist;
    private PreparedStatement queryAlbum;

    private Connection connection;

    public boolean open() {
        try {
            connection = DriverManager.getConnection(CONNECTION_STRING);
            querySongInfoView = connection.prepareStatement(QUERY_VIEW_SONG_INFO_PREP);
            insertIntoArtists = connection.prepareStatement(INSERT_ARTIST, Statement.RETURN_GENERATED_KEYS);
            insertIntoAlbums = connection.prepareStatement(INSERT_ALBUM, Statement.RETURN_GENERATED_KEYS);
            insertIntoSongs = connection.prepareStatement(INSERT_SONG);
            queryArtist = connection.prepareStatement(QUERY_ARTIST);
            queryAlbum = connection.prepareStatement(QUERY_ALBUM);
            return true;
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
            return false;
        }
    }

    public void close() {
        try {
            if(querySongInfoView != null){
                querySongInfoView.close();
            }
            if(insertIntoArtists != null) {
                insertIntoArtists.close();
            }
            if(insertIntoAlbums != null){
                insertIntoAlbums.close();
            }
            if(insertIntoSongs != null){
                insertIntoSongs.close();
            }
            if(queryArtist != null){
                queryArtist.close();
            }
            if(queryAlbum != null){
                queryAlbum.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }
    }

    public List<Artist> queryArtists(int sortOrder) {

        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(TABLE_ARTISTS);
        if (sortOrder != ORDER_BY_NONE) {
            sb.append(" ORDER BY ");
            sb.append(COLUMN_ARTIST_NAME);
            sb.append(" COLLATE NOCASE ");
            if (sortOrder == ORDER_BY_DESCENDING) {
                sb.append("DESC");
            } else {
                sb.append("ASC");
            }
        }

        try (Statement statement = connection.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())) {

            List<Artist> artists = new ArrayList<>();

            while (results.next()) {
                Artist artist = new Artist();
                artist.setId(results.getInt(INDEX_ARTIST_ID));
                artist.setName(results.getString(INDEX_ARTIST_NAME));
                artists.add(artist);
            }
            return artists;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List<String> queryAlbumsForArtists(String artistName, int sortOrder) {
        StringBuilder sb = new StringBuilder(QUERY_ALBUMS_BY_ARTIST_START);
        sb.append(artistName);
        sb.append("\"");

        if (sortOrder != ORDER_BY_NONE) {
            sb.append(QUERY_ALBUMS_BY_ARTIST_SORT);

            if (sortOrder == ORDER_BY_DESCENDING) {
                sb.append("DESC");
            } else {
                sb.append("ASC");
            }
        }
        System.out.println("SQL statement -> " + sb.toString());

        try (Statement statement = connection.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())) {

            List<String> albums = new ArrayList<>();

            while (results.next()) {
                albums.add(results.getString(1));
            }
            return albums;

        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
            return null;
        }
    }

    public List<SongArtist> queryArtistForSong(String songName, int sortOrder) {
        StringBuilder sb = new StringBuilder(QUERY_ARTIST_OR_SONG_START);
        sb.append(songName);
        sb.append("\"");

        if (sortOrder != ORDER_BY_NONE) {
            sb.append(QUERY_ARTIST_FOR_SONG_SORT);
            if (sortOrder == ORDER_BY_DESCENDING) {
                sb.append("DESC");
            } else {
                sb.append("ASC");
            }
        }
        System.out.println("SQL statement " + sb.toString());

        try (Statement statement = connection.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())) {

            List<SongArtist> songArtists = new ArrayList<>();

            while (results.next()) {
                SongArtist songArtist = new SongArtist();
                songArtist.setArtistName(results.getString(1));
                songArtist.setAlbumName(results.getString(2));
                songArtist.setTrack(results.getInt(3));

                songArtists.add(songArtist);
            }
            return songArtists;

        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
            return null;
        }

    }

    public void querySongsMetadata() {
        String sql = "SELECT * FROM " + TABLE_SONGS;

        try (Statement statement = connection.createStatement();
             ResultSet results = statement.executeQuery(sql)) {

            ResultSetMetaData meta = results.getMetaData();
            int numColumns = meta.getColumnCount();

            for (int i = 1; i <= numColumns; i++) {
                System.out.format("Column %d in the songs table is named %s\n", i, meta.getColumnName(i));
            }
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }
    }

    public int getCount(String table) {
        String sql = "SELECT COUNT(*) AS count FROM " + table;
        try (Statement statement = connection.createStatement();
             ResultSet results = statement.executeQuery(sql)) {

            int count = results.getInt("count");

            return count;
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
            return -1;
        }
    }

    public boolean createViewForSongArtists() {
        try (Statement statement = connection.createStatement()) {

            statement.execute(CREATE_ARTIST_FOR_SONG_VIEW);
            return true;

        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
            System.out.println("Create view failed - " + sqlex.getMessage());
            return false;
        }
    }

    public List<SongArtist> querySongInfoView(String title) {
        try {
            querySongInfoView.setString(1, title);
            ResultSet results = querySongInfoView.executeQuery();

            List<SongArtist> songArtists = new ArrayList<>();
            while (results.next()) {
                SongArtist artist = new SongArtist();
                artist.setArtistName(results.getString(1));
                artist.setAlbumName(results.getString(2));
                artist.setTrack(results.getInt(3));
                songArtists.add(artist);
            }
            return songArtists;

        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
            return null;
        }
    }

    private int insertArtist(String name) throws SQLException
    {
        queryArtist.setString(1, name);
        ResultSet results = queryArtist.executeQuery();
        if(results.next()){
            return results.getInt(1);
        } else {
            insertIntoArtists.setString(1, name);
            int affectedRows = insertIntoArtists.executeUpdate();
            if(affectedRows != 1){
                throw new SQLException("Couldn't insert artist");
            }
            ResultSet generatedKeys = insertIntoArtists.getGeneratedKeys();
            if(generatedKeys.next()){
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Couldn't get _id for artist");
            }
        }
    }


    private int insertAlbum(String name, int artistId) throws SQLException
    {
        queryAlbum.setString(1, name);
        ResultSet results = queryAlbum.executeQuery();
        if(results.next()){
            return results.getInt(1);
        } else {
            insertIntoAlbums.setString(1, name);
            insertIntoAlbums.setInt(2, artistId);
            int affectedRows = insertIntoAlbums.executeUpdate();
            if(affectedRows != 1){
                throw new SQLException("Couldn't insert artist");
            }
            ResultSet generatedKeys = insertIntoAlbums.getGeneratedKeys();
            if(generatedKeys.next()){
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Couldn't get _id for artist");
            }
        }
    }

    public void insertSong(String title, String album, String artist, int track)
    {
        try{
            connection.setAutoCommit(false);

            int artistId = insertArtist(artist);
            int albumId = insertAlbum(album, artistId);
            insertIntoSongs.setInt(1, track);
            insertIntoSongs.setString(2, title);
            insertIntoSongs.setInt(3, albumId);

            int affectedRows = insertIntoSongs.executeUpdate();
            if(affectedRows == 1){
                connection.commit();
            } else {
                throw new SQLException("Song commit failed");
            }
        } catch (Exception ex) {
            System.out.println("Insert song exception" + ex.getMessage()); // if index is out of bounds we do not want to commit artist and album without committing a song
            try{
                System.out.println("Performing rollback");
                connection.rollback();
            } catch (SQLException sqlex2) {
                System.out.println("Could not perform rollback" + sqlex2.getMessage());
            }
        } finally {
            try{
                System.out.println("Resetting default commit behaviour");
                connection.setAutoCommit(true);
            } catch (SQLException sqlex) {
                System.out.println("Couldn't reset autocommit" + sqlex.getMessage());
            }
        }

    }
}
