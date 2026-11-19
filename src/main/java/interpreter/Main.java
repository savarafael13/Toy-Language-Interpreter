package interpreter;

import interpreter.controller.Controller;
import interpreter.model.PrgState;
import interpreter.model.stmt.IStmt;
import interpreter.repository.Repository;
import interpreter.view.Examples;
import interpreter.view.ExitCommand;
import interpreter.view.RunExampleCommand;
import interpreter.view.TextMenu;

import java.util.List;

// A3 (§2.7): wires each example to its own PrgState, Repository (logN.txt), Controller, and Command
public class Main {

    public static void main(String[] args) {
        // Step 7 adds Examples.EX4 and Examples.EX5 to this list
        List<IStmt> programs = List.of(Examples.EX1, Examples.EX2, Examples.EX3);

        TextMenu menu = new TextMenu();
        menu.addCommand(new ExitCommand("0", "exit"));

        for (int i = 0; i < programs.size(); i++) {
            IStmt program = programs.get(i);
            var repo = new Repository(PrgState.of(program), "log" + (i + 1) + ".txt");
            var controller = new Controller(repo, true);
            menu.addCommand(new RunExampleCommand(
                    String.valueOf(i + 1),
                    program.toString(),
                    controller
            ));
        }

        menu.show();
    }
}