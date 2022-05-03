package commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

@CommandLine.Command(name = "patch", description = "Create a patched version of the file based on the structured format.")
public class PatchCommand implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(PatchCommand.class);

    @Override
    public void run(){

    }

}

