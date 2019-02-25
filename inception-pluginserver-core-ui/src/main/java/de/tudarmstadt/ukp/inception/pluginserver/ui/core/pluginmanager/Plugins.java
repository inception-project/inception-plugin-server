package de.tudarmstadt.ukp.inception.pluginserver.ui.core.pluginmanager;

import org.springframework.beans.factory.annotation.Autowired;

import de.tudarmstadt.ukp.inception.pluginserver.ui.ApiUiCore;

import java.util.List;

public class Plugins {
	@Autowired
    private List<ApiUiCore> plugins;

	public boolean hasPlugins() {
	    if(plugins.size() > 0) {
            System.out.println(">>>>>> plugins attached");
        }
        else {
            System.out.println(">>>>>> no plugins");
        }
	    
	    return (plugins.size() > 0);
	}
	
	public List<ApiUiCore> getPlugins() {
	    return plugins;
	}
}
