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
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import org.springframework.util.comparator.Comparators;

import org.wicketstuff.annotation.mount.MountPath;

import de.tudarmstadt.ukp.clarin.webanno.ui.core.menu.MenuItem;
import de.tudarmstadt.ukp.clarin.webanno.ui.core.menu.MenuItemRegistry;
import de.tudarmstadt.ukp.clarin.webanno.ui.core.page.ApplicationPageBase;
import de.tudarmstadt.ukp.inception.pluginserver.core.plugindb.Plugin;
import de.tudarmstadt.ukp.inception.pluginserver.core.plugindb.PluginVersion;
import de.tudarmstadt.ukp.inception.pluginserver.core.plugindb.dao.PluginDao;
import de.tudarmstadt.ukp.inception.pluginserver.ui.core.dashboard.DashboardMenu;

@MountPath(value = "/plugin.html")
public class PluginPage
    extends ApplicationPageBase
{

    private @SpringBean MenuItemRegistry menuItemService;
    private @SpringBean PluginDao pluginRepository;

    private Plugin plugin;
    private DashboardMenu menu;

    public PluginPage(PageParameters params)
    {
        this.plugin = params != null ? getPluginByID(params.get("plugin")) : null;

        if (plugin == null || !plugin.hasEnabledVersion()) {
            setResponsePage(PluginBrowsePage.class);
        }

        menu = new DashboardMenu("menu", LoadableDetachableModel.of(this::getMenuItems));
        add(menu);
        
        RepeatingView versions = new RepeatingView("versions");
        
        for (PluginVersion v : versionList(plugin.getVersions())) {
            versions.add(new PluginPagePanel(versions.newChildId(), v));
        }
        add(versions);

    }

    private List<PluginVersion> versionList(Set<PluginVersion> versions)
    {
        return versions.stream()
                .filter(PluginVersion::isEnabled)
                .sorted(Comparators.comparable().reversed())
                .collect(Collectors.toList());
    }

    private Plugin getPluginByID(StringValue id)
    {
        return pluginRepository.get(id.toString());
    }

    private List<MenuItem> getMenuItems()
    {
        return menuItemService.getMenuItems().stream()
                .filter(item -> item.getPath().matches("/pluginpage/[^/]+|/browsepage"))
                .collect(Collectors.toList());

    }

    private static final long serialVersionUID = 2090986921013118927L;

}
