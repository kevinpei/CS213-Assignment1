package songLib.address;

import java.util.*;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
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

    @FXML private void deleteSong() {
        int selectedIndex = songTable.getSelectionModel().getSelectedIndex();
        Alert confirm = new Alert(AlertType.CONFIRMATION);
        confirm.initOwner(mainApp.getPrimaryStage());
        confirm.setTitle("Delete?");
        confirm.setHeaderText("Do you wish to delete the selected song?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.get() == ButtonType.OK) {
            if (selectedIndex >= 0) {
                songTable.getItems().remove(selectedIndex);
                if (selectedIndex < songTable.getItems().size()) {
        			songTable.getSelectionModel().select(selectedIndex);
        			songTable.getFocusModel().focus(selectedIndex);
        			showSongDetails(songTable.getItems().get(selectedIndex));
                } else {
                	if (songTable.getItems().size() == 0) {
            			showSongDetails(null);
                	} else {
                		songTable.getSelectionModel().select(selectedIndex - 1);
            			songTable.getFocusModel().focus(selectedIndex - 1);
            			showSongDetails(songTable.getItems().get(selectedIndex - 1));
                	}
                }
            } else {
                // Nothing selected.
                Alert alert = new Alert(AlertType.WARNING);
                alert.initOwner(mainApp.getPrimaryStage());
                alert.setTitle("No Selection");
                alert.setHeaderText("No Song Selected");
                alert.setContentText("Please select a song in the table.");

                alert.showAndWait();
            }
        }
    }

    private static int linearAddition(ObservableList<Song> list, Song s) {
        if (list.size() == 0) {
            list.add(s);
            return 0;
        }
        int i = 0;
        while (i < list.size()) {
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
            if (s.getName().compareTo(list.get(i).getName()) < 0) {
                list.add(i, s);
                return i;
            }
            i++;
        }
        list.add(s);
        return list.size() - 1;
    }

    public static boolean isInteger(String s) {
    	if (s.charAt(0) == '-' || Character.isDigit(s.charAt(0))) {
    		for(int i = 1; i < s.length(); i++) {
                if (Character.isDigit(s.charAt(i)) == false) {
                	return false;
                }
            }
    		return true;
    	}
        return false;
    }
    
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
