/*
 * Springfield College: CISC 280 Object-oriented Programming with Java
 * https://scmoodle2.springfieldcollege.edu/course/view.php?id=32250
 * @author Ruth Kurniawati (rkurniawati@springfield.edu) 
 * (c) 2017
 * Created: Apr 9, 2017 9:42:18 PM 
 */

package fpearthquakedatafactory;

import java.util.ArrayList;
import java.util.Date;
import javafx.animation.*;
import javafx.application.*;
import static javafx.application.Application.launch;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.web.*;
import javafx.beans.value.*;
import javafx.scene.control.TableView.*;


/**
 *
 * @author Ruth Kurniawati (rkurniawati@springfield.edu)
 */
public class FPInterface extends Application {
  static final EarthquakeDataFactory factory = new EarthquakeDataFactoryAtom();
  static boolean useLiveData;
  String url;
  
  @Override // Override the start method in the Application class
  public void start(Stage primaryStage) {

    // Create a pane and set its properties
    GridPane gridPane = new GridPane();
    gridPane.setAlignment(Pos.CENTER);
    gridPane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
    //gridPane.setHgap(5);
    //gridPane.setVgap(5);

    Button btShow = new Button("Show");
    gridPane.add(btShow, 2, 1);
    GridPane.setHalignment(btShow, HPos.RIGHT);
    
    // Place nodes in the pane
    gridPane.add(new Label("Find earthquakes in the past"), 0, 0);
    final ComboBox earthQuakeTimeComboBox = new ComboBox();
    earthQuakeTimeComboBox.getItems().addAll(
      "Past Week",
      "Past Hour",
      "Past Day",
      "Past 30 Days"
    );
    earthQuakeTimeComboBox.setValue("Past Week");
        
    gridPane.add(earthQuakeTimeComboBox, 0, 1);
    gridPane.add(new Label("Magnitude"), 1, 0); 
    
    final ComboBox earthQuakeMagnitudeComboBox = new ComboBox();
    earthQuakeMagnitudeComboBox.getItems().addAll(
      "Significant",
      "M4.5+",
      "M2.5+",
      "M1.0+",
      "All"  
    );
    earthQuakeMagnitudeComboBox.setValue("Significant");
    gridPane.add(earthQuakeMagnitudeComboBox, 1, 1);

    // create the table here
    TableView<EarthquakeData> tableView = new TableView<>();
 
    TableColumn magnitudeColumn = new TableColumn("Magnitude");
    magnitudeColumn.setMinWidth(10);
    magnitudeColumn.setCellValueFactory(
      new PropertyValueFactory<EarthquakeData, Double>("magnitude"));
    magnitudeColumn.setStyle( "-fx-alignment: CENTER-RIGHT;");

    TableColumn locationColumn = new TableColumn("Location");
    locationColumn.setMinWidth(200);
    locationColumn.setCellValueFactory(
      new PropertyValueFactory<EarthquakeData, String>("location"));

    TableColumn quakeTimeColumn = new TableColumn("Time");
    quakeTimeColumn.setMinWidth(10);
    quakeTimeColumn.setCellValueFactory(
      new PropertyValueFactory<EarthquakeData, String>("quakeTime"));

    TableColumn latitudeColumn = new TableColumn("Latitude");
    latitudeColumn.setMinWidth(10);
    latitudeColumn.setCellValueFactory(
      new PropertyValueFactory<EarthquakeData, String>("latitude"));
    latitudeColumn.setStyle( "-fx-alignment: CENTER-RIGHT;");

    TableColumn longitudeColumn = new TableColumn("Longitude");
    longitudeColumn.setMinWidth(10);
    longitudeColumn.setCellValueFactory(
      new PropertyValueFactory<EarthquakeData, String>("longitude"));
    longitudeColumn.setStyle( "-fx-alignment: CENTER-RIGHT;");

    TableColumn depthColumn = new TableColumn("Depth (m)");
    depthColumn.setMinWidth(10);
    depthColumn.setCellValueFactory(
      new PropertyValueFactory<EarthquakeData, String>("depthMeters"));
    depthColumn.setStyle( "-fx-alignment: CENTER-RIGHT;");

    tableView.getColumns().addAll(magnitudeColumn, locationColumn, quakeTimeColumn, 
           latitudeColumn, longitudeColumn, depthColumn);

    tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
    @Override
    public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
        //Check whether item is selected and set value of selected item to Label
        if(tableView.getSelectionModel().getSelectedItem() != null) 
        {    
           TableViewSelectionModel selectionModel = tableView.getSelectionModel();
           ObservableList selectedCells = selectionModel.getSelectedCells();
           TablePosition tablePosition = (TablePosition) selectedCells.get(0);
           Object val = tablePosition.getTableColumn().getCellData(newValue);
           System.out.println("Selected Value" + val);
         }
         }
     });

    setFactoryURL(tableView, "Significant", "Past Week");
    // Border pane for gridPane and the table 
    BorderPane pane = new BorderPane();
    pane.setTop(gridPane);
    pane.setCenter(tableView);

    /*
    WebView browser = new WebView();
    WebEngine webEngine = browser.getEngine();
    //webEngine.load("https://earthquake.usgs.gov/earthquakes/eventpage/us10008fsd");
    pane.setRight(browser);
    */
    
    // set the event handler
    btShow.setOnAction((ActionEvent e)->{
      System.out.println(earthQuakeMagnitudeComboBox.getValue());
      System.out.println(earthQuakeTimeComboBox.getValue());
      setFactoryURL(tableView, (String) earthQuakeMagnitudeComboBox.getValue(), 
              (String) earthQuakeTimeComboBox.getValue());
    });
    
    // Create a scene and place it in the stage
    Scene scene = new Scene(pane);
    primaryStage.setTitle("Earthquake data query"); // Set the stage title
    primaryStage.setScene(scene); // Place the scene in the stage
    primaryStage.show(); // Display the stage
  }
  
  /**
   * The main method is only needed for the IDE with limited
   * JavaFX support. Not needed for running from the command line.
   */
  public static void main(String[] args) {
    useLiveData = true;
    if (args.length > 0)
    {
      if (args[0].equals("false")) useLiveData = false;
    }
    
    
    launch(args);
  }

  private void setFactoryURL(TableView tableView, String quakeMagnitude, String quakeTime) {
    // combinations: 
    //"Past Week",
    //    https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/significant_week.atom
    //    https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/4.5_week.atom
    //    https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom
    //    https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/1.0_hour.atom
    //    https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_week.atom
    // "Past Hour",
    //     https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/significant_hour.atom
    //     https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/4.5_hour.atom
    //     https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_hour.atom
    //     https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/1.0_hour.atom
    //     https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_hour.atom
    // "Past Day",
    //     https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/significant_day.atom
    //     https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/4.5_day.atom
    //     https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_day.atom
    //     https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/1.0_day.atom
    //     https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_day.atom
    // "Past 30 Days"
    //     https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/significant_month.atom
    //     https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/4.5_month.atom
    //     https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_month.atom
    //     https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/1.0_month.atom
    //     https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_month.atom
    // x
    //
    // "Significant",
    // "M4.5+",
    // "M2.5+",
    // "M1.0+",
    // "All"  

    url = "data/4.5_week.xml";
    if (useLiveData)
    {  
      // TODO: compute the URL here
      url = "";
    }
    
  }
}

