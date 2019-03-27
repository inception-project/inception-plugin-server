/*
 * Copyright 2019
 * Ubiquitous Knowledge Processing (UKP) Lab
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

package de.tudarmstadt.ukp.inception.pluginserver;

import org.apache.commons.lang.StringUtils;
import org.pf4j.Extension;
import org.pf4j.ExtensionPoint;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;
import org.pf4j.RuntimeMode;

import de.tudarmstadt.ukp.clarin.webanno.ui.core.menu.MenuItem;
import de.tudarmstadt.ukp.inception.pluginserver.ui.ApiUiCore;

/** 
 * Plugin to add a test menu item. Is an extension <b>Plugin</b> (PF4J)
 * @see Plugin
 * @author vladzb1
 */
public class AddMenuItemPlugin extends Plugin {

    /** 
     * Plugin to add a test menu item. Is an extension <b>Plugin</b> (PF4J)
     * @param wrapper - Wrapper
     */
    public AddMenuItemPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    /** 
     * After connecting and starting the plugin executes the described code
     */
    @Override
    public void start() {
        System.out.println("AddMenuItemPlugin.start()");
        
        if (RuntimeMode.DEVELOPMENT.equals(wrapper.getRuntimeMode())) {
            System.out.println(StringUtils.upperCase("AddMenuItemPlugin"));
        }
    }

    /** 
     * After stoping the plugin executes the described code
     */
    @Override
    public void stop() {
        System.out.println("AddMenuItemPlugin.stop()");
    }
    
    /** 
     * Implements a pre-described interface {@link ApiUiCore} for data exchange with the system
     */
    @Extension
    public static class AddMenuItemApiUiCore implements ApiUiCore {
        
        /** 
         * Method to get the name of the plugin
         * @return Plugin's name
         */
        @Override
        public String getPluginName() { 
            NewPluginMenuItem menuItem = new NewPluginMenuItem();
            return menuItem.getLabel();
        }
        
        /**
         * Method to get page class and menu item
         * @return Test page and menuitem
         */
        @Override
        public Class<? extends MenuItem> getMenuItem() { 
            return NewPluginMenuItem.class;
        }

    }
}
