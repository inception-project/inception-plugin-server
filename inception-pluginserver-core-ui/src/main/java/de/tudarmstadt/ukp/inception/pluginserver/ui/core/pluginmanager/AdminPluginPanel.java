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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.tudarmstadt.ukp.clarin.webanno.support.lambda.LambdaAjaxLink;
import de.tudarmstadt.ukp.inception.pluginserver.core.plugindb.Plugin;
import de.tudarmstadt.ukp.inception.pluginserver.core.plugindb.dao.PluginDao;
import de.tudarmstadt.ukp.inception.pluginserver.core.plugindb.dao.PluginDependencyDao;

/**
 * This is a PluginPanel that includes a button to permanently remove a plugin from the database.
 */
public class AdminPluginPanel
    extends PluginPanel
{

    private static final long serialVersionUID = 3663159670526519125L;

    private @SpringBean PluginDependencyDao dependencyRepo;
    private @SpringBean PluginDao pluginRepo;

    private IModel<Plugin> selectedPlugin;

    /**
     * @param id
     *            The non-null id of this component
     * @param model
     *            The model of the plugin whose versions can be selected in a VersionPanel
     * @param plugins
     *            A model of a List of all plugins that the panel is supposed to list
     */
    public AdminPluginPanel(String id, IModel<Plugin> selectedPlugin, IModel<List<Plugin>> plugins)
    {
        super(id, selectedPlugin, plugins);
        this.selectedPlugin = selectedPlugin;
        add(new LambdaAjaxLink("removePlugin", this::actionRemovePlugin)
        {
            private static final long serialVersionUID = 2717348634115464793L;

            @Override
            protected void updateAjaxAttributes(AjaxRequestAttributes attributes)
            {
                super.updateAjaxAttributes(attributes);

                AjaxCallListener ajaxCallListener = new AjaxCallListener();
                ajaxCallListener.onPrecondition(
                        "return confirm('Are you sure you want to delete this plugin?');");
                attributes.getAjaxCallListeners().add(ajaxCallListener);
            }
        });
    }

    /**
     * This method is called when the "Remove Plugin" button is clicked. It does these things:
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
        Plugin plugin = selectedPlugin.getObject();

        if (dependencyRepo.hasDependers(plugin)) {
            info("Could not remove this plugin because other plugins depend on it.");
        }
        else {
            pluginRepo.delete(plugin);
            selectedPlugin.setObject(null);
            info("Removed the selected plugin from the database.");
            target.add(this, getParent().get("versions"), getParent().get("pluginDetails"));
        }
    }

}
