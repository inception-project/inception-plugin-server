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

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import de.tudarmstadt.ukp.clarin.webanno.support.lambda.LambdaStatelessLink;
import de.tudarmstadt.ukp.clarin.webanno.ui.core.page.ApplicationPageBase;
import de.tudarmstadt.ukp.inception.pluginserver.core.plugindb.Plugin;
import de.tudarmstadt.ukp.inception.pluginserver.core.plugindb.PluginVersion;
import de.tudarmstadt.ukp.inception.pluginserver.core.plugindb.dao.PluginDao;

/**
 * This page consists of a list of all plugins that can be downloaded from the server.
 * 
 * The list items contain links to the individual {@link PluginPage}s.
 */
@MountPath(value = "/browse.html")
public class PluginBrowsePage
    extends ApplicationPageBase
{

    private static final long serialVersionUID = 1778391157660314718L;
    
    private @SpringBean PluginDao pluginRepository;

    public PluginBrowsePage()
    {
        add(createPluginList());
    }

    private WebMarkupContainer createPluginList()
    {
        ListView<Plugin> listview = new ListView<Plugin>("plugin",
                LoadableDetachableModel.of(() -> pluginRepository.list()))
        {
            private static final long serialVersionUID = 833958274042572812L;

            @Override
            protected void populateItem(ListItem<Plugin> item)
            {
                if (!item.getModelObject().hasEnabledVersion()) {
                    item.setEnabled(false);
                    return;
                }
                LambdaStatelessLink pluginLink = new LambdaStatelessLink("pluginLink", () ->
                    selectPlugin(item.getModelObject()));
                PluginVersion currentVersion = item.getModelObject().newestVersion();
                pluginLink.add(new Label("name", currentVersion.getName()));
                item.add(pluginLink);
                item.add(new Label("version", currentVersion.getVersionNumber()));
            }

        };

        WebMarkupContainer pluginList = new WebMarkupContainer("plugins");
        pluginList.setOutputMarkupPlaceholderTag(true);
        pluginList.add(listview);

        return pluginList;
    }

    private void selectPlugin(Plugin currentPlugin)
    {
        PageParameters params = new PageParameters();
        params.add("plugin", currentPlugin.getId());
        setResponsePage(PluginPage.class, params);
    }

}
