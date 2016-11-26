package lu.dainesch.lighty.messaging;


public class WSCommand extends WSMessage {

    public static enum Command {
        STREAM, KICK, OTHERSTREAM
    }

    private Command command;

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

}
