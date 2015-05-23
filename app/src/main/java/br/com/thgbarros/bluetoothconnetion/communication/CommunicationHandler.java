package br.com.thgbarros.bluetoothconnetion.communication;

/**
 * Created by thiago on 23/05/15.
 */
public class CommunicationHandler {
    private Command command;

    public CommunicationHandler(Command command){
        this.command = command;
    }

    public void setCommand(Command command){
        this.command = command;
    }

    public Command getCommand(){
        return command;
    }

    public void init(){
        command.startCommunication();
    }

    public void stop(){
        command.stopCommunication();
    }


}
