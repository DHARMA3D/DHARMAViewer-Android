package org.dharma.Viewer;

import java.util.List;

/**
 * Date: 5/19/13
 * Author: James Sweet
 */
public class Model {
    String Title;
    float Radius = 1.0f;
    float[] View = new float[4];
    float[] Center = new float[3];
    String Path;
    List<Cloud> Data;
}
