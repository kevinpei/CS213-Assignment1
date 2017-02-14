package songLib.address;

import java.io.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.collections.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private ObservableList<Song> songs = FXCollections.observableArrayList();

    public MainApp() {
    }

    public ObservableList<Song> getSongData() {
        File file = new File("/Users/adosreis/Documents/CS213-Assignment1/src/songLib/address/songList.xml");
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(file);
            NodeList nList = document.getElementsByTagName("Song");
            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                //System.out.println("\nCurrent Element :" + nNode.getNodeName());
                //TODO Catch all types of listed songs, right now only can get complete information songs!
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                   // eElement.getAttribute("Song");
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


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("SongLib");

        initRootLayout();

        showSongOverview();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the person overview inside the root layout.
     */

    public void showSongOverview() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/SongLib.fxml"));
            AnchorPane songOverview = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(songOverview);

            // Give the controller access to the main app.
            SongLib controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void stop() {
        File file = new File("/Users/adosreis/Documents/CS213-Assignment1/src/songLib/address/songList.xml");
        file.delete();
        ObservableList<Song> exitList = getSongData();
        try {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("List");
        doc.appendChild(rootElement);
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
            System.out.println("File saved!");
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