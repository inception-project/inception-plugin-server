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
package de.tudarmstadt.ukp.inception.pluginserver.ui.core.dashboard.dashlet;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;

import de.tudarmstadt.ukp.inception.pluginserver.ui.core.pluginbrowser.PluginBrowsePage;
import de.tudarmstadt.ukp.inception.pluginserver.ui.core.pluginmanager.PlaceholderPlugin;

public class CurrentPluginDashlet
    extends Panel
{

    private static final long serialVersionUID = -3431335093802133184L;

    public CurrentPluginDashlet(String id)
    {
        super(id);

        add(new Label("name", LoadableDetachableModel.of(this::getPluginName)));
        add(new Label("author", LoadableDetachableModel.of(this::getPluginAuthor)));
        add(new Label("version", LoadableDetachableModel.of(this::getPluginVersion)));
        add(new Label("license", LoadableDetachableModel.of(this::getPluginLicense)));

        add(new Label("description", LoadableDetachableModel.of(this::getPluginDescription))
                .setEscapeModelStrings(false));
    }

    private PlaceholderPlugin getPlugin()
    {
        PlaceholderPlugin plugin = Session.get().getMetaData(PluginBrowsePage.CURRENT_PLUGIN);
        return plugin;
    }

    private String getPluginName()
    {
        PlaceholderPlugin plugin = getPlugin();
        return plugin != null ? plugin.getName() : "No plugin selected";
    }

    private String getPluginAuthor()
    {
        PlaceholderPlugin plugin = getPlugin();
        return plugin != null ? plugin.getAuthor() : "";
    }
    
    private String getPluginVersion()
    {
        PlaceholderPlugin plugin = getPlugin();
        return plugin != null ? plugin.getVersion() : "";
    }
    
    private String getPluginLicense()
    {
        PlaceholderPlugin plugin = getPlugin();
        return plugin != null ? plugin.getLicense() : "";
    }
    
    private String getPluginDescription()
    {
        PlaceholderPlugin plugin = getPlugin();
        return plugin != null ? plugin.getDescription() : "";
    }

}
