package interpreter.view;

// A3 (§2.7): menu option "0" that exits the application
public class ExitCommand extends Command {
    public ExitCommand(String key, String description) {
        super(key, description);
    }

    @Override
    public void execute() {
        System.exit(0);
    }
}