package Controller;

import Service.AccountService;
import Service.MessageService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        accountService = new AccountService();
        messageService = new MessageService(accountService);
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
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

    private void postUserRegisterHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper(); 
        Account account = mapper.readValue(context.body(), Account.class);
        Account registeredAccount = accountService.registerUser(account);
        if(registeredAccount!=null){
            context.json(mapper.writeValueAsString(registeredAccount));
        }else{
            context.status(400);
        }
    }

    private void postUserLoginHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper(); 
        Account account = mapper.readValue(context.body(), Account.class);
        Account verifiedAccount = accountService.verifyAccountDetails(account);
        if(verifiedAccount!=null){
            context.json(mapper.writeValueAsString(verifiedAccount));
        }else{
            context.status(401);
        }
    }

    private void postMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper(); 
        Message message = mapper.readValue(context.body(), Message.class);
        Message addedMessage = messageService.createMessage(message);
        if(addedMessage!=null){
            context.json(mapper.writeValueAsString(addedMessage));
        }else{
            context.status(400);
        }
    }

    private void getAllMessagesHandler(Context context){
        context.json(messageService.getAllMessages());
    }

    private void getMessageByIdHandler(Context context) throws JsonProcessingException{
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.getMessage(message_id);
        if(message != null){
            context.json(message);
        }   
    }

    private void deleteMessageHandler(Context context){
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessage(message_id);
        if(deletedMessage != null){
            context.json(deletedMessage);
        }
    }

    private void patchMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper(); 
        Message message = mapper.readValue(context.body(), Message.class);
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message updatedMessage = messageService.updateMessage(message_id, message);
        if(updatedMessage!=null){
            context.json(updatedMessage);
        }else{
            context.status(400);
        }
    }

    private void getAllMessagesByUserHandler(Context context){
        int account_id = Integer.parseInt(context.pathParam("account_id"));
        context.json(messageService.getAllMessages(account_id));
    }
}