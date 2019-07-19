package net.haesleinhuepf.clij.utilities;

import net.imglib2.realtransform.AffineTransform2D;
import net.imglib2.realtransform.AffineTransform3D;

/**
 * AffineTransform
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 01 2019
 */
public class AffineTransform {
    public static float[] matrixToFloatArray(AffineTransform3D at) {
        return new float[] {
                (float) at.get(0,0), (float) at.get(0,1), (float) at.get(0,2), (float) at.get(0,3),
                (float) at.get(1,0), (float) at.get(1,1), (float) at.get(1,2), (float) at.get(1,3),
                (float) at.get(2,0), (float) at.get(2,1), (float) at.get(2,2), (float) at.get(2,3)
        };
    }

    public static float[] matrixToFloatArray2D(AffineTransform2D at) {
        return new float[] {
                (float) at.get(0,0), (float) at.get(0,1), (float) at.get(0,2),
                (float) at.get(1,0), (float) at.get(1,1), (float) at.get(1,2),
        };
    }
}
