import java.util.*;

public class SongLib {

	public void addSong(ArrayList<Song> songlist, Song newSong) {
		int i = 0;
		if (songlist.size() == 0) {
			songlist.add(newSong);
			return;
		}
		while (newSong.name.compareToIgnoreCase(songlist.get(i).name) > 0) {
			i++;
		}
		if (newSong.name.compareToIgnoreCase(songlist.get(i).name) == 0 && 
			newSong.artist.compareToIgnoreCase(songlist.get(i).artist) == 0) {
			return;
		}
		songlist.add(i, newSong);
		return;
	}
	
	public void removeSong(ArrayList<Song> songlist, Song target) {
		int i = 0;
		if (songlist.size() == 0) {
			return;
		}
		while (songlist.get(i).name.compareToIgnoreCase(target.name) > 0) {
			i++;
		}
		if (songlist.get(i).name.compareToIgnoreCase(target.name) == 0) {
			songlist.remove(i);
		}
		return;
	}
	
	public static void main(String[] args) {
		ArrayList<Song> songlist = new ArrayList<Song>();
		
	}

}
