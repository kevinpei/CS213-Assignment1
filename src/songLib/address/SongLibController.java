//Created by Kevin Pei and Andrew Dos Reis

package songLib.address;

import java.util.*;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import songLib.address.SongLib;
import songLib.address.model.Song;

public class SongLibController {

    @FXML private TableView<Song> songTable;
    @FXML private TableColumn<Song, String> nameColumn;
    @FXML private TextField nameText;
    @FXML private TextField artistText;
    @FXML private TextField albumText;
    @FXML private TextField yearText;
    @FXML private SongLib mainApp = new SongLib();

    public SongLibController() {
    }

    @FXML private void initialize() {
        // Initialize the song table with the name column.
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        songTable.setItems(mainApp.getSongData());

        // Listen for selection changes and show the song details when changed.
        songTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showSongDetails(newValue));
    }

    public void setMainApp(SongLib mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table.
        songTable.setItems(mainApp.getSongData());
    }
    
    private void showSongDetails(Song song) {
    	// If song is null, that means no song is selected.
        if (song != null) {
            // Fill the text fields with info from the selected song object.
            nameText.setText(song.getName());
            artistText.setText(song.getArtist());
            albumText.setText(song.getAlbum());
            yearText.setText(String.format("%d", song.getYear()));
        } else {
            // No song is selected, remove all the text.
            nameText.setText("");
            artistText.setText("");
            albumText.setText("");
            yearText.setText("");
        }
    }

    // This method is called during start. It selects the first song if there are any songs in the list.
    public void selectSong() {
    	if (songTable.getItems().size() != 0) {
    		songTable.getSelectionModel().select(0);
    		songTable.getFocusModel().focus(0);
    		showSongDetails(songTable.getItems().get(0));
    	}
    }
    
    // This method deletes the selected song.
    @FXML private void deleteSong() {
        int selectedIndex = songTable.getSelectionModel().getSelectedIndex();
        // Makes sure that the selected song is within the bounds of the song list.
        if (selectedIndex < 0 || selectedIndex >= songTable.getItems().size()) {
        	// Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Song Selected");
            alert.setContentText("Please select a song in the table.");
            alert.showAndWait();
        }
        // This is the confirmation pop up.
        Alert confirm = new Alert(AlertType.CONFIRMATION);
        confirm.initOwner(mainApp.getPrimaryStage());
        confirm.setTitle("Delete?");
        confirm.setHeaderText("Do you wish to delete the selected song?");
        Optional<ButtonType> result = confirm.showAndWait();
        // If the user hits OK, the following code is executed.
        if (result.get() == ButtonType.OK) {
        	songTable.getItems().remove(selectedIndex);
        	// If the deleted song isn't the last song in the list, then select the next song.
            if (selectedIndex < songTable.getItems().size()) {
        		songTable.getSelectionModel().select(selectedIndex);
        		songTable.getFocusModel().focus(selectedIndex);
        		showSongDetails(songTable.getItems().get(selectedIndex));
            } else {
            	// If the deleted song was the only song in the list, select nothing.
            	if (songTable.getItems().size() == 0) {
            		showSongDetails(null);
            	// If the deleted song was the last song in the list, select the previous song.
            	} else {
            		songTable.getSelectionModel().select(selectedIndex - 1);
            		songTable.getFocusModel().focus(selectedIndex - 1);
            		showSongDetails(songTable.getItems().get(selectedIndex - 1));
            	}
            }
        }
    }

    // A method to add a song via a linear search.
    private static int linearAddition(ObservableList<Song> list, Song s) {
    	// If the list is empty, just add a song.
        if (list.size() == 0) {
            list.add(s);
            return 0;
        }
        int i = 0;
        while (i < list.size()) {
        	/** If the name of the added song matches the name of one in the list, 
        	 * check to see if the artist also matches. If so, it's a duplicate. 
        	 * Otherwise, alphabetize by artist name.
        	 */
            if (s.getName().compareTo(list.get(i).getName()) == 0) {
            	if (s.getArtist().compareTo(list.get(i).getArtist()) == 0) {
            		return -1;
            	} else {
            		if (s.getArtist().compareTo(list.get(i).getArtist()) < 0) {
            			list.add(i, s);
                        return i;
            		}
            	}
            }
            // Add the song in the position before where there is a song that comes later alphabetically.
            if (s.getName().compareTo(list.get(i).getName()) < 0) {
                list.add(i, s);
                return i;
            }
            i++;
        }
        // If the song's name is later alphabetically than all other song names, add it to the end.
        list.add(s);
        return list.size() - 1;
    }

    // A method to test whether a string is a decimal integer.
    public static boolean isInteger(String s) {
    	//If the first character is a negative sign or a digit, continue.
    	if (s.charAt(0) == '-' || Character.isDigit(s.charAt(0))) {
    		for(int i = 1; i < s.length(); i++) {
    			/** Check to see if every character after the first is a digit.
    			 * If not, then the string is not an integer.
    			 */
                if (Character.isDigit(s.charAt(i)) == false) {
                	return false;
                }
            }
    		// If there were no non-digit characters, then it is an integer.
    		return true;
    	}
    	// If the first character is not a negative sign or a digit, then it is not an integer.
        return false;
    }
    
    //This method displays an error if a song with the same artist and name already exists.
    @FXML private void additionError() {
    	Alert alert = new Alert(AlertType.WARNING);
        alert.initOwner(mainApp.getPrimaryStage());
        alert.setTitle("Invalid Song");
        alert.setHeaderText("Same song is already present");
        alert.setContentText("A song with this title and artist already exists.");

        alert.showAndWait();
    }
    
    private void songCheck(int check) {
    	if (check != -1) {
			songTable.getSelectionModel().select(check);
			songTable.getFocusModel().focus(check);
			showSongDetails(songTable.getItems().get(check));
		} else {
			additionError();
		}
    }
    
    @FXML private void addSong() {
        if (isInputValid()) {
            String name = nameText.getText();
            String artist = artistText.getText();
            String album = albumText.getText();
            if (yearText.getText().length() != 0) {
            	if (isInteger(yearText.getText()) == false) {
            		Alert alert = new Alert(AlertType.WARNING);
                    alert.initOwner(mainApp.getPrimaryStage());
                    alert.setTitle("Invalid Year");
                    alert.setHeaderText("Year is not an Integer");
                    alert.setContentText("Please enter an integer for Year or leave it blank.");

                    alert.showAndWait();
            	}
            }
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Add?");
            alert.setHeaderText("Do you wish to add a song with the given characteristics?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (yearText.getText().length() != 0) {
                		int year = Integer.parseInt(yearText.getText());
                		if (album == null) {
                			songCheck(linearAddition(songTable.getItems(), new Song(name, artist, year)));
                		} else {
                			songCheck(linearAddition(songTable.getItems(), new Song(name, artist, album, year)));
                		}
                } else {
                    if (album == null) {
            			songCheck(linearAddition(songTable.getItems(), new Song(name, artist)));
                    } else {
            			songCheck(linearAddition(songTable.getItems(), new Song(name, artist, album)));
                    }
                }
            }
        }
    }

    @FXML private void editSong() {
        int selectedIndex = songTable.getSelectionModel().getSelectedIndex();
        String name = nameText.getText();
        String artist = artistText.getText();
        String album = albumText.getText();
        int year = 0;
        if (yearText.getText().length() != 0) {
        	if (isInteger(yearText.getText()) == true) {
        		year = Integer.parseInt(yearText.getText());
        	} else {
        		Alert alert = new Alert(AlertType.WARNING);
                alert.initOwner(mainApp.getPrimaryStage());
                alert.setTitle("Invalid Year");
                alert.setHeaderText("Year is not an Integer");
                alert.setContentText("Please enter an integer for Year or leave it blank.");

                alert.showAndWait();
                return;
        	}
        }
        if (selectedIndex >= 0){
            if(isInputValid()) {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.initOwner(mainApp.getPrimaryStage());
                alert.setTitle("Edit?");
                alert.setHeaderText("Do you wish to edit the selected song?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    if (yearText.getText().length() != 0) {
                    	if (album == null) {
                    		songTable.getItems().remove(selectedIndex);
                			songCheck(linearAddition(songTable.getItems(), new Song(name, artist, year)));
                    	} else {
                    		songTable.getItems().remove(selectedIndex);
                			songCheck(linearAddition(songTable.getItems(), new Song(name, artist, album, year)));
                    	}
                    } else {
                        if (album == null) {
                            songTable.getItems().remove(selectedIndex);
                			songCheck(linearAddition(songTable.getItems(), new Song(name, artist)));
                        } else {
                            songTable.getItems().remove(selectedIndex);
                			songCheck(linearAddition(songTable.getItems(), new Song(name, artist, album)));
                        }
                    }
                }
            }
        }
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (nameText.getText() == null || nameText.getText().length() == 0) {
            errorMessage += "No valid name!\n";
        }
        if (artistText.getText() == null || artistText.getText().length() == 0) {
            errorMessage += "No valid artist!\n";
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please address the following issues:");
            alert.setContentText(errorMessage);

            alert.showAndWait();
            return false;
        }
    }
    
    @FXML public ObservableList<Song> returnSongList(){
        return songTable.getItems();
    }

    public static void main(String[] args) {

    }

}
