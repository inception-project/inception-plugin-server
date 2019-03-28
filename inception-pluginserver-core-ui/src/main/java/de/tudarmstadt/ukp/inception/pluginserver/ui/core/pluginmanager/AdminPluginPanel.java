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
package de.tudarmstadt.ukp.inception.pluginserver.ui.core.pluginmanager;

import java.util.List;
import java.util.function.Supplier;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;

import de.tudarmstadt.ukp.clarin.webanno.support.lambda.LambdaAjaxLink;
import de.tudarmstadt.ukp.inception.pluginserver.core.plugindb.Plugin;

/**
 * This is a PluginPanel that includes a button to permanently remove a plugin from the database.
 */
public class AdminPluginPanel
    extends PluginPanel
{

    private static final long serialVersionUID = 3663159670526519125L;

    /**
     * @param id
     *            The non-null id of this component
     * @param model
     *            The model of the plugin whose versions can be selected in a VersionPanel
     * @param plugins
     *            A Supplier of a List of all plugins that the panel is supposed to list
     */
    public AdminPluginPanel(String id, IModel<Plugin> model,
            Supplier<List<Plugin>> plugins)
    {
        super(id, model, plugins);
        add(new LambdaAjaxLink("removePlugin", this::actionRemovePlugin));
    }

    /**
     * This method is called when the "Remove Plugin" button is clicked. It is supposed to do these
     * things:
     * <ul>
     * <li>display an "Are you sure?" message - do nothing if the removal is cancelled</li>
     * <li>permanently remove the selected plugin and all of its versions from the database</li>
     * <li>unselect the selected plugin and version</li>
     * <li>refresh the PluginDetailForm, the VersionPanel and the AdminPluginPanel</li>
     * </ul>
     * 
     * @param target
     *            The request target
     */
    private void actionRemovePlugin(AjaxRequestTarget target)
    {
        // TODO Remove all versins of the selected plugin from the server
    }

}
