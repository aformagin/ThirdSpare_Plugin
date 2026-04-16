package com.thirdspare.thirdsparemain.entities.data;

public class ChannelData {
    private String name;
    private String prefix;
    private String color;

    public ChannelData() {
    }

    public ChannelData(String name, String prefix, String color) {
        this.name = name;
        this.prefix = prefix;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}