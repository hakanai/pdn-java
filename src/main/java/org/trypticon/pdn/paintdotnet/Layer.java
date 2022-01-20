package org.trypticon.pdn.paintdotnet;

import com.google.common.primitives.UnsignedInteger;
import org.trypticon.pdn.dotnet.system.collections.generic.KeyValuePair;
import org.trypticon.pdn.nrbf.arrays.records.BinaryArray;
import org.trypticon.pdn.nrbf.classes.NrbfClassRecord;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Stand-in for Paint.NET class {@code PaintDotNet.BitmapLayer}.
 */
public abstract class Layer {
    private final boolean disposed;
    private final int width;
    private final int height;
    private final LayerProperties properties;

    protected Layer(NrbfClassRecord record) {
        Map<String, Object> map = record.asMap();
        disposed = (boolean) map.get("Layer+isDisposed");
        width = (int) map.get("Layer+width");
        height = (int) map.get("Layer+height");
        properties = new LayerProperties((NrbfClassRecord) map.get("Layer+properties"));
    }

    public boolean isDisposed() {
        return disposed;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public LayerProperties getProperties() {
        return properties;
    }

    /**
     * Converts the layer to a buffered image.
     *
     * @return the buffered image.
     */
    public abstract BufferedImage toBufferedImage();

    public static class LayerProperties {
        private final String name;
        private final List<KeyValuePair<String, String>> userMetadataItems;
        private final boolean visible;
        private final boolean background;
        private final UnsignedInteger opacity;
        private final LayerBlendMode blendMode;

        public LayerProperties(NrbfClassRecord record) {
            Map<String, Object> map = record.asMap();
            name = (String) map.get("name");
            userMetadataItems = ((BinaryArray) map.get("userMetadataItems"))
                    .getMembers().stream()
                    .map(object -> new KeyValuePair<String, String>((NrbfClassRecord) object))
                    .collect(Collectors.toList());
            visible = (boolean) map.get("visible");
            background = (boolean) map.get("isBackground");
            opacity = (UnsignedInteger) map.get("opacity");

            if (map.get("blendMode") != null) {
                blendMode = new LayerBlendMode((NrbfClassRecord) map.get("blendMode"));
            } else {
                blendMode = new LayerBlendMode();
            }
        }

        public String getName() {
            return name;
        }

        public List<KeyValuePair<String, String>> getUserMetadataItems() {
            return userMetadataItems;
        }

        public boolean isVisible() {
            return visible;
        }

        public boolean isBackground() {
            return background;
        }

        public UnsignedInteger getOpacity() {
            return opacity;
        }

        public LayerBlendMode getBlendMode() {
            return blendMode;
        }
    }
}
