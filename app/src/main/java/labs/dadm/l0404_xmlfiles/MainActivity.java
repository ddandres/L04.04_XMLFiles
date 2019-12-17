/*
 * Copyright (c) 2019. David de Andrés and Juan Carlos Ruiz, DISCA - UPV, Development of apps for mobile devices.
 */

package labs.dadm.l0404_xmlfiles;

import android.os.Bundle;
import android.util.Xml;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    // Constants XML Tags
    private static final String EMAIL = "Email";
    private static final String FROM = "From";
    private static final String NAME = "Name";
    private static final String TO = "To";
    private static final String SUBJECT = "Subject";
    private static final String BODY = "Body";

    // Constant for file name
    private static final String FILENAME = "xml_file_internal_storage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the XML file in the application internal storage if it does not exist
        File file = getFileStreamPath("xml_file_internal_storage");
        if (!file.exists()) {
            createXmlFile();
        }

        // Read the XML file from the application internal storage
        readXmlFile();

    }

    /*
        Creates an XML file using an XmlSerializer
    */
    private void createXmlFile() {

        XmlSerializer serializer = Xml.newSerializer();
        OutputStreamWriter writer = null;

        try {

            // Get a Writer to the file in the application internal storage
            writer = new OutputStreamWriter(openFileOutput(FILENAME, MODE_PRIVATE));
            // Associate this writer to the XmlSerializer
            serializer.setOutput(writer);
            // Start of document tag
            serializer.startDocument("UTF-8", null);
            // Start of Email tag
            serializer.startTag(null, EMAIL);

            // From tag
            serializer.startTag(null, FROM);
            serializer.attribute(null, NAME, "David");
            serializer.text("ddandres@disca.upv.es");
            serializer.endTag(null, FROM);

            // To tag
            serializer.startTag(null, TO);
            serializer.attribute(null, NAME, "Juan Carlos");
            serializer.text("jcruizg@disca.upv.es");
            serializer.endTag(null, TO);

            // SUbject tag
            serializer.startTag(null, SUBJECT);
            serializer.text("Next class");
            serializer.endTag(null, SUBJECT);

            // Body tag
            serializer.startTag(null, BODY);
            serializer.text("Remember to bring your smartphone to the next class");
            serializer.endTag(null, BODY);

            // End of email tag
            serializer.endTag(null, EMAIL);
            // End of document tag
            serializer.endDocument();

            serializer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Ensure that the Writer is close (if opened)
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*
        Reads the contents of the XML file and displays in the screen
    */
    private void readXmlFile() {

        // Hold references to View
        TextView tv;
        EditText et = null;

        XmlPullParser parser = Xml.newPullParser();
        InputStreamReader reader = null;
        int event;

        try {

            // Get a reader to the file in the application internal storage
            reader = new InputStreamReader(openFileInput(FILENAME));
            // Associate this reader to the XmlPullParser
            parser.setInput(reader);

            // Keep reading from the XML file until an End of Document tag is reached
            while ((event = parser.next()) != XmlPullParser.END_DOCUMENT) {

                // The action to perform depends on the event read
                switch (event) {

                    // Start of tag
                    case XmlPullParser.START_TAG:

                        // From tag
                        if (parser.getName().equalsIgnoreCase(FROM)) {

                            // Display the value of the associated attribute Name on the corresponding TextView
                            tv = findViewById(R.id.tvFrom);
                            tv.setText(String.format(getResources().getString(R.string.from),
                                    parser.getAttributeValue(null, NAME)));

                            // Get a reference to the associated EditText
                            et = findViewById(R.id.etFrom);
                        }

                        // To tag
                        else if (parser.getName().equalsIgnoreCase(TO)) {

                            // Display the value of the associated attribute Name on the corresponding TextView
                            tv = findViewById(R.id.tvTo);
                            tv.setText(String.format(getResources().getString(R.string.to),
                                    parser.getAttributeValue(null, NAME)));

                            // Get a reference to the associated EditText
                            et = findViewById(R.id.etTo);
                        }

                        // Subject tag
                        else if (parser.getName().equalsIgnoreCase(SUBJECT)) {
                            // Get a reference to the associated EditText
                            et = findViewById(R.id.etSubject);
                        }

                        // Body tag
                        else if (parser.getName().equalsIgnoreCase(BODY)) {
                            // Get a reference to the associated EditText
                            et = findViewById(R.id.etBody);
                        }
                        break;

                    // Text within a tag
                    case XmlPullParser.TEXT:
                        if (et != null) {
                            // Display the value of the associated Text in the corresponding EditText
                            et.setText(parser.getText());
                        }
                        break;
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Ensure that the Reader is closed (if opened)
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
