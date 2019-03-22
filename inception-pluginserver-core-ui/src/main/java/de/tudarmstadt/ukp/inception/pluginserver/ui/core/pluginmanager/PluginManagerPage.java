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

import static de.tudarmstadt.ukp.clarin.webanno.support.lambda.LambdaBehavior.enabledWhen;

import java.util.List;
import java.util.function.Supplier;

import org.apache.wicket.ajax.AjaxRequestTarget;
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

    class PluginDetailForm
        extends Form<PlaceholderPlugin>
    {

        private static final long serialVersionUID = -4995450803719326124L;

        private boolean isCreate;

        public PluginDetailForm(String id, IModel<PlaceholderPlugin> aModel)
        {
            super(id, new CompoundPropertyModel<>(aModel));

            setOutputMarkupId(true);
            setOutputMarkupPlaceholderTag(true);

            add(new TextField<String>("name").add(enabledWhen(() -> isCreate)));

            add(new TextField<String>("version"));

            add(new TextField<String>("author"));

            add(new TextArea<String>("description"));

            add(new TextField<String>("license"));

            add(new UrlTextField("projectPage", PropertyModel.of(this.getModel(), "projectPage")));

            add(new UrlTextField("docPage", PropertyModel.of(this.getModel(), "docPage")));

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
            pluginDetails.isCreate = false;
            pluginDetails.visitChildren(new ModelChangedVisitor(selectedVersion));
            _target.add(pluginDetails);
        });

        add(plugins);
        add(versions);

        pluginDetails = new PluginDetailForm("pluginDetails", selectedVersion);
        add(pluginDetails);
    }

    protected PluginPanel makePluginPanel(String id, IModel<PlaceholderPlugin> model,
            Supplier<List<PlaceholderPlugin>> plugins)
    {
        return new PluginPanel(id, model, plugins);
    }

    public void actionSave(AjaxRequestTarget aTarget, Form<PlaceholderPlugin> aForm)
    {
        info("Plugin details would have been saved if this was the real app.");
    }

    private void actionCancel(AjaxRequestTarget aTarget)
    {
        selectedPlugin.setObject(null);
        selectedVersion.setObject(null);
        aTarget.add(pluginDetails);
        aTarget.add(versions);
        aTarget.add(plugins);
    }

    public void actionWithdraw(AjaxRequestTarget aTarget, Form<PlaceholderPlugin> aForm)
    {
        info("The selected plugin version would have been withdrawn if this was the real app.");
    }

    /**
     * @return menu items corresponding to the plugins that can be managed by this PluginManagerPage
     *         instance.
     */
    protected List<PlaceholderPlugin> applicablePlugins()
    {
        // TODO return all plugins that have been uploaded by the current user
        return PlaceholderPluginList.userPlugins();
    }
}
