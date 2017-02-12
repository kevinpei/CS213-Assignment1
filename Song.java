package songLib.address;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.IntegerProperty;

public class Song {
	StringProperty name;
	String Name;
	StringProperty artist;
	String Artist;
	StringProperty album;
	String Album;
	IntegerProperty year;
	int Year;
	public Song (String name, String artist) {
		this.name = new SimpleStringProperty(name);
		this.Name = name;
		this.artist = new SimpleStringProperty(artist);
		this.Artist = artist;
	}
	public Song (String name, String artist, String album) {
		this(name, artist);
		this.album = new SimpleStringProperty(album);
		this.Album = album;
	}
	public Song (String name, String artist, int year) {
		this(name, artist);
		this.year = new SimpleIntegerProperty(year);
		this.Year = year;
	}
	public Song (String name, String artist, String album, int year) {
		this(name, artist, album);
		this.year = new SimpleIntegerProperty(year);
		this.Year = year;
	}
	public String getName() {
		return this.Name;
	}
	public StringProperty getNameProperty() {
		return this.name;
	}
	public String getArtist() {
		return this.Artist;
	}
	public String getAlbum() {
		return this.Album;
	}
	public int getYear() {
		return this.Year;
	}
}
