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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.feedback.IFeedback;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import de.tudarmstadt.ukp.clarin.webanno.support.lambda.LambdaAjaxButton;
import de.tudarmstadt.ukp.inception.pluginserver.core.plugindb.Plugin;
import de.tudarmstadt.ukp.inception.pluginserver.core.plugindb.PluginVersion;
import de.tudarmstadt.ukp.inception.pluginserver.core.plugindb.dao.PluginDependencyDao;

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

    private @SpringBean PluginDependencyDao dependencyRepo;

    public AdminPluginManagerPage()
    {
        super();
        pluginDetails
                .add(new LambdaAjaxButton<PluginVersion>("removeVersion", this::actionRemoveVersion)
                {
                    private static final long serialVersionUID = 2238594308806271237L;

                    @Override
                    protected void updateAjaxAttributes(AjaxRequestAttributes attributes)
                    {
                        super.updateAjaxAttributes(attributes);

                        AjaxCallListener ajaxCallListener = new AjaxCallListener();
                        ajaxCallListener.onPrecondition(
                                "return confirm('Are you sure you want to delete this version?');");
                        attributes.getAjaxCallListeners().add(ajaxCallListener);
                    }
                });
    }

    /**
     * @return A model of all plugins on the server
     */
    @Override
    protected LoadableDetachableModel<List<Plugin>> applicablePlugins()
    {
        return LoadableDetachableModel.of(pluginRepo::list);
    }

    /**
     * @return An AdminPluginPanel that includes a button to permanently remove a plugin from the
     *         server
     */
    @Override
    protected PluginPanel makePluginPanel(String id, IModel<Plugin> model,
            IModel<List<Plugin>> plugins)
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
     *            ignored - this method always uses the PluginDetailForm on this page
     */
    private void actionRemoveVersion(AjaxRequestTarget aTarget, Form<PluginVersion> aForm)
    {
        PluginVersion version = selectedVersion.getObject();

        if (dependencyRepo.hasNonDependerRelations(version)) {
            info("Could not remove this version because it is the minimum "
                    + "or maximum dependee version in at least one dependency relation.");
        }
        else {
            versionRepo.delete(version);
            selectedVersion.setObject(null);
            info("Removed the selected version from the database.");
            aTarget.add(plugins, versions, pluginDetails);

        }

        aTarget.addChildren(getPage(), IFeedback.class);
    }

}
