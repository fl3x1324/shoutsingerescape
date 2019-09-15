package org.epc3.shoutsingerescape;

import com.healthmarketscience.jackcess.DatabaseBuilder;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;
import javax.swing.text.BadLocationException;
import javax.swing.text.rtf.RTFEditorKit;

import org.epc3.shoutsingerescape.generated.ObjectFactory;

public class ShoutSingerEscape {

	public static void main(String[] args) throws BadLocationException {
		try {
			var fileName = args[0];
			var inputFile = new File(fileName);
			var db = DatabaseBuilder.open(inputFile);
			var table = db.getTable("Songs");
			var pattern = Pattern.compile("<Text>([\\s\\S]*?)</Text>[\\s\\S]*?");
			var startMilis = System.currentTimeMillis();
			for (var row : table) {
				var title = row.get("Title");
				System.out.println(String.format("Displaying song with title: %s", title));
				var cue = row.get("Cue").toString();
				var matcher = pattern.matcher(cue);
				while (matcher.find()) {
					var element = matcher.group(1);
					var input = new ByteArrayInputStream(element.getBytes());
					var editorKit = new RTFEditorKit();
					var document = editorKit.createDefaultDocument();
					editorKit.read(input, document, 0);
					var text = document.getText(0, document.getLength());
					System.out.println(text.strip());
				}
			}
			var stopMilis = System.currentTimeMillis();
			System.out.println(String.format("Listing all %d songs took %d ms.", table.getRowCount(), stopMilis - startMilis));
			var openSongObjectFactory = new ObjectFactory();
			var song = openSongObjectFactory.createSong();
			song.setProperties(openSongObjectFactory.createSongProperties());
			song.getProperties().setTitles(openSongObjectFactory.createSongPropertiesTitles());
			song.getProperties().getTitles().setTitle("Amazing race");
			System.out.println(song.getProperties().getTitles().getTitle());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
