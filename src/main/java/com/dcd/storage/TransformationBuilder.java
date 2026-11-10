package com.dcd.storage;

import com.dcd.storage.options.TransformationOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class TransformationBuilder {
    private final String endpoint;
    private final String publicId;
    private final TransformationOptions options;
    private String deliveryPrefix = "image/upload";

    private static final Pattern KNOWN_IMAGE_EXTS = Pattern.compile(
            "\\.(jpg|jpeg|png|webp|avif|gif|tiff|svg|bmp)$",
            Pattern.CASE_INSENSITIVE
    );

    public TransformationBuilder(String endpoint, String publicId) {
        this(endpoint, publicId, new TransformationOptions(), "image/upload");
    }

    public TransformationBuilder(String endpoint, String publicId, TransformationOptions options, String deliveryPrefix) {
        this.endpoint = endpoint != null ? endpoint.replaceAll("/+$", "") : "";
        this.publicId = publicId != null ? publicId.replaceAll("^/+", "") : "";
        this.options = options != null ? options : new TransformationOptions();
        if (deliveryPrefix != null && !deliveryPrefix.isEmpty()) {
            this.deliveryPrefix = deliveryPrefix.replaceAll("^/+|/+$", "");
        }
    }

    public TransformationBuilder prefix(String prefix) {
        if (prefix != null) {
            this.deliveryPrefix = prefix.replaceAll("^/+|/+$", "");
        }
        return this;
    }

    public TransformationBuilder cloudinaryMode() {
        this.deliveryPrefix = "image/upload";
        return this;
    }

    public TransformationBuilder width(int width) {
        options.width(width);
        return this;
    }

    public TransformationBuilder height(int height) {
        options.height(height);
        return this;
    }

    public TransformationBuilder resize(int width, int height) {
        options.width(width);
        options.height(height);
        return this;
    }

    public TransformationBuilder resize(int width, int height, String crop) {
        options.resize(width, height, crop);
        return this;
    }

    public TransformationBuilder crop(String crop) {
        options.crop(crop);
        return this;
    }

    public TransformationBuilder format(String format) {
        options.format(format);
        return this;
    }

    public TransformationBuilder quality(int quality) {
        options.quality(quality);
        return this;
    }

    public TransformationBuilder quality(String quality) {
        options.quality(quality);
        return this;
    }

    public TransformationBuilder blur(int blur) {
        options.blur(blur);
        return this;
    }

    public TransformationBuilder grayscale(boolean grayscale) {
        options.grayscale(grayscale);
        return this;
    }

    public TransformationBuilder sharpen(boolean sharpen) {
        options.sharpen(sharpen);
        return this;
    }

    public TransformationBuilder rotation(String rotation) {
        options.rotation(rotation);
        return this;
    }

    public String toParamsString() {
        List<String> params = new ArrayList<>();
        if (options.getWidth() != null) params.add("w_" + options.getWidth());
        if (options.getHeight() != null) params.add("h_" + options.getHeight());
        if (options.getCrop() != null && !options.getCrop().isEmpty()) params.add("c_" + options.getCrop());
        if (options.getFormat() != null && !options.getFormat().isEmpty()) params.add("f_" + options.getFormat());
        if (options.getQuality() != null && !options.getQuality().isEmpty()) params.add("q_" + options.getQuality());
        if (options.getBlur() != null && options.getBlur() > 0) params.add("e_blur_" + options.getBlur());
        if (Boolean.TRUE.equals(options.getGrayscale())) params.add("e_gray");
        if (Boolean.TRUE.equals(options.getSharpen())) params.add("e_sharp");
        if (options.getRotation() != null && !options.getRotation().isEmpty()) params.add("a_" + options.getRotation());

        return String.join(",", params);
    }

    public String build() {
        String paramsStr = toParamsString();
        String finalPublicId = publicId;

        if (options.getFormat() != null && !options.getFormat().isEmpty()) {
            String newExt = "." + options.getFormat();
            if (KNOWN_IMAGE_EXTS.matcher(finalPublicId).find()) {
                finalPublicId = KNOWN_IMAGE_EXTS.matcher(finalPublicId).replaceAll(newExt);
            } else {
                finalPublicId = finalPublicId + newExt;
            }
        }

        if (paramsStr.isEmpty()) {
            return endpoint + "/" + deliveryPrefix + "/" + finalPublicId;
        }

        return endpoint + "/" + deliveryPrefix + "/" + paramsStr + "/" + finalPublicId;
    }

    @Override
    public String toString() {
        return build();
    }
}

