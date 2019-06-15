package net.haesleinhuepf.clij.converters;

import ij.ImagePlus;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.converters.implementations.*;
import net.imglib2.RandomAccessibleInterval;

import java.util.HashMap;

public class FallBackCLIJConverterService extends CLIJConverterService {

    public FallBackCLIJConverterService() {
        converterPlugins.put(new ClassPair(ClearCLBuffer.class, ClearCLImage.class), new ClearCLBufferToClearCLImageConverter());
        converterPlugins.put(new ClassPair(ClearCLBuffer.class, ImagePlus.class), new ClearCLBufferToImagePlusConverter());
        converterPlugins.put(new ClassPair(ClearCLBuffer.class, RandomAccessibleInterval.class), new ClearCLBufferToRandomAccessibleIntervalConverter());
        converterPlugins.put(new ClassPair(ClearCLImage.class, ClearCLBuffer.class), new ClearCLImageToClearCLBufferConverter());
        converterPlugins.put(new ClassPair(ClearCLImage.class, ImagePlus.class), new ClearCLImageToImagePlusConverter());
        converterPlugins.put(new ClassPair(ClearCLImage.class, RandomAccessibleInterval.class), new ClearCLImageToRandomAccessibleIntervalConverter());
        converterPlugins.put(new ClassPair(ImagePlus.class, ClearCLBuffer.class), new ImagePlusToClearCLBufferConverter());
        converterPlugins.put(new ClassPair(ImagePlus.class, ClearCLImage.class), new ImagePlusToClearCLImageConverter());
        converterPlugins.put(new ClassPair(ImagePlus.class, RandomAccessibleInterval.class), new ImagePlusToRandomAccessibleIntervalConverter());
        converterPlugins.put(new ClassPair(NioBuffer.class, ClearCLBuffer.class), new NioToClearCLBufferConverter());
        converterPlugins.put(new ClassPair(RandomAccessibleInterval.class, ClearCLBuffer.class), new RandomAccessibleIntervalToClearCLBufferConverter());
        converterPlugins.put(new ClassPair(RandomAccessibleInterval.class, ClearCLImage.class), new RandomAccessibleIntervalToClearCLImageConverter());
        converterPlugins.put(new ClassPair(RandomAccessibleInterval.class, ImagePlus.class), new RandomAccessibleIntervalToImagePlusConverter());
    }

    private final HashMap<ClassPair, CLIJConverterPlugin> converterPlugins = new HashMap<>();

    @Override
    public <S, T> CLIJConverterPlugin<S, T> getConverter(Class<S> a, Class<T> b) {
        CLIJConverterPlugin<S, T> converter = getConverter(new ClassPair(a, b));
        if (converter == null) {
            throw new IllegalArgumentException("Couldn't instantiate converter found from " + a + " to " + b);
        }
        converter.setCLIJ(getCLIJ());
        return converter;
    }

    private <S, T> CLIJConverterPlugin<S, T> getConverter(ClassPair classPair) {
        for (ClassPair pair : converterPlugins.keySet()) {
            if (pair.a == classPair.a && pair.b == classPair.b) {
                return converterPlugins.get(pair);
            }
        }
        return null;
    }
}
