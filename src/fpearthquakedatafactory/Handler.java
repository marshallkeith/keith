/*
 * Springfield College: CISC 280 Object-oriented Programming with Java
 * https://scmoodle2.springfieldcollege.edu/course/view.php?id=32250
 * @author Ruth Kurniawati (rkurniawati@springfield.edu) 
 * (c) 2017
 * Created: Mar 26, 2017 4:15:51 PM 
 */
package fpearthquakedatafactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.DefaultHandler2;

/**
 * Class Hander, sax handler for earthquake data from Atom file.
 *
 * @author Ruth Kurniawati (rkurniawati@springfield.edu)
 */
public class Handler extends DefaultHandler2 {

  private Locator locator;
  private ArrayList<EarthquakeData> result;
  private EarthquakeData currentData;
  private String currentAttribute;
  private SimpleDateFormat isoDateParser;
  private boolean getSummaryCDATA;

  private static final String EARTHQUAKE_ENTRY = "entry";
  private static final String EARTHQUAKE_ENTRY_ID = "id"; // urn:earthquake-usgs-gov:ci:37828544
  private static final String EARTHQUAKE_ENTRY_TITLE = "title";
  private static final String EARTHQUAKE_ENTRY_UPDATED = "updated";
  private static final String EARTHQUAKE_ENTRY_LINK = "link";
  private static final String EARTHQUAKE_ENTRY_SUMMARY = "summary";
  private static final String EARTHQUAKE_ENTRY_LATLONG = "georss:point";
  private static final String EARTHQUAKE_ENTRY_ELEVATION = "georss:elev";

  Handler() {
    result = new ArrayList<EarthquakeData>();
    isoDateParser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
    getSummaryCDATA = false;
  }

  ArrayList<EarthquakeData> getResult()
  {
    return result;
  }

  boolean setQuakeTime(String value)
  {
    String startPattern="<dt>Time</dt><dd>";
    String endPattern="</dd>";
    if (value.indexOf(startPattern)< 0) 
      return false; 
    
    String earthquakeTimeStr = value.substring(value.indexOf(startPattern)+startPattern.length());
    earthquakeTimeStr = earthquakeTimeStr.substring(0, earthquakeTimeStr.indexOf(endPattern));
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
    try {
      Date earthquakeTime;
      earthquakeTime = df.parse(earthquakeTimeStr);
      currentData.setQuakeTime(new SimpleObjectProperty<Date>(earthquakeTime));
    } catch (Exception ex) {
      System.err.println(ex.toString());
    }
    return true;
  }
  
  @Override
  public void characters(char[] ch, int start, int length) {
    String value = new String(ch, start, length);
    if (currentData != null) {
      if (getSummaryCDATA) {
        getSummaryCDATA = false;
        setQuakeTime(value);
      } else if (currentAttribute != null) {
        if (currentAttribute.equals(EARTHQUAKE_ENTRY_TITLE)) {
          int index = value.indexOf(" - ");
          String magStr = value.substring(2, index);
          if (magStr.indexOf(" ") > 0)
          {
            magStr = magStr.substring(0, magStr.indexOf(" "));
          }
          String location = value.substring(index+3);
          currentData.setLocation(new SimpleStringProperty(location));
          currentData.setMagnitude(new SimpleDoubleProperty(Double.parseDouble(magStr)));
        } else if (currentAttribute.equals(EARTHQUAKE_ENTRY_LINK)) {
          currentData.setLink(new SimpleStringProperty(value));
        } else if (currentAttribute.equals(EARTHQUAKE_ENTRY_UPDATED)) {
          try
          {
            currentData.setUpdateTime(new SimpleObjectProperty<Date>(isoDateParser.parse(value)));
          } catch (ParseException ex) {
            System.err.println(ex.toString());
          }
        } else if (currentAttribute.equals(EARTHQUAKE_ENTRY_SUMMARY)) {
          // need to parse summary data for earthquake time
          //System.out.println("++++Summary: " + value);
          if (!setQuakeTime(value))
            getSummaryCDATA=true;
        } else if (currentAttribute.equals(EARTHQUAKE_ENTRY_LATLONG)) {
          String[] latlong = value.split(" ", 2);
          double latitude = Double.parseDouble(latlong[0]);
          double longitude = Double.parseDouble(latlong[1]);
          currentData.setLatitude(new SimpleDoubleProperty(latitude));
          currentData.setLongitude(new SimpleDoubleProperty(longitude));
        } else if (currentAttribute.equals(EARTHQUAKE_ENTRY_ELEVATION)) {
          double elev = Double.parseDouble(value);
          currentData.setDepthMeters(new SimpleDoubleProperty(elev));;
        }
      }
      currentAttribute = null;
    } // currentData != null
  }

  @Override
  public void comment(char[] ch, int start, int length) {
    /* 
      System.out.print("characters() [");
      for (int i = start; i < start + length; i++)
         System.out.print(ch[i]);
      System.out.println("]");
     */
  }

  @Override
  public void endCDATA() {
    //System.out.println("endCDATA()");
  }

  @Override
  public void endDocument() {
    //System.out.println("endDocument()");
  }

  @Override
  public void endDTD() {
    //System.out.println("endDTD()");
  }

  @Override
  public void endElement(String uri, String localName, String qName) {
    if (qName.equals(EARTHQUAKE_ENTRY)) {
      // end of entry, add data to array and set currentData to null
      assert currentData != null;
      result.add(currentData);
      currentData = null;
    }
    currentAttribute = null;
    /*
      System.out.print("endElement() ");
      System.out.print("uri=[" + uri + "], ");
      System.out.print("localName=[" + localName + "], ");
      System.out.println("qName=[" + qName + "]"); */
  }

  @Override
  public void endEntity(String name) {
    /* System.out.print("endEntity() ");
      System.out.println("name=[" + name + "]"); */
  }

  @Override
  public void endPrefixMapping(String prefix) {
    //System.out.print("endPrefixMapping() ");
    //System.out.println("prefix=[" + prefix + "]");
  }

  @Override
  public void error(SAXParseException saxpe) {
    //System.out.println("error() " + saxpe);
  }

  @Override
  public void fatalError(SAXParseException saxpe) {
    //System.out.println("fatalError() " + saxpe);
  }

  @Override
  public void ignorableWhitespace(char[] ch, int start, int length) {
    /*
      System.out.print("ignorableWhitespace() [");
      for (int i = start; i < start + length; i++)
         System.out.print(ch[i]);
      System.out.println("]"); */
  }

  @Override
  public void notationDecl(String name, String publicId, String systemId) {
    /*
      System.out.print("notationDecl() ");
      System.out.print("name=[" + name + "]");
      System.out.print("publicId=[" + publicId + "]");
      System.out.println("systemId=[" + systemId + "]"); */
  }

  @Override
  public void processingInstruction(String target, String data) {
    /*
      System.out.print("processingInstruction() [");
      System.out.println("target=[" + target + "]");
      System.out.println("data=[" + data + "]"); */
  }

  @Override
  public InputSource resolveEntity(String publicId, String systemId) {
    /* 
      System.out.print("resolveEntity() ");
      System.out.print("publicId=[" + publicId + "]");
      System.out.println("systemId=[" + systemId + "]"); */
    // Do not perform a remapping.
    InputSource is = new InputSource();
    is.setPublicId(publicId);
    is.setSystemId(systemId);
    return is;
  }

  @Override
  public void setDocumentLocator(Locator locator) {
    /* System.out.print("setDocumentLocator() ");
      System.out.println("locator=[" + locator + "]"); */
    this.locator = locator;
  }

  @Override
  public void skippedEntity(String name) {
    //System.out.print("skippedEntity() ");
    //System.out.println("name=[" + name + "]");
  }

  @Override
  public void startCDATA() {
    //System.out.println("startCDATA()");
  }

  @Override
  public void startDocument() {
    //System.out.println("startDocument()");
  }

  @Override
  public void startDTD(String name, String publicId, String systemId) {
    /*System.out.print("startDTD() ");
      System.out.print("name=[" + name + "]");
      System.out.print("publicId=[" + publicId + "]");
      System.out.println("systemId=[" + systemId + "]");*/
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) {

    currentAttribute = qName;
    if (qName.equals(EARTHQUAKE_ENTRY)) {
      assert currentData == null;
      currentData = new EarthquakeData();
    }
    /*
    System.out.print("startElement() ");
    System.out.print("uri=[" + uri + "], ");
    System.out.print("localName=[" + localName + "], ");
    System.out.println("qName=[" + qName + "]");
    for (int i = 0; i < attributes.getLength(); i++) {
      System.out.println("  Attribute: " + attributes.getLocalName(i)
              + ", " + attributes.getValue(i));
    }
    System.out.println("Column number=[" + locator.getColumnNumber()
            + "]");
    System.out.println("Line number=[" + locator.getLineNumber() + "]");*/
  }

  @Override
  public void startEntity(String name) {
    /* System.out.print("startEntity() ");
      System.out.println("name=[" + name + "]"); */
  }

  @Override
  public void startPrefixMapping(String prefix, String uri) {
    /* System.out.print("startPrefixMapping() ");
      System.out.print("prefix=[" + prefix + "]");
      System.out.println("uri=[" + uri + "]"); */
  }

  @Override
  public void unparsedEntityDecl(String name, String publicId,
          String systemId, String notationName) {
    /* System.out.print("unparsedEntityDecl() ");
      System.out.print("name=[" + name + "]");
      System.out.print("publicId=[" + publicId + "]");
      System.out.print("systemId=[" + systemId + "]");
      System.out.println("notationName=[" + notationName + "]"); */
  }

  @Override
  public void warning(SAXParseException saxpe) {
    //System.out.println("warning() " + saxpe);
  }
}
