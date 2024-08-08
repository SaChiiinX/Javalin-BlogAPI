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
        this.accountService = accountService;
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

    public Message getMessage(int message_id){
        return this.messageDao.getMessage(message_id);
    }

    public List<Message> getAllMessages(){
        return this.messageDao.getAllMessages();
    }

    public List<Message> getAllMessages(int account_id){
        return this.messageDao.getAllMessages(account_id);
    }

    public Message deleteMessage(int message_id){
        Message message = this.messageDao.getMessage(message_id);
        if(message != null){
            this.messageDao.removeMessage(message_id);
            return message;
        }
        
        return null;
    }   

    public Message updateMessage(int message_id, Message message){
        Message old = getMessage(message_id);
        if(old == null || message.message_text.isBlank() || message.message_text.length() > 255){
            return null;
        }
        this.messageDao.updateMessage(message_id, message);
        old.setMessage_text(message.message_text);
        return old;
    }
}
