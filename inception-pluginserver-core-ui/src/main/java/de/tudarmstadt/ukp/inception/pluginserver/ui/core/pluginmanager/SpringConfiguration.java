package de.tudarmstadt.ukp.inception.pluginserver.ui.core.pluginmanager;

import org.pf4j.spring.SpringPluginManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SpringConfiguration {
    @Bean
    public SpringPluginManager pluginManager() {
        /*
         * need specify the correct path 
         * now actual path for package plugin target
         */
        return new SpringPluginManager(Paths.get("D:\\plugins"));
    }

    @Bean
    @DependsOn("pluginManager")
    public Plugins plugins() {
        return new Plugins();
    }
}

