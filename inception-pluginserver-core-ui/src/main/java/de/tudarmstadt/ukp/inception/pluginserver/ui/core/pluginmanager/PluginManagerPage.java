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

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.tudarmstadt.ukp.clarin.webanno.security.UserDao;
import de.tudarmstadt.ukp.clarin.webanno.security.model.User;
import de.tudarmstadt.ukp.clarin.webanno.support.lambda.LambdaAjaxButton;
import de.tudarmstadt.ukp.clarin.webanno.support.lambda.LambdaAjaxLink;
import de.tudarmstadt.ukp.clarin.webanno.support.wicket.ModelChangedVisitor;
import de.tudarmstadt.ukp.clarin.webanno.ui.core.login.LoginPage;
import de.tudarmstadt.ukp.clarin.webanno.ui.core.page.ApplicationPageBase;

public abstract class PluginManagerPage
    extends ApplicationPageBase
{

    private static final long serialVersionUID = -7182183739204537244L;

    // protected because UserPluginManagerPage will need to know the current user
    protected @SpringBean UserDao userRepository;

    private PluginPanel plugins;

    private PluginDetailForm pluginDetails;

    private IModel<PlaceholderPlugin> selectedPlugin;

    private class PluginDetailForm
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

            add(new TextField<String>("author").add(enabledWhen(() -> isCreate)));

            add(new TextField<String>("description"));

            add(new TextField<String>("license"));

            add(new CheckBox("enabled"));

            add(new ListChoice<String>("versions", Arrays.asList("0.0.1")));

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

        plugins = new PluginPanel("plugins", selectedPlugin, () -> applicablePlugins());

        plugins.setCreateAction(_target -> {
            selectedPlugin.setObject(new PlaceholderPlugin());
            _target.add(plugins);
            _target.add(pluginDetails);
            _target.registerRespondListener(__target -> pluginDetails.isCreate = true);
        });

        plugins.setChangeAction(_target -> {
            pluginDetails.isCreate = false;
            pluginDetails.visitChildren(new ModelChangedVisitor(selectedPlugin));
            _target.add(pluginDetails);
        });

        add(plugins);

        pluginDetails = new PluginDetailForm("pluginDetails", selectedPlugin);
        add(pluginDetails);
    }

    public void actionSave(AjaxRequestTarget aTarget, Form<User> aForm)
    {
        info("Plugin details would have been saved if this was the real app.");
    }

    private void actionCancel(AjaxRequestTarget aTarget)
    {
        selectedPlugin.setObject(null);
        aTarget.add(pluginDetails);
        aTarget.add(plugins);
    }

    /**
     * @return menu items corresponding to the plugins that can be managed by this PluginManagerPage
     *         instance.
     */
    protected abstract List<PlaceholderPlugin> applicablePlugins();
}
