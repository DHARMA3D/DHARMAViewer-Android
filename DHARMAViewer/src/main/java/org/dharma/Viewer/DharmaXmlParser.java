package org.dharma.Viewer;

import java.io.InputStream;
import java.util.List;
import android.util.Xml;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * Date: 5/19/13
 * Author: James Sweet
 */
public class DharmaXmlParser {
    private static final String ns = null;

    public List<Model> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return null;
        } finally {
            in.close();
        }
    }
}
