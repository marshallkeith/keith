/*
 * Springfield College: CISC 280 Object-oriented Programming with Java
 * https://scmoodle2.springfieldcollege.edu/course/view.php?id=32250
 * @author Ruth Kurniawati (rkurniawati@springfield.edu) 
 * (c) 2017
 * Created: Mar 26, 2017 1:36:17 PM 
 */

package fpearthquakedatafactory;

import java.time.LocalDate;
import java.util.Date;
import javafx.beans.property.*;

/**
 * Class EarthquakeData
 * @author Ruth Kurniawati (rkurniawati@springfield.edu)
 */
public class EarthquakeData implements  Comparable<EarthquakeData>,  Cloneable {
  /**
   * M scale
   */
  private SimpleDoubleProperty magnitude;
  
  /**
   * Earthquake location description
   */
  private SimpleStringProperty location; 

  /**
   * URL to a web page that contains more information
   */
  private SimpleStringProperty link;
  
  /**
   * When earthquake happened (UTC)
   */
  private ObjectProperty<Date> quakeTime;
  
  /**
   * When data was last updated (UTC)
   */
  private ObjectProperty<Date> updateTime;
  
  /**
   * Earthquake location's latitude
   */
  private SimpleDoubleProperty latitude;
  
  /**
   * Earthquake location's longitude
   */
  private SimpleDoubleProperty longitude;
  
  /**
   * Earthquake depth in meters
   */
  private SimpleDoubleProperty depthMeters;
  
  /**
   * Default constructor
   */
  public EarthquakeData() {
   
  }

  /**
   * Constructor
   * @param magnitude
   * @param location
   * @param quakeTime
   * @param updateTime
   * @param latitude
   * @param longitude
   * @param depthMeters 
   */
  public EarthquakeData(double magnitude, String location, String link, Date quakeTime, Date updateTime, double latitude, double longitude, double depthMeters) {
    this.magnitude = new SimpleDoubleProperty(magnitude);
    this.location = new SimpleStringProperty(location);
    this.quakeTime = new SimpleObjectProperty<Date>(quakeTime);
    this.updateTime = new SimpleObjectProperty<Date>(updateTime);
    this.latitude = new SimpleDoubleProperty(latitude);
    this.longitude = new SimpleDoubleProperty(longitude);
    this.depthMeters = new SimpleDoubleProperty(depthMeters);
  }

  /**
   * Return string representation of the earthquake data
   * @return 
   */
  @Override
  public String toString() {
    return "EarthquakeData{" + "magnitude=" + magnitude + ", location=" + location + ", quakeTime=" + quakeTime + ", updateTime=" + updateTime + ", latitude=" + latitude + ", longitude=" + longitude + ", depthMeters=" + depthMeters + '}';
  }

  public double getMagnitude() {
    return magnitude.get();
  }

  public void setMagnitude(SimpleDoubleProperty magnitude) {
    this.magnitude = magnitude;
  }

  public String getLocation() {
    return location.get();
  }

  public void setLocation(SimpleStringProperty location) {
    this.location = location;
  }

  public Date getQuakeTime() {
    return quakeTime.get();
  }

  public void setQuakeTime(ObjectProperty<Date> quakeTime) {
    this.quakeTime = quakeTime;
  }

  public Date getUpdateTime() {
    return updateTime.get();
  }

  public SimpleStringProperty getLink() {
    return link;
  }

  public void setLink(SimpleStringProperty link) {
    this.link = link;
  }

  public void setUpdateTime(ObjectProperty<Date> updateTime) {
    this.updateTime = updateTime;
  }

  public double getLatitude() {
    return latitude.get();
  }

  public void setLatitude(SimpleDoubleProperty latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude.get();
  }

  public void setLongitude(SimpleDoubleProperty longitude) {
    this.longitude = longitude;
  }

  public double getDepthMeters() {
    return depthMeters.get();
  }

  public void setDepthMeters(SimpleDoubleProperty depthMeters) {
    this.depthMeters = depthMeters;
  }  
 
  @Override
  public int compareTo(EarthquakeData o) {
    return (this.magnitude.get() - o.getMagnitude()) > 0.0 ? 1 : -1;
  }

  /** Override the protected clone method defined in 
    the Object class, and strengthen its accessibility */
  @Override 
  public Object clone() {
    try {
      EarthquakeData clone = (EarthquakeData) super.clone();
      return clone;
    }
    catch (CloneNotSupportedException ex) {
      return null;
    }

  }

  @Override
  public boolean equals(Object o)
  {
    if (o instanceof EarthquakeData) {
      EarthquakeData otherData = (EarthquakeData) o;
      return otherData.getLocation().equals(this.getLocation()) && otherData.getQuakeTime().equals(this.getQuakeTime());
  }
    return false;
}
}
