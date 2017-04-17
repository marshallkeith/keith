/*
 * Springfield College: CISC 280 Object-oriented Programming with Java
 * https://scmoodle2.springfieldcollege.edu/course/view.php?id=32250
 * @author Ruth Kurniawati (rkurniawati@springfield.edu) 
 * (c) 2017
 * Created: Mar 26, 2017 1:37:14 PM 
 */

package fpearthquakedatafactory;

import java.util.ArrayList;

/**
 * Class EarthquakeDataFactory
 * @author Ruth Kurniawati (rkurniawati@springfield.edu)
 */
public abstract class EarthquakeDataFactory {
  
  public abstract ArrayList<EarthquakeData> getEarthquakeData(String xmlUrl);
}
