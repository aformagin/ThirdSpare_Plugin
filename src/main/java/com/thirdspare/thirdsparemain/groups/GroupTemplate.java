package com.thirdspare.thirdsparemain.groups;

import java.util.ArrayList;
import java.util.UUID;

public class GroupTemplate {
    protected GroupTypes groupType;
    protected String groupName;
    protected ArrayList<UUID> membersUUIDList;

    public GroupTypes getGroupType() {
        return groupType;
    }

    public void setGroupType(GroupTypes groupType) {
        this.groupType = groupType;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public ArrayList<UUID> getMembersUUIDList() {
        return membersUUIDList;
    }

    public void setMembersUUIDList(ArrayList<UUID> membersUUIDList) {
        this.membersUUIDList = membersUUIDList;
    }
}
