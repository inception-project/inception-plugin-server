package de.tudarmstadt.ukp.inception.pluginserver;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class Plugins {
	@Autowired
    private List<Api> plugins;

	public boolean hasPlugins() {
	    if(plugins.size() > 0) {
            System.out.println(">>>>>> plugins attached");
        }
        else {
            System.out.println(">>>>>> no plugins");
        }
	    
	    return (plugins.size() > 0);
	}
	
	public List<Api> getPlugins() {
	    return plugins;
	}
	
//	public void addMenuItems() {
//
//        for (Api plugin : plugins) {
//            plugin.addMenuItem();
//        }
//    }
	
	/*
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
    */
}
