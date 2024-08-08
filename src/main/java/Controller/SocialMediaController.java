package Controller;

import Service.AccountService;
import Service.MessageService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * Controller class for handling HTTP requests related to social media operations.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    /**
     * Constructor that initializes the AccountService and MessageService.
     */
    public SocialMediaController(){
        accountService = new AccountService();
        messageService = new MessageService(accountService);
    }

    /**
     * Configures and starts the Javalin API server with the defined endpoints.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postUserRegisterHandler);
        app.post("/login", this::postUserLoginHandler);
        app.post("messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::patchMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByUserHandler);
        return app;
    }

    /**
     * Handles the registration of a new user. Reads the user details from the request body,
     * registers the user, and responds with the registered user details or a 400 status code.
     * @param context The Javalin context containing the HTTP request and response.
     * @throws JsonProcessingException If there is an error processing JSON.
     */
    private void postUserRegisterHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper(); 
        Account account = mapper.readValue(context.body(), Account.class);
        Account registeredAccount = accountService.registerUser(account);
        if(registeredAccount != null){
            context.json(mapper.writeValueAsString(registeredAccount));
        } else {
            context.status(400);
        }
    }

    /**
     * Handles user login. Reads the user credentials from the request body, verifies the user,
     * and responds with the user details or a 401 status code if authentication fails.
     * @param context The Javalin context containing the HTTP request and response.
     * @throws JsonProcessingException If there is an error processing JSON.
     */
    private void postUserLoginHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper(); 
        Account account = mapper.readValue(context.body(), Account.class);
        Account verifiedAccount = accountService.verifyAccountDetails(account);
        if(verifiedAccount != null){
            context.json(mapper.writeValueAsString(verifiedAccount));
        } else {
            context.status(401);
        }
    }

    /**
     * Handles the creation of a new message. Reads the message details from the request body,
     * creates the message, and responds with the created message or a 400 status code if creation fails.
     * @param context The Javalin context containing the HTTP request and response.
     * @throws JsonProcessingException If there is an error processing JSON.
     */
    private void postMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper(); 
        Message message = mapper.readValue(context.body(), Message.class);
        Message addedMessage = messageService.createMessage(message);
        if(addedMessage != null){
            context.json(mapper.writeValueAsString(addedMessage));
        } else {
            context.status(400);
        }
    }

    /**
     * Handles the retrieval of all messages. Responds with a list of all messages.
     * @param context The Javalin context containing the HTTP request and response.
     */
    private void getAllMessagesHandler(Context context){
        context.json(messageService.getAllMessages());
    }

    /**
     * Handles the retrieval of a specific message by its ID. Responds with the message details 
     * or an empty file if the message is not found.
     * @param context The Javalin context containing the HTTP request and response.
     * @throws JsonProcessingException If there is an error processing JSON.
     */
    private void getMessageByIdHandler(Context context) throws JsonProcessingException{
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.getMessage(message_id);
        if(message != null){
            context.json(message);
        }
    }

    /**
     * Handles the deletion of a message by its ID. Responds with the deleted message or 
     * an empty file if the message is not found.
     * @param context The Javalin context containing the HTTP request and response.
     */
    private void deleteMessageHandler(Context context){
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessage(message_id);
        if(deletedMessage != null){
            context.json(deletedMessage);
        }
    }

    /**
     * Handles the update of a message by its ID. Reads the updated message details from the request body,
     * updates the message, and responds with the updated message or a 400 status code if the update fails.
     * @param context The Javalin context containing the HTTP request and response.
     * @throws JsonProcessingException If there is an error processing JSON.
     */
    private void patchMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper(); 
        Message message = mapper.readValue(context.body(), Message.class);
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message updatedMessage = messageService.updateMessage(message_id, message);
        if(updatedMessage != null){
            context.json(updatedMessage);
        } else {
            context.status(400);
        }
    }

    /**
     * Handles the retrieval of all messages posted by a specific user. Responds with a list of messages for 
     * the specified account ID.
     * @param context The Javalin context containing the HTTP request and response.
     */
    private void getAllMessagesByUserHandler(Context context){
        int account_id = Integer.parseInt(context.pathParam("account_id"));
        context.json(messageService.getAllMessages(account_id));
    }
}
