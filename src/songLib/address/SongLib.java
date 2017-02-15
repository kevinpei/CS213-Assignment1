//Created by Kevin Pei and Andrew Dos Reis

package songLib.address;

import java.io.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import songLib.address.model.Song;
import javafx.collections.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class SongLib extends Application {

    private Stage primaryStage;
    private ObservableList<Song> songs = FXCollections.observableArrayList();

    //The SongLib constructor
    public SongLib() {
    }

    /**	
     * This method reads from songList.xml and populates our songs ObservableList with those songs.
     * This is where we load saved song data. 
     */
    public ObservableList<Song> getSongData() {
    	//This is where the save data is found.
        File file = new File("src/songLib/address/songList.xml");
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(file);
            NodeList nList = document.getElementsByTagName("Song");
            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);
                
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                   String title = eElement.getElementsByTagName("Title").item(0).getTextContent();
                   String artist = eElement.getElementsByTagName("Artist").item(0).getTextContent();
                   String album = eElement.getElementsByTagName("Album").item(0).getTextContent();
                   int year;
                   if(eElement.getElementsByTagName("Year").item(0).getTextContent().length() != 0) {
                       year = Integer.parseInt(eElement.getElementsByTagName("Year").item(0).getTextContent());
                   }
                   else{
                       year = 0;
                   }
                   songs.add(new Song(title,artist,album,year));
                }
            }
        }catch (Exception e) {
        e.printStackTrace();
    }
        return songs;
    }

    //This method overrides the default start method.
    @Override public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        //We call the window "Song Library".
        this.primaryStage.setTitle("Song Library");
        showSongOverview();
        //We set the window to not be resizable to avoid formatting errors.
        primaryStage.setResizable(false);
    }

    //This shows the main window.
    public void showSongOverview() {
        try {
            // Load the song library xml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(SongLib.class.getResource("view/SongLib.fxml"));
            AnchorPane songOverview = (AnchorPane) loader.load();
            Scene scene = new Scene(songOverview);
            primaryStage.setScene(scene);
            primaryStage.show();
            // Give the controller access to the main app.
            SongLibController controller = loader.getController();
            controller.setMainApp(this);
            controller.selectSong();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the main stage.
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    //This method overrides the default stop method.
    @Override public void stop() {
    	/**	
         * This method deletes the previous songList.xml file and creates a new one.
         * This is how we save the current song list.
         */
        File file = new File("src/songLib/address/songList.xml");
        file.delete();
        ObservableList<Song> exitList = songs;
        try {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("List");
        doc.appendChild(rootElement);
        //For each song in the list, we create a new line for it.
        for (Song song : exitList) {
            String title = song.getName();
            String artist = song.getArtist();
            String album = song.getAlbum();
            int year = song.getYear();
            String yearStr;
            if (year == 0) {
                yearStr = "";
            } else {
                yearStr = Integer.toString(year);
            }

            Element Song = doc.createElement("Song");
            rootElement.appendChild(Song);
            Element Title = doc.createElement("Title");
            Title.appendChild(doc.createTextNode(title));
            Song.appendChild(Title);
            Element Artist = doc.createElement("Artist");
            Artist.appendChild(doc.createTextNode(artist));
            Song.appendChild(Artist);
            Element Album = doc.createElement("Album");
            Album.appendChild(doc.createTextNode(album));
            Song.appendChild(Album);
            Element Year = doc.createElement("Year");
            Year.appendChild(doc.createTextNode(yearStr));
            Song.appendChild(Year);
        }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(
                    new FileOutputStream(file, false));
            transformer.transform(source, result);
            } catch (ParserConfigurationException pce) {
                pce.printStackTrace();
            } catch (TransformerException tfe) {
                tfe.printStackTrace();
            } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}