package Service;

import java.util.List;

import DAO.MessageDao;
import Model.Message;

public class MessageService {
    private MessageDao messageDao;
    private AccountService accountService;

    public MessageService(){
        this.messageDao = new MessageDao();
        this.accountService = new AccountService();
    }

    public MessageService(AccountService accountService){
        this.messageDao = new MessageDao();
    }

    public MessageService(MessageDao messageDao){
        this.messageDao = messageDao;
        this.accountService = new AccountService();
    }

    public MessageService(MessageDao messageDao, AccountService accountService){
        this.messageDao = messageDao;
        this.accountService = accountService;
    }


    public Message createMessage(Message message){
        if(message.message_text.isBlank() || message.message_text.length() > 255 || !accountService.isUser(message.posted_by)){
            return null;
        }
        return this.messageDao.addMessage(message);
    }

    public Message getMessage(int id){
        return this.messageDao.getMessage(id);
    }

    public List<Message> getAllMessages(){
        return this.messageDao.getAllMessages();
    }

    public List<Message> getAllMessages(int user){
        return this.messageDao.getAllMessages(user);
    }

    public Message deleteMessage(int id){
        return this.messageDao.removeMessage();
    }

    public Message updateMessage(int id, String newText){
        if(getMessage(id) == null || newText.isBlank() || newText.length() > 255){
            return null;
        }

        return this.messageDao.updateMessage(id, newText);
    }
}
