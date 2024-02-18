package com.xwm.beans;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class Group implements Serializable {
    private String groupId;
    private String groupName;
    private String ownerName;
    private Set<String> usernames = new CopyOnWriteArraySet();

    public Group(String groupName, String ownerName, Set<String> usernames) {
        this.groupName = groupName;
        this.ownerName = ownerName;
        this.usernames = usernames;
    }

    public Group() {
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Set<String> getUsernames() {
        return usernames;
    }

    public void setUsernames(Set<String> usernames) {
        this.usernames = usernames;
    }

    public Group(String groupId, String groupName, String ownerName, Set<String> usernames) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.ownerName = ownerName;
        this.usernames = usernames;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
