package com.driver;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class WhatsappRepository {
    HashMap<String, User> userHashMap = new HashMap<>();

    HashMap<String, List<User>> groupHashMap = new HashMap<>();

    HashMap<String, User> groupNameToAdminMap = new HashMap<>();
    HashMap<Integer, Message> messageHashMap = new HashMap<>();

    HashMap<Message, User> userSentMessages = new HashMap<>();

    HashMap<Message, Group> messages = new HashMap<>();
    int CurrentGroupNumber = 1;

    int CurrentMessageNumber = 1;

    public String createUser(String name, String mobile) throws Exception {
        if (userHashMap.containsKey(mobile))
            throw new Exception("User already exists");
        else {
            User user = new User(name, mobile);
            userHashMap.put(mobile, user);
        }
        return "SUCCESS";

    }

    public String createGroupName() {
        return "Group " + CurrentGroupNumber++;
    }

    public void saveGroup(Group group, List<User> users) {
        groupNameToAdminMap.put(group.getName(), users.get(0));
        groupHashMap.put(group.getName(), users);
    }

    public Message createMessage(String content) {
        Message message = new Message(CurrentMessageNumber++, content);
        messageHashMap.put(message.getId(), message);
        return message;
    }

    public boolean isGroupExists(String groupName) {
        return groupHashMap.containsKey(groupName);
    }

    public boolean isSenderPartOfGroup(String groupName, User sender) {
        return groupHashMap.get(groupName).contains(sender);
    }

    public int addMessageToGroup(Message message, Group group, User sender) {
        messages.put(message, group);
        userSentMessages.put(message, sender);
        return messages.size();
    }

    public boolean isApproverAdmin(User approver, Group group) {
        return groupNameToAdminMap.get(group.getName()).equals(approver);
    }

    public boolean isUserAParticipant(User user, Group group) {
        return groupHashMap.get(group.getName()).contains(user);
    }

    public void changeAdmin(User user, Group group) {
        groupNameToAdminMap.put(group.getName(), user);
    }

    public boolean ifUserIsPresent(User user) {
        for (Map.Entry<String, List<User>> set :
                groupHashMap.entrySet()) {
            if (set.getValue().contains(user)) {
                return true;
            }

        }
        return false;
    }

    public boolean ifUserIsAdmin(User user) {
        for (Map.Entry<String, User> set :
                groupNameToAdminMap.entrySet()) {
            if (set.getValue().equals(user)) {
                return true;
            }
        }
        return false;
    }

    public int removeUser(User user) {
        int updatedGroupMembers = 0;
        int updatedMessagesInGroup = 0;
        int updateOverallMessages = 0;
        String groupName = "";
        for (Map.Entry<String, List<User>> set :
                groupHashMap.entrySet()) {
            if (set.getValue().contains(user)) {
                set.getValue().remove(user);
                groupHashMap.put(set.getKey(), set.getValue());
                groupName = set.getKey();
                updatedGroupMembers = set.getValue().size();
            }
        }
        for (Map.Entry<Message, User> set : userSentMessages.entrySet()) {
            if (set.getValue().equals(user)) {
                userSentMessages.remove(set.getKey());
                messageHashMap.remove(set.getKey().getId());
                messages.remove(set.getKey());
            }
        }
        for (Map.Entry<Message, Group> set : messages.entrySet()) {
            if(set.getValue().getName().equals(groupName)) {
                updatedMessagesInGroup++;
            }
        }
        updateOverallMessages = messages.size();
        return updateOverallMessages + updatedGroupMembers + updatedMessagesInGroup;
    }

}
