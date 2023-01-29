package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class WhatsappService {

    @Autowired
    WhatsappRepository whatsappRepository;

    public String createUser(String name, String mobile) throws Exception {
        return whatsappRepository.createUser(name, mobile);
    }

    public Group createGroup(List<User> users) {
        Group group = new Group();
        if (users.size() > 2) {
             group = new Group(whatsappRepository.createGroupName(), users.size());
             whatsappRepository.saveGroup(group, users);
        }else if (users.size() == 2){
            group = new Group(users.get(1).getName(), users.size());
            whatsappRepository.saveGroup(group, users);
        }
        return group;
    }

    public int createMessage(String content) {
        Message message = whatsappRepository.createMessage(content);
        return message.getId();
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception {
        if(!whatsappRepository.isGroupExists(group.getName())) {
            throw new Exception("Group does not exist");
        }
        if (!whatsappRepository.isSenderPartOfGroup(group.getName(), sender)) {
            throw new Exception("You are not allowed to send message");
        }
        return whatsappRepository.addMessageToGroup(message, group, sender);
    }

    public String changeAdmin(User approver, User user, Group group) throws Exception {
        if(!whatsappRepository.isGroupExists(group.getName())) {
            throw new Exception("Group does not exist");
        }
        if (!whatsappRepository.isApproverAdmin(approver, group)) {
            throw new Exception("Approver does not have rights");
        }

        if (!whatsappRepository.isUserAParticipant(user, group)) {
            throw new Exception("User is not a participant");
        }
        whatsappRepository.changeAdmin(user, group);
        return "SUCCESS";
    }

    public int removeUser(User user) throws Exception {
        if(!whatsappRepository.ifUserIsPresent(user))
            throw new Exception( "User not found");

         if(whatsappRepository.ifUserIsAdmin(user))
             throw new Exception("Cannot remove admin");
         return whatsappRepository.removeUser(user);
    }

    public String findMessage(Date start, Date end, int k) {
        return "";
    }
}
