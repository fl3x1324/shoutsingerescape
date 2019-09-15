package org.epc3.shoutsingerescape;

import com.healthmarketscience.jackcess.DatabaseBuilder;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import javax.swing.text.BadLocationException;
import javax.swing.text.rtf.RTFEditorKit;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import org.epc3.shoutsingerescape.generated.ObjectFactory;
import org.epc3.shoutsingerescape.generated.Song;
import org.epc3.shoutsingerescape.util.XmlUtil;

public class ShoutSingerEscape {

    public static void main(String[] args) throws BadLocationException {
        try {
            var fileName = args[0];
            var inputFile = new File(fileName);
            var db = DatabaseBuilder.open(inputFile);
            var table = db.getTable("Songs");
            var pattern = Pattern.compile("<Text>([\\s\\S]*?)</Text>[\\s\\S]*?");
            var openSongObjectFactory = new ObjectFactory();
            var songs = new ArrayList<Song>();
            var count = 0;
            var rowCount = table.getRowCount();
            var startMilis = System.currentTimeMillis();
            for (var row : table) {
                var tenth = rowCount / 10;
                if (count % tenth == 0) {
                    System.out.println(String.format("Parsing %d/%d...", count, rowCount));
                }
                var title = row.get("Title");
                if (title.toString().toLowerCase().startsWith("проповед")
                    || title.toString().toLowerCase().startsWith("молитва")) {
                    continue;
                }
                var song = openSongObjectFactory.createSong();
                var cue = row.get("Cue").toString();
                var matcher = pattern.matcher(cue);
                var songTitle = row.get("Title").toString();
                var songAuthor = "God";
                song.setCreatedIn("ShoutSingerEscape");
                song.setModifiedIn("ShoutSingerEscape");
                song.setModifiedDate(DatatypeFactory.newInstance()
                    .newXMLGregorianCalendar(Instant.now().toString()));
                song.setVersion(0.8f);
                song.setProperties(openSongObjectFactory.createSongProperties());
                song.getProperties()
                    .setAuthors(openSongObjectFactory.createSongPropertiesAuthors());
                song.getProperties().getAuthors().setAuthor(songAuthor);
                song.getProperties().setTitles(openSongObjectFactory.createSongPropertiesTitles());
                song.getProperties().getTitles().setTitle(songTitle);
                song.setLyrics(openSongObjectFactory.createSongLyrics());
                var sequence = 1;
                var verses = new LinkedHashMap<String, Song.Lyrics.Verse>();
                while (matcher.find()) {
                    var element = matcher.group(1);
                    var input = new ByteArrayInputStream(element.getBytes());
                    var rtfEditor = new RTFEditorKit();
                    var document = rtfEditor.createDefaultDocument();
                    rtfEditor.read(input, document, 0);
                    var text = document.getText(0, document.getLength()).replace("&amp;apos;", "'");
                    var verse = openSongObjectFactory.createSongLyricsVerse();
                    verse.setLines(openSongObjectFactory.createSongLyricsVerseLines());
                    var textLines = text.strip().split("\\s+");
                    Stream.of(textLines).forEach(line -> {
                        verse.getLines().getContent().add(line.strip());
                    });
                    verse.setName("v" + sequence++);
                    verses.put(verse.getName(), verse);
                }
                var playOrder = verses.keySet();
                var verseList = new ArrayList<>(verses.values());
                song.getLyrics().getVerse().addAll(verseList);
                song.getProperties().setVerseOrder(String.join(" ", playOrder));
                songs.add(song);
                count++;
            }
            var stopMilis = System.currentTimeMillis();
            System.out.println(
                String.format("Parsing all %d songs took %d ms.", count, stopMilis - startMilis));
            startMilis = System.currentTimeMillis();
            var xmlUtil = new XmlUtil();
            songs.forEach(song -> {
                try {
                    var xml = xmlUtil.convertToXml(song);
                    var outputXml = new File(String.format("/Users/nasko/openlp_import/%s.xml",
                        song.getProperties().getTitles().getTitle().replace("/", "")));
                    var outputXmlWriter = new FileWriter(outputXml);
                    outputXmlWriter.append(xml);
                    outputXmlWriter.flush();
                    outputXmlWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            stopMilis = System.currentTimeMillis();
            System.out.println(String.format("Exporting %d xml files to disk took %dms.", count,
                stopMilis - startMilis));
        } catch (IOException | DatatypeConfigurationException e) {
            e.printStackTrace();
        }
    }
}
