/*
 * Springfield College: CISC 280 Object-oriented Programming with Java
 * https://scmoodle2.springfieldcollege.edu/course/view.php?id=32250
 * @author Ruth Kurniawati (rkurniawati@springfield.edu) 
 * (c) 2017
 * Created: Mar 26, 2017 4:05:23 PM 
 */

package fpearthquakedatafactory;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import java.net.URL;
import java.util.Scanner;

/**
 * Class EarthquakeDataFactoryAtom
 * Reads the atom feeds from https://earthquake.usgs.gov/earthquakes/feed/v1.0/atom.php
 * @author Ruth Kurniawati (rkurniawati@springfield.edu)
 */
public class EarthquakeDataFactoryAtom extends EarthquakeDataFactory{
  
  public EarthquakeDataFactoryAtom() {
  }

  
  @Override
  public ArrayList<EarthquakeData> getEarthquakeData(String xmlURL) {
    try {
       XMLReader xmlr = XMLReaderFactory.createXMLReader();
       xmlr.setFeature("http://xml.org/sax/features/namespace-prefixes",
                       true);
       Handler handler = new Handler();
       xmlr.setContentHandler(handler);
       xmlr.setDTDHandler(handler);
       xmlr.setEntityResolver(handler);
       xmlr.setErrorHandler(handler);
       xmlr.setProperty("http://xml.org/sax/properties/lexical-handler", 
                        handler);
       
       InputSource in = null;
       if (xmlURL.startsWith("http:") || xmlURL.startsWith("https:") ) {
         URL url = new URL(xmlURL);
         in = new InputSource(url.openStream());
       } else {
         in= new InputSource(new FileReader(xmlURL));
       }
       
       xmlr.parse(in);
       return handler.getResult();
    }
    catch (IOException ioe)
    {
       System.err.println("IOE: " + ioe);
    }
    catch (SAXException saxe)
    {
       System.err.println("SAXE: " + saxe);
    }

    return null;
  }
  
}
