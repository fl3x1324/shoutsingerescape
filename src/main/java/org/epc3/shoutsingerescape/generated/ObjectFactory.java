//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.09.15 at 03:16:39 AM EEST 
//


package org.epc3.shoutsingerescape.generated;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.epc3.shoutsingerescape.generated package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.epc3.shoutsingerescape.generated
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Song }
     * 
     */
    public Song createSong() {
        return new Song();
    }

    /**
     * Create an instance of {@link Song.Lyrics }
     * 
     */
    public Song.Lyrics createSongLyrics() {
        return new Song.Lyrics();
    }

    /**
     * Create an instance of {@link Song.Properties }
     * 
     */
    public Song.Properties createSongProperties() {
        return new Song.Properties();
    }

    /**
     * Create an instance of {@link Song.Lyrics.Verse }
     * 
     */
    public Song.Lyrics.Verse createSongLyricsVerse() {
        return new Song.Lyrics.Verse();
    }

    /**
     * Create an instance of {@link Song.Properties.Titles }
     * 
     */
    public Song.Properties.Titles createSongPropertiesTitles() {
        return new Song.Properties.Titles();
    }

    /**
     * Create an instance of {@link Song.Properties.Authors }
     * 
     */
    public Song.Properties.Authors createSongPropertiesAuthors() {
        return new Song.Properties.Authors();
    }

}