/*
 * Springfield College: CISC 280 Object-oriented Programming with Java
 * https://scmoodle2.springfieldcollege.edu/course/view.php?id=32250
 * @author Ruth Kurniawati (rkurniawati@springfield.edu) 
 * (c) 2017
 * Created: Mar 26, 2017 3:50:58 PM 
 */

package fpearthquakedatafactory;

import java.util.Collection;
import java.util.Date;
import java.util.ArrayList;

/**
 * Class EarthquakeDataFactorySimulated
 * @author Ruth Kurniawati (rkurniawati@springfield.edu)
 */
public class EarthquakeDataFactorySimulated extends EarthquakeDataFactory {

  private final static int NUM_DATA = 5;
  @Override
  public ArrayList<EarthquakeData> getEarthquakeData(String xmlUrl) {
    ArrayList<EarthquakeData> data = new ArrayList<EarthquakeData>();
    // double magnitude, String location, Date quakeTime, Date updateTime, double latitude, double longitude, double depthMeters
    data.add(new EarthquakeData(4.5, "Big island, HI", "fake link",  new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), 50, 20, -100));
    data.add(new EarthquakeData(1.5, "Winipeg, Canada", "fake link", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), 50, 20, -200));
    data.add(new EarthquakeData(3.5, "Seattle, WA", "fake link", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), 50, 20, -300));
    data.add(new EarthquakeData(2.5, "Billings, Montana", "fake link", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), 50, 20, -400));
    data.add(new EarthquakeData(7.5, "Washington, DC", "fake link", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), 50, 20, -500));
    return data;
  }

}
