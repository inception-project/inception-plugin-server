package de.tudarmstadt.ukp.inception.pluginserver;

import org.pf4j.demo.api.Greeting;
import de.tudarmstadt.ukp.inception.pluginserver.TestAPI;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class Greetings {
	@Autowired
    private List<Greeting> greetings;

    public void printGreetings() {
        System.out.println(String.format("Found %d extensions for extension point '%s'", greetings.size(), Greeting.class.getName()));
        for (Greeting greeting : greetings) {
            System.out.println(">>> " + greeting.getGreeting());
        }
    }
    
    public void printHelloWorld() {
        for (Greeting greeting : greetings) {
            System.out.println(">>> " + greeting.getGreeting());
        }
    }
}
