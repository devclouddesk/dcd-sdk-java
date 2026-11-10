package com.dcd.storage.options;

public class TransformationOptions {
    private Integer width;
    private Integer height;
    private String crop;
    private String format;
    private String quality;
    private Integer blur;
    private Boolean grayscale;
    private Boolean sharpen;
    private String rotation;

    public TransformationOptions() {}

    public static TransformationOptions builder() {
        return new TransformationOptions();
    }

    public TransformationOptions width(int width) {
        this.width = width;
        return this;
    }

    public TransformationOptions height(int height) {
        this.height = height;
        return this;
    }

    public TransformationOptions resize(int width, int height, String crop) {
        this.width = width;
        this.height = height;
        this.crop = crop;
        return this;
    }

    public TransformationOptions crop(String crop) {
        this.crop = crop;
        return this;
    }

    public TransformationOptions format(String format) {
        this.format = format;
        return this;
    }

    public TransformationOptions quality(int quality) {
        this.quality = String.valueOf(quality);
        return this;
    }

    public TransformationOptions quality(String quality) {
        this.quality = quality;
        return this;
    }

    public TransformationOptions blur(int blur) {
        this.blur = blur;
        return this;
    }

    public TransformationOptions grayscale(boolean grayscale) {
        this.grayscale = grayscale;
        return this;
    }

    public TransformationOptions sharpen(boolean sharpen) {
        this.sharpen = sharpen;
        return this;
    }

    public TransformationOptions rotation(String rotation) {
        this.rotation = rotation;
        return this;
    }

    public Integer getWidth() { return width; }
    public Integer getHeight() { return height; }
    public String getCrop() { return crop; }
    public String getFormat() { return format; }
    public String getQuality() { return quality; }
    public Integer getBlur() { return blur; }
    public Boolean getGrayscale() { return grayscale; }
    public Boolean getSharpen() { return sharpen; }
    public String getRotation() { return rotation; }
}

