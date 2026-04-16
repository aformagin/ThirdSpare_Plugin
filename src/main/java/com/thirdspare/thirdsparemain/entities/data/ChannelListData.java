package com.thirdspare.thirdsparemain.entities.data;

import java.util.List;

public class ChannelListData {
    private List<ChannelData> channelList;

    public ChannelListData() {
    }

    public ChannelListData(List<ChannelData> channelList) {
        this.channelList = channelList;
    }

    public List<ChannelData> getChannelList() {
        return channelList;
    }

    public void setChannelList(List<ChannelData> channelList) {
        this.channelList = channelList;
    }
}