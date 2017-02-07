
public class Song {
	String name;
	String artist;
	String album;
	int year;
	public Song (String name, String artist) {
		this.name = name;
		this.artist = artist;
	}
	public Song (String name, String artist, String album) {
		this(name, artist);
		this.album = album;
	}
	public Song (String name, String artist, int year) {
		this(name, artist);
		this.year = year;
	}
	public Song (String name, String artist, String album, int year) {
		this(name, artist, album);
		this.year = year;
	}
}
