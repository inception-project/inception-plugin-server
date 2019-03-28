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

import java.util.List;
import java.util.function.Supplier;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.wicketstuff.annotation.mount.MountPath;

import de.tudarmstadt.ukp.clarin.webanno.support.lambda.LambdaAjaxButton;
import de.tudarmstadt.ukp.inception.pluginserver.core.plugindb.Plugin;

/**
 * This class is a PluginManagerPage for managing not just the user's own plugins, but all plugins
 * on the server. It adds buttons to permanently remove a plugin or plugin version from the server.
 * These features are intended to be used by administrators.
 */
@MountPath(value = "/pluginmgr/admin.html")
public class AdminPluginManagerPage
    extends PluginManagerPage
{

    private static final long serialVersionUID = -3689156510746843965L;

    public AdminPluginManagerPage()
    {
        super();
        pluginDetails.add(new LambdaAjaxButton<>("removeVersion", this::actionRemoveVersion));
    }

    /**
     * @return A List of all plugins on the server
     */
    @Override
    protected List<Plugin> applicablePlugins()
    {
        return pluginRepository.list();
    }

    /**
     * @return An AdminPluginPanel that includes a button to permanently remove a plugin from the
     *         server
     */
    @Override
    protected PluginPanel makePluginPanel(String id, IModel<Plugin> model,
            Supplier<List<Plugin>> plugins)
    {
        return new AdminPluginPanel(id, model, plugins);
    }

    /**
     * This method is called when the "Remove this version" button is clicked. It is supposed to do
     * these things:
     * <ul>
     * <li>display an "Are you sure?" message - do nothing if the removal is cancelled</li>
     * <li>permanently remove the selected plugin version from the database</li>
     * <li>unselect the selected plugin version</li>
     * <li>refresh the PluginDetailForm and the VersionPanel</li>
     * </ul>
     * 
     * @param aTarget
     *            The request target
     * @param aForm
     *            The PluginDetailForm on this page
     */
    private void actionRemoveVersion(AjaxRequestTarget aTarget, Form<PlaceholderPlugin> aForm)
    {
        // TODO: remove the selected plugin version from the server
    }

}
