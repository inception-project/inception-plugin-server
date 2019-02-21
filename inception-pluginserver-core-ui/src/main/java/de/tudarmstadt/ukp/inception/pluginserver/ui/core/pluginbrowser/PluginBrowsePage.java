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

import org.apache.wicket.MetaDataKey;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.LoadableDetachableModel;
import org.wicketstuff.annotation.mount.MountPath;

import de.tudarmstadt.ukp.clarin.webanno.support.lambda.LambdaStatelessLink;
import de.tudarmstadt.ukp.clarin.webanno.ui.core.page.ApplicationPageBase;
import de.tudarmstadt.ukp.inception.pluginserver.ui.core.pluginmanager.PlaceholderPlugin;
import de.tudarmstadt.ukp.inception.pluginserver.ui.core.pluginmanager.PlaceholderPluginList;

@MountPath(value = "/browse.html")
public class PluginBrowsePage
    extends ApplicationPageBase
{

    private static final long serialVersionUID = 1778391157660314718L;

    public static final MetaDataKey<PlaceholderPlugin> CURRENT_PLUGIN =
            new MetaDataKey<PlaceholderPlugin>()
    {
        private static final long serialVersionUID = 1L;
    };

    public PluginBrowsePage()
    {
        add(createPluginList());
    }

    private WebMarkupContainer createPluginList()
    {
        ListView<PlaceholderPlugin> listview = new ListView<PlaceholderPlugin>("plugin",
                LoadableDetachableModel.of(PlaceholderPluginList::allPlugins))
        {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<PlaceholderPlugin> item)
            {
                if (!item.getModelObject().isEnabled()) {
                    return;
                }
                LambdaStatelessLink pluginLink = new LambdaStatelessLink("pluginLink", () ->
                    selectPlugin(item.getModelObject()));
                pluginLink.add(new Label("name", item.getModelObject().getName()));
                item.add(pluginLink);
                item.add(new Label("version", item.getModelObject().getVersion()));
            }

        };

        WebMarkupContainer pluginList = new WebMarkupContainer("plugins");
        pluginList.setOutputMarkupPlaceholderTag(true);
        pluginList.add(listview);

        return pluginList;
    }

    protected void selectPlugin(PlaceholderPlugin currentPlugin)
    {
        Session.get().setMetaData(CURRENT_PLUGIN, currentPlugin);
        setResponsePage(PluginPage.class);
    }

}
