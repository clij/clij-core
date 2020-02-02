package net.haesleinhuepf.clij.utilities;

import net.haesleinhuepf.clij.clearcl.ClearCLImage;

/**
 * CLIJUtilities
 * <p>
 * Author: @haesleinhuepf
 * December 2018
 */
public class CLIJUtilities {
    public static int radiusToKernelSize(int radius) {
        int kernelSize = radius * 2 + 1;
        return kernelSize;
    }

    public static int sigmaToKernelSize(float sigma) {
        int n = (int)(sigma * 8.0);
        if (n % 2 == 0) {
            n++;
        }
        return n;
    }

    public static String classToName(Class aClass) {
        String name = aClass.getSimpleName();
        if (aClass.getPackage().toString().contains(".clij2.")) {
            return "CLIJ2_" + name.substring(0, 1).toLowerCase() + name.substring(1, name.length());
        } else if (aClass.getPackage().toString().contains(".clijx.")) {
            return "CLIJx_" + name.substring(0, 1).toLowerCase() + name.substring(1, name.length());
        } else {
            return "CLIJ_" + name.substring(0, 1).toLowerCase() + name.substring(1, name.length());
        }
    }

    public static void assertDifferent(Object src, Object dst) {
        if (src == dst) {
            throw new IllegalArgumentException("Input image and output image must be different. Inplace operations are not supported.");
        }
    }

}
