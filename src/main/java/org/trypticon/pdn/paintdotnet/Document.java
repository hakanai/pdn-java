package org.trypticon.pdn.paintdotnet;

import org.trypticon.pdn.dotnet.system.Version;
import org.trypticon.pdn.dotnet.system.collections.generic.KeyValuePair;
import org.trypticon.pdn.nrbf.arrays.records.BinaryArray;
import org.trypticon.pdn.nrbf.classes.NrbfClassRecord;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Stand-in for Paint.NET class {@code PaintDotNet.Document}.
 */
@SuppressWarnings("UnstableApiUsage")
public class Document {
    private final boolean disposed;
    private final LayerList layers;
    private final int width;
    private final int height;
    private final Version savedWith;
    private final List<KeyValuePair<String, String>> userMetadataItems;

    public Document(NrbfClassRecord record) {
        Map<String, Object> map = record.asMap();
        disposed = (Boolean) map.get("isDisposed");
        layers = new LayerList(this, (NrbfClassRecord) map.get("layers"));
        width = ((Number) map.get("width")).intValue();
        height = ((Number) map.get("height")).intValue();
        savedWith = new Version((NrbfClassRecord) map.get("savedWith"));
        userMetadataItems = ((BinaryArray) map.get("userMetadataItems"))
                .getMembers().stream()
                .map(object -> new KeyValuePair<String, String>((NrbfClassRecord) object))
                .collect(Collectors.toList());
    }

    public boolean isDisposed() {
        return disposed;
    }

    public LayerList getLayers() {
        return layers;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Version getSavedWith() {
        return savedWith;
    }

    public List<KeyValuePair<String, String>> getUserMetadataItems() {
        return userMetadataItems;
    }

    /**
     * Flattens all layers to a buffered image.
     *
     * @return the buffered image.
     */
    public BufferedImage toBufferedImage() {
        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        try {
            for (Layer layer : layers) {
                BufferedImage layerImage = layer.toBufferedImage();

                // TODO: Blend mode certainly matters and isn't being considered yet.
                graphics.drawImage(layerImage, 0, 0, null);
            }
        } finally {
            graphics.dispose();
        }
        return image;
    }
}
