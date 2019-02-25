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
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;
import org.pf4j.RuntimeMode;

import de.tudarmstadt.ukp.inception.pluginserver.ui.ApiUiCore;


public class AddMenuItemPlugin extends Plugin {

    public AddMenuItemPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Override
    public void start() {
        System.out.println("AddMenuItemPlugin.start()");
        // for testing the development mode
        if (RuntimeMode.DEVELOPMENT.equals(wrapper.getRuntimeMode())) {
            System.out.println(StringUtils.upperCase("AddMenuItemPlugin"));
        }
    }

    @Override
    public void stop() {
        System.out.println("AddMenuItemPlugin.stop()");
    }
    
    @Extension
    public static class AddMenuItemApiUiCore implements ApiUiCore {
        
        @Override
        public String getMenuItem() {
            return "Plugin#1 AddMenuItemPlugin";
        }

    }

}
