package de.tudarmstadt.ukp.inception.pluginserver;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class Greetings {
	@Autowired
    private List<Api> greetings;

    public void printGreetings() {
        System.out.println(String.format("Found %d extensions for extension point '%s'", greetings.size(), Api.class.getName()));
        for (Api greeting : greetings) {
            System.out.println(">>> " + greeting.getTestMessage());
        }
    }
    
    public void printHelloWorld() {
        for (Api greeting : greetings) {
            System.out.println(">>> " + greeting.getTestMessage());
        }
    }
}
