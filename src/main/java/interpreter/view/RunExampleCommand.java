package interpreter.view;

import interpreter.controller.Controller;
import interpreter.exceptions.MyException;

// A3 (§2.7): runs a single example program once and catches MyException so the menu stays alive
public class RunExampleCommand extends Command {
    private final Controller controller;
    // A3 (§2.7, §4): prevents running the same PrgState/Repository instance more than once
    private boolean executed = false;

    public RunExampleCommand(String key, String description, Controller controller) {
        super(key, description);
        this.controller = controller;
    }

    @Override
    public void execute() {
        if (executed) {
            System.out.println("This example has already been executed.");
            return;
        }
        executed = true;
        try {
            controller.allStep();
        } catch (MyException e) {
            // A3 (§2.7): interpreter errors are printed cleanly without crashing the menu
            System.out.println("Error: " + e.getMessage());
        }
    }
}