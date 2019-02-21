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
package de.tudarmstadt.ukp.inception.pluginserver.ui.core.pluginbrowser;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.wicket.Session;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import de.tudarmstadt.ukp.clarin.webanno.ui.core.menu.MenuItem;
import de.tudarmstadt.ukp.clarin.webanno.ui.core.menu.MenuItemRegistry;
import de.tudarmstadt.ukp.clarin.webanno.ui.core.page.ApplicationPageBase;
import de.tudarmstadt.ukp.inception.pluginserver.ui.core.dashboard.DashboardMenu;
import de.tudarmstadt.ukp.inception.pluginserver.ui.core.dashboard.dashlet.CurrentPluginDashlet;
import de.tudarmstadt.ukp.inception.pluginserver.ui.core.pluginmanager.PlaceholderPlugin;

@MountPath(value = "/plugin.html")
public class PluginPage extends ApplicationPageBase
{

    private @SpringBean MenuItemRegistry menuItemService;

    private PlaceholderPlugin plugin;
    private DashboardMenu menu;

    public PluginPage()
    {
        this.plugin = Session.get().getMetaData(PluginBrowsePage.CURRENT_PLUGIN);

        if (plugin == null) {
            setResponsePage(PluginBrowsePage.class);
        }

        menu = new DashboardMenu("menu", LoadableDetachableModel.of(this::getMenuItems));
        add(menu);

        add(new CurrentPluginDashlet("currentPluginDashlet"));

    }

    private List<MenuItem> getMenuItems()
    {
        return menuItemService.getMenuItems().stream()
                .filter(item -> item.getPath().matches("/pluginpage/[^/]+|/browsepage"))
                .collect(Collectors.toList());

    }

    private static final long serialVersionUID = 2090986921013118927L;

}
