package commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

@CommandLine.Command(name = "fetch", description = "Fetch game dialogues in a structured format.")
public class FetchCommand implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(FetchCommand.class);

    @Override
    public void run(){

    }

}
