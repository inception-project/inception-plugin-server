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
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.UrlTextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import de.tudarmstadt.ukp.clarin.webanno.security.UserDao;
import de.tudarmstadt.ukp.clarin.webanno.security.model.User;
import de.tudarmstadt.ukp.clarin.webanno.support.lambda.LambdaAjaxButton;
import de.tudarmstadt.ukp.clarin.webanno.support.lambda.LambdaAjaxLink;
import de.tudarmstadt.ukp.clarin.webanno.support.wicket.ModelChangedVisitor;
import de.tudarmstadt.ukp.clarin.webanno.ui.core.login.LoginPage;
import de.tudarmstadt.ukp.clarin.webanno.ui.core.page.ApplicationPageBase;

/**
 * This page allows a registered user to manage their own plugins. From here, the user can
 * <ol>
 * <li>create a new plugin</li>
 * <li>upload a new version of an existing plugin</li>
 * <li>withdraw a plugin version</li>
 * <li>(re-)publish a plugin version that was withdrawn or newly uploaded</li>
 * <li>change a particular plugin version's metadata</li>
 * </ol>
 */
@MountPath(value = "/pluginmgr/user.html")
public class PluginManagerPage
    extends ApplicationPageBase
{

    private static final long serialVersionUID = -7182183739204537244L;

    private @SpringBean UserDao userRepository;

    protected PluginPanel plugins;

    protected PluginDetailForm pluginDetails;

    private IModel<PlaceholderPlugin> selectedPlugin;

    private VersionPanel versions;

    private IModel<PlaceholderPlugin> selectedVersion;

    /**
     * This form displays a plugin version's metadata
     * and allows a developer or administrator to change it.
     */
    class PluginDetailForm
        extends Form<PlaceholderPlugin>
    {

        private static final long serialVersionUID = -4995450803719326124L;

        public PluginDetailForm(String id, IModel<PlaceholderPlugin> aModel)
        {
            super(id, new CompoundPropertyModel<>(aModel));

            setOutputMarkupId(true);
            setOutputMarkupPlaceholderTag(true);

            add(new TextField<String>("name"));

            add(new TextField<String>("version"));

            add(new TextField<String>("author"));

            add(new TextArea<String>("description"));

            add(new TextField<String>("license"));

            add(new UrlTextField("projectPage", PropertyModel.of(this.getModel(), "projectPage")));

            add(new UrlTextField("docPage", PropertyModel.of(this.getModel(), "docPage")));
            
            add(new Label("uploadTime", this.getModel().map(x -> x.getUploadTime())));

            add(new LambdaAjaxButton<>("withdraw", PluginManagerPage.this::actionWithdraw));

            add(new LambdaAjaxButton<>("save", PluginManagerPage.this::actionSave));

            add(new LambdaAjaxLink("cancel", PluginManagerPage.this::actionCancel));

        }

        @Override
        protected void onConfigure()
        {
            super.onConfigure();

            setVisible(getModelObject() != null);
        }

    }

    public PluginManagerPage()
    {
        setStatelessHint(true);
        setVersioned(false);

        // In case we restore a saved session, make sure the user actually still exists in the DB.
        // redirect to login page (if no user is found, admin/admin will be created)
        User user = userRepository.getCurrentUser();
        if (user == null) {
            setResponsePage(LoginPage.class);
        }

        selectedPlugin = Model.of();
        selectedVersion = Model.of();
        plugins = makePluginPanel("plugins", selectedPlugin, () -> applicablePlugins());
        versions = new VersionPanel("versions", selectedPlugin, selectedVersion);

        plugins.setCreateAction(_target -> {
            // left empty for now
        });

        plugins.setChangeAction(_target -> {
            versions.visitChildren(new ModelChangedVisitor(selectedPlugin));
            pluginDetails.visitChildren(new ModelChangedVisitor(selectedVersion));
            _target.add(versions);
            _target.add(pluginDetails);
        });

        versions.setCreateAction(_target -> {
            // left empty for now
        });

        versions.setChangeAction(_target -> {
            pluginDetails.visitChildren(new ModelChangedVisitor(selectedVersion));
            _target.add(pluginDetails);
        });

        add(plugins);
        add(versions);

        pluginDetails = new PluginDetailForm("pluginDetails", selectedVersion);
        add(pluginDetails);
    }

    /**
     * This method is called in the constructor and can be overwritten by a child class if a
     * different PluginPanel should be used.
     * 
     * @param id
     *            The non-null id of this component
     * @param model
     *            The model for the selected plugin
     * @param plugins
     *            A Supplier for the list of plugins to select from
     * @return The PluginPanel for this page
     */
    protected PluginPanel makePluginPanel(String id, IModel<PlaceholderPlugin> model,
            Supplier<List<PlaceholderPlugin>> plugins)
    {
        return new PluginPanel(id, model, plugins);
    }

    /**
     * This method is called when the save button is clicked. It is supposed to update the version
     * metadata in the DB with the values in the form if all of these values are of the correct
     * format (e.g. the URL fields must actually contain URLs).
     * 
     * @param aTarget
     *            The request target
     * @param aForm
     *            The PluginDetailForm on this page
     */
    public void actionSave(AjaxRequestTarget aTarget, Form<PlaceholderPlugin> aForm)
    {
        info("Plugin details would have been saved if this was the real app.");
    }

    /**
     * This method is called when the cancel button is clicked. It unselects the currently selected
     * plugin and version and thus hides everything but the PluginPanel.
     * 
     * @param aTarget
     *            The request target
     */
    private void actionCancel(AjaxRequestTarget aTarget)
    {
        selectedPlugin.setObject(null);
        selectedVersion.setObject(null);
        aTarget.add(pluginDetails);
        aTarget.add(versions);
        aTarget.add(plugins);
    }

    /**
     * This method is called when the withdraw button is clicked. It is supposed to do these things:
     * <ul>
     * <li>make the selected plugin version invisible on the plugin download page</li>
     * <li>hide the withdraw button</li>
     * <li>show the publish button (not implemented yet)</li>
     * </ul>
     * 
     * @param aTarget
     *            The request target
     * @param aForm
     *            The PluginDetailForm on this page
     */
    public void actionWithdraw(AjaxRequestTarget aTarget, Form<PlaceholderPlugin> aForm)
    {
        info("The selected plugin version would have been withdrawn if this was the real app.");
    }

    /**
     * @return A List of all plugins that can be managed by this PluginManagerPage instance.
     */
    protected List<PlaceholderPlugin> applicablePlugins()
    {
        // TODO return all plugins that have been uploaded by the current user
        return PlaceholderPluginList.userPlugins();
    }
}
