/*
 * Copyright 2019
 * Ubiquitous Knowledge Processing (UKP) Lab and FG Language Technology
 * Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *  http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.tudarmstadt.ukp.inception.pluginserver.ui.core.pluginmanager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import de.tudarmstadt.ukp.inception.pluginserver.ui.ApiUiCore;

public class PlaceholderPluginList
{
    public static List<PlaceholderPlugin> userPlugins()
    {
        ApplicationContext applicationContext = 
                new AnnotationConfigApplicationContext(SpringConfiguration.class);

        Plugins plugins = applicationContext.getBean(Plugins.class); 
        
        ArrayList<String> manuitems = new ArrayList<String>();
        
        manuitems.add("My first plugin");
        manuitems.add("My second plugin");
        manuitems.add("My third plugin");
        
        if (plugins.hasPlugins()) {
            List<ApiUiCore> oPlugins = plugins.getPlugins();
              
            for (ApiUiCore plugin : oPlugins) {
                manuitems.add(plugin.getMenuItem());
            }
        }
        
        Stream<Object> stringStream = Stream.of(manuitems.toArray());
        
        String[] stringArray = stringStream.toArray(size -> new String[size]);

        return Arrays.stream(stringArray)
                .map(x -> new PlaceholderPlugin(x, "Me",
                        "0.0.1", x, "Example License", true))
                .collect(Collectors.toList());
    }
    
    public static List<PlaceholderPlugin> allPlugins()
    {
        List<PlaceholderPlugin> allPlugins = userPlugins();
        allPlugins.addAll(Arrays.stream(new String[] {"Someone else's first plugin",
                "Someone else's second plugin", "Someone else's third plugin"})
                .map(x -> new PlaceholderPlugin(x, "Someone else",
                        "0.0.1", x, "Example License", true))
                .collect(Collectors.toList()));
        return allPlugins;
    }
}
