package Service;

import java.util.List;

import DAO.MessageDao;
import Model.Message;

/**
 * Service class for managing messages.
 */
public class MessageService {
    private MessageDao messageDao;
    private AccountService accountService;

    /**
     * Default constructor that initializes MessageDao and AccountService.
     */
    public MessageService(){
        this.messageDao = new MessageDao();
        this.accountService = new AccountService();
    }

    /**
     * Constructor with a custom AccountService.
     * @param accountService The AccountService to be used by this service.
     */
    public MessageService(AccountService accountService){
        this.messageDao = new MessageDao();
        this.accountService = accountService;
    }

    /**
     * Constructor with a custom MessageDao.
     * @param messageDao The MessageDao to be used by this service.
     */
    public MessageService(MessageDao messageDao){
        this.messageDao = messageDao;
        this.accountService = new AccountService();
    }

    /**
     * Constructor with custom MessageDao and AccountService.
     * @param messageDao The MessageDao to be used by this service.
     * @param accountService The AccountService to be used by this service.
     */
    public MessageService(MessageDao messageDao, AccountService accountService){
        this.messageDao = messageDao;
        this.accountService = accountService;
    }

    /**
     * Creates a new message if the message text is not blank, does not exceed 255 characters,
     * and the user exists.
     * @param message The message to be created.
     * @return The created Message object if successful, or null if creation failed.
     */
    public Message createMessage(Message message){
        if(message.message_text.isBlank() || message.message_text.length() > 255 || !accountService.isUser(message.posted_by)){
            return null;
        }
        return this.messageDao.addMessage(message);
    }

    /**
     * Retrieves a message by its ID.
     * @param message_id The ID of the message to retrieve.
     * @return The Message object with the specified ID, or null if not found.
     */
    public Message getMessage(int message_id){
        return this.messageDao.getMessage(message_id);
    }

    /**
     * Retrieves all messages.
     * @return A list of all Message objects.
     */
    public List<Message> getAllMessages(){
        return this.messageDao.getAllMessages();
    }

    /**
     * Retrieves all messages for a specific account.
     * @param account_id The ID of the account whose messages to retrieve.
     * @return A list of Message objects for the specified account.
     */
    public List<Message> getAllMessages(int account_id){
        return this.messageDao.getAllMessages(account_id);
    }

    /**
     * Deletes a message by its ID. If the message exists, it is removed from the data store.
     * @param message_id The ID of the message to delete.
     * @return The deleted Message object if it existed, or null if it did not exist.
     */
    public Message deleteMessage(int message_id){
        Message message = this.messageDao.getMessage(message_id);
        if(message != null){
            this.messageDao.removeMessage(message_id);
            return message;
        }
        
        return null;
    }   

    /**
     * Updates a message by its ID. The message text must not be blank and must not exceed 255 characters.
     * @param message_id The ID of the message to update.
     * @param message The new message information to update.
     * @return The updated Message object if successful, or null if the message could not be updated.
     */
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