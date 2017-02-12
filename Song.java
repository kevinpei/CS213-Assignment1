package songLib.address;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.IntegerProperty;

public class Song {
	StringProperty name;
	StringProperty artist;
	StringProperty album;
	IntegerProperty year;
	public Song (String name, String artist) {
		this.name = new SimpleStringProperty(name);
		this.artist = new SimpleStringProperty(artist);
	}
	public Song (String name, String artist, String album) {
		this(name, artist);
		this.album = new SimpleStringProperty(album);
	}
	public Song (String name, String artist, int year) {
		this(name, artist);
		this.year = new SimpleIntegerProperty(year);
	}
	public Song (String name, String artist, String album, int year) {
		this(name, artist, album);
		this.year = new SimpleIntegerProperty(year);
	}
	public StringProperty getName() {
		return this.name;
	}
}
