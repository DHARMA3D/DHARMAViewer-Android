package org.dharma.Viewer;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.res.AssetManager;
import android.util.Xml;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * Date: 5/19/13
 * Author: James Sweet
 */
public class DharmaXmlParser {
    public static List<Model> Parse(AssetManager assets, String file) throws XmlPullParserException, IOException {
        InputStream in = assets.open(file);
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return ParseModel(assets, parser);
        } finally {
            in.close();
        }
    }

    // Model Functions
    private static List<Model> ParseModel(AssetManager assets, XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Model> models = new ArrayList<Model>();

        parser.require(XmlPullParser.START_TAG, null, "data");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("model")) {
                models.add(readModel(assets, parser));
            } else {
                skip(parser);
            }
        }
        return models;
    }

    private static Model readModel(AssetManager assets, XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "model");

        Model retVal = new Model(assets);

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
                readCloud(retVal, parser);
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

    private static float readRadius(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "radius");
        String data = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "radius");
        return Float.parseFloat(data);
    }

    private static float[] readView(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "view");
        String data = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "view");

        float retVal[] = new float[4];

        int position = 0;
        for( String v : data.split(", ") ){
            retVal[position] = Float.parseFloat(v);
            position += 1;
        }

        return retVal;
    }

    private static float[] readCenter(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "center");
        String data = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "center");

        float retVal[] = new float[3];

        int position = 0;
        for( String v : data.split(", ") ){
            retVal[position] = Float.parseFloat(v);
            position += 1;
        }

        return retVal;
    }

    private static String readPath(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "packagepath");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "packagepath");
        return title;
    }

    // Cloud Functions
    private static void readCloud( Model model, XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "cloud");

        float[] Transformation = new float[16];
        float Scale = 1.0f;
        int Points = 0;
        String Path = "";

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("transformmatrix")) {
                Transformation = readCloudTransform(parser);
            } else if ( name.equals("pointscale") ) {
                Scale = readCloudScale(parser);
            } else if ( name.equals("numbercolorpoints") ) {
                Points = readCloudPoints(parser);
            } else if ( name.equals("colorpoints") ) {
                Path = readCloudPath(parser);
            } else {
                skip(parser);
            }
        }

        model.addCloud(Transformation, Scale, Points, Path);
    }

    private static float[] readCloudTransform(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "transformmatrix");
        String data = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "transformmatrix");

        float retVal[] = new float[16];

        int position = 0;
        for( String v : data.split(", ") ){
            retVal[position] = Float.parseFloat(v);
            position += 1;
        }

        return retVal;
    }

    private static float readCloudScale(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "pointscale");
        String data = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "pointscale");

        return Float.parseFloat(data);
    }

    private static int readCloudPoints(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "numbercolorpoints");
        String data = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "numbercolorpoints");

        return Integer.parseInt(data);
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
