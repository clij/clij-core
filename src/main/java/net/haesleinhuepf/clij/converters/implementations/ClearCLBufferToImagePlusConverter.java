package net.haesleinhuepf.clij.converters.implementations;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import ij.ImagePlus;
import ij.gui.NewImage;
import ij.plugin.Duplicator;
import net.haesleinhuepf.clij.converters.AbstractCLIJConverter;
import net.haesleinhuepf.clij.converters.CLIJConverterPlugin;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.display.imagej.ImageJFunctions;
import org.scijava.plugin.Plugin;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * ClearCLBufferToImagePlusConverter
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 12 2018
 */
@Plugin(type = CLIJConverterPlugin.class)
public class ClearCLBufferToImagePlusConverter extends AbstractCLIJConverter<ClearCLBuffer, ImagePlus> {

    final long SMALL_IMAGE_SIZE = 2048 * 2048;

    @Override
    public ImagePlus convert(ClearCLBuffer source) {
        int width = (int) source.getWidth();
        int height = (int) source.getHeight();
        int depth = (int) source.getDepth();

        int numberOfPixelsPerPlane = width * height;
        long numberOfPixels = (long)numberOfPixelsPerPlane * (long)depth;

        ImagePlus result = null;

        if (numberOfPixelsPerPlane >= SMALL_IMAGE_SIZE) {
            result = convertBigImage(source, numberOfPixelsPerPlane, width, height, depth);
        } else {
            if (source.getNativeType() == NativeTypeEnum.UnsignedByte) {
                result = NewImage.createByteImage("slice", width, height, depth, NewImage.FILL_BLACK);
                byte[] array = new byte[(int) numberOfPixels];
                ByteBuffer buffer = ByteBuffer.wrap(array);
                source.writeTo(buffer, true);

                for (int z = 0; z < depth; z++) {
                    result.setSlice(z + 1);
                    byte[] sliceArray = (byte[]) result.getProcessor().getPixels();
                    System.arraycopy(array, z * numberOfPixelsPerPlane, sliceArray, 0, sliceArray.length);
                }
            } else if (source.getNativeType() == NativeTypeEnum.UnsignedShort) {
                result = NewImage.createShortImage("slice", width, height, depth, NewImage.FILL_BLACK);

                short[] array = new short[(int) numberOfPixels];
                ShortBuffer buffer = ShortBuffer.wrap(array);
                source.writeTo(buffer, true);

                for (int z = 0; z < depth; z++) {
                    result.setSlice(z + 1);
                    short[] sliceArray = (short[]) result.getProcessor().getPixels();
                    System.arraycopy(array, z * numberOfPixelsPerPlane, sliceArray, 0, sliceArray.length);
                }
            } else if (source.getNativeType() == NativeTypeEnum.Float) {
                result = NewImage.createFloatImage("slice", width, height, depth, NewImage.FILL_BLACK);

                float[] array = new float[(int) numberOfPixels];
                FloatBuffer buffer = FloatBuffer.wrap(array);
                source.writeTo(buffer, true);

                for (int z = 0; z < depth; z++) {
                    result.setSlice(z + 1);
                    float[] sliceArray = (float[]) result.getProcessor().getPixels();
                    System.arraycopy(array, z * numberOfPixelsPerPlane, sliceArray, 0, sliceArray.length);
                }
            }
        }

        if (result == null) {
            result = convertLegacy(source);
        }
        return result;

    }

    private ImagePlus convertBigImage(ClearCLBuffer source, int numberOfPixelsPerPlane, int width, int height, int depth) {
        ImagePlus result = null;

        if (source.getNativeType() == NativeTypeEnum.UnsignedByte) {
            result = NewImage.createByteImage("slice", width, height, depth, NewImage.FILL_BLACK);

            byte[] array = new byte[numberOfPixelsPerPlane];
            for (int z = 0; z < depth; z++) {
                ByteBuffer buffer = ByteBuffer.wrap(array);
                source.writeTo(buffer, new long[]{0, 0, z}, new long[]{0,0,0}, new long[]{width, height}, true);

                result.setSlice(z + 1);
                byte[] sliceArray = (byte[]) result.getProcessor().getPixels();
                System.arraycopy(array, 0, sliceArray, 0, sliceArray.length);
            }
        } else if (source.getNativeType() == NativeTypeEnum.UnsignedShort) {
            result = NewImage.createShortImage("slice", width, height, depth, NewImage.FILL_BLACK);

            short[] array = new short[numberOfPixelsPerPlane];

            for (int z = 0; z < depth; z++) {
                ShortBuffer buffer = ShortBuffer.wrap(array);
                source.writeTo(buffer, new long[]{0, 0, z}, new long[]{0,0,0}, new long[]{width, height}, true);
                result.setSlice(z + 1);
                short[] sliceArray = (short[]) result.getProcessor().getPixels();
                System.arraycopy(array, 0, sliceArray, 0, sliceArray.length);
            }
        } else if (source.getNativeType() == NativeTypeEnum.Float) {
            result = NewImage.createFloatImage("slice", width, height, depth, NewImage.FILL_BLACK);

            float[] array = new float[numberOfPixelsPerPlane];

            for (int z = 0; z < depth; z++) {
                FloatBuffer buffer = FloatBuffer.wrap(array);
                source.writeTo(buffer, new long[]{0, 0, z}, new long[]{0,0,0}, new long[]{width, height}, true);

                result.setSlice(z + 1);
                float[] sliceArray = (float[]) result.getProcessor().getPixels();
                System.arraycopy(array, 0, sliceArray, 0, sliceArray.length);
            }
        }
        return result;
    }

    public ImagePlus convertLegacy(ClearCLBuffer source) {
        ClearCLBufferToRandomAccessibleIntervalConverter cclbtraic = new ClearCLBufferToRandomAccessibleIntervalConverter();
        cclbtraic.setCLIJ(clij);
        RandomAccessibleInterval rai = cclbtraic.convert(source);
        return new Duplicator().run(ImageJFunctions.wrap(rai, "" + rai));
    }

    @Override
    public Class<ClearCLBuffer> getSourceType() {
        return ClearCLBuffer.class;
    }

    @Override
    public Class<ImagePlus> getTargetType() {
        return ImagePlus.class;
    }
}
