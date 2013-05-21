package org.dharma.Viewer;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
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
    public static List<Model> Parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return ParseModel(parser);
        } finally {
            in.close();
        }
    }

    // Model Functions
    private static List<Model> ParseModel(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Model> models = new ArrayList<Model>();

        parser.require(XmlPullParser.START_TAG, null, "data");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("model")) {
                models.add(readModel(parser));
            } else {
                skip(parser);
            }
        }
        return models;
    }

    private static Model readModel(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "model");

        Model retVal = new Model();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("title")) {
                retVal.Title = readTitle(parser);
            } else if ( name.equals("radius") ) {
                retVal.Radius = readRadius(parser);
            } else if ( name.equals("view") ) {
                retVal.View = readView(parser);
            } else if ( name.equals("center") ) {
                retVal.Center = readCenter(parser);
            } else if ( name.equals("packagepath") ) {
                retVal.Path = readPath(parser);
            } else if ( name.equals("cloud") ) {
                retVal.Data.add(readCloud(parser));
            } else {
                skip(parser);
            }
        }

        return retVal;
    }

    private static String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "title");
        return title;
    }

    private static String readRadius(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "radius");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "radius");
        return title;
    }

    private static String readView(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "view");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "view");
        return title;
    }

    private static String readCenter(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "center");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "center");
        return title;
    }

    private static String readPath(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "packagepath");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "packagepath");
        return title;
    }

    // Cloud Functions
    private static Cloud readCloud(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "cloud");

        Cloud retVal = new Cloud();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("transformmatrix")) {
                retVal.Transformation = readCloudTransform(parser);
            } else if ( name.equals("pointscale") ) {
                retVal.Scale = readCloudScale(parser);
            } else if ( name.equals("numbercolorpoints") ) {
                retVal.Points = readCloudPoints(parser);
            } else if ( name.equals("colorpoints") ) {
                retVal.Path = readCloudPath(parser);
            } else {
                skip(parser);
            }
        }

        return retVal;
    }

    private static String readCloudTransform(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "transformmatrix");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "transformmatrix");
        return title;
    }

    private static String readCloudScale(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "pointscale");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "pointscale");
        return title;
    }

    private static String readCloudPoints(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "numbercolorpoints");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "numbercolorpoints");
        return title;
    }

    private static String readCloudPath(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "colorpoints");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "colorpoints");
        return title;
    }

    // Helper Functions
    private static String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
