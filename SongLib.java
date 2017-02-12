package songLib.address;

import java.util.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import songLib.address.MainApp;
import songLib.address.Song;

public class SongLib {
	
	@FXML private TableView<Song> songTable;
    @FXML private TableColumn<Song, String> nameColumn;
    @FXML private TextField nameText;
    @FXML private TextField artistText;
    @FXML private TextField albumText;
    @FXML private TextField yearText;
    @FXML private MainApp mainApp = new MainApp();
    
    public SongLib() {
    }
    
    @FXML private void initialize() {
        // Initialize the person table with the two columns.
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        songTable.setItems(mainApp.getSongData());
        
        showSongDetails(null);

        // Listen for selection changes and show the person details when changed.
        songTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showSongDetails(newValue));
    }
	
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        songTable.setItems(mainApp.getSongData());
    }
    
    private void showSongDetails(Song song) {
        if (song != null) {
            // Fill the labels with info from the person object.
            nameText.setText(song.getName());
            artistText.setText(song.getArtist());
            albumText.setText(song.getAlbum());
            yearText.setText(String.format("%d", song.getYear()));
            

            // TODO: We need a way to convert the birthday into a String! 
            // birthdayLabel.setText(...);
        } else {
            // Person is null, remove all the text.
            nameText.setText("");
            artistText.setText("");
            albumText.setText("");
            yearText.setText("");
        }
    }
    
	public void addSong(ArrayList<Song> songlist, Song newSong) {
		int i = 0;
		if (songlist.size() == 0) {
			songlist.add(newSong);
			return;
		}
/*		while (newSong.name.compareToIgnoreCase(songlist.get(i).name) > 0) {
			i++;
		}
		if (newSong.name.compareToIgnoreCase(songlist.get(i).name) == 0 && 
			newSong.artist.compareToIgnoreCase(songlist.get(i).artist) == 0) {
			return;
		}*/
		songlist.add(i, newSong);
		return;
	}
	
	public void removeSong(ArrayList<Song> songlist, Song target) {
		int i = 0;
		if (songlist.size() == 0) {
			return;
		}/*
		while (songlist.get(i).name.compareToIgnoreCase(target.name) > 0) {
			i++;
		}
		if (songlist.get(i).name.compareToIgnoreCase(target.name) == 0) {
			songlist.remove(i);
		}*/
		return;
	}
	
	public void editName(Song target, String name) {
//		target.name = name;
		return;
	}
	
	public void editArtist(Song target, String artist) {
//		target.artist = artist;
		return;
	}
	
	public void editAlbum(Song target, String album) {
//		target.album = album;
		return;
	}
	
	public void editYear(Song target, int year) {
//		target.year= year;
		return;
	}
	
	public static void main(String[] args) {
		
	}

}
