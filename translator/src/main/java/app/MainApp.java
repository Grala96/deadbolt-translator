package app;

import commands.FetchCommand;
import commands.PatchCommand;
import commands.ValidateCommand;
import commands.VerifyCommand;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;

@Slf4j
@CommandLine.Command(name = "deadbolt-translator", sortOptions = false,
        header = {
                "@|green +-+-+-+-+-+-+-+-+ +-+-+-+-+-+-+-+-+-+-+-+ |@",
                "@|green |D|E|A|D|B|O|L|T| |T|R|A|N|S|L|A|T|I|O|N| |@",
                "@|green +-+-+-+-+-+-+-+-+ +-+-+-+-+-+-+-+-+-+-+-+ |@",
                ""},
        description = {
                "",
                "DeadBolt Translation Tool"}
)
public class MainApp {

    public static void main(String[] args) throws Exception {
        CommandLine mainCommand = mainCommand();
        int exitCode = mainCommand.execute(args);
        assert exitCode == 0;
    }

    static CommandLine mainCommand() {
        CommandLine commandLine = new CommandLine(new MainApp());
        commandLine.addSubcommand("help", new CommandLine.HelpCommand());
        commandLine.addSubcommand("fetch", new FetchCommand());
        commandLine.addSubcommand("patch", new PatchCommand());
        commandLine.addSubcommand("verify", new VerifyCommand());
        commandLine.addSubcommand("validate", new ValidateCommand());
        return commandLine;
    }

}

// Exit Codes: https://tldp.org/LDP/abs/html/exitcodes.html