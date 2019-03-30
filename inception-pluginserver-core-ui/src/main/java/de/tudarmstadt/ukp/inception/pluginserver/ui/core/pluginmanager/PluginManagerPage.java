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
import static de.tudarmstadt.ukp.clarin.webanno.support.lambda.LambdaBehavior.visibleWhen;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.feedback.IFeedback;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.UrlTextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.StringValidator;
import org.apache.wicket.validation.validator.UrlValidator;

import org.wicketstuff.annotation.mount.MountPath;

import de.tudarmstadt.ukp.clarin.webanno.security.UserDao;
import de.tudarmstadt.ukp.clarin.webanno.security.model.User;
import de.tudarmstadt.ukp.clarin.webanno.support.lambda.LambdaAjaxButton;
import de.tudarmstadt.ukp.clarin.webanno.support.lambda.LambdaAjaxLink;
import de.tudarmstadt.ukp.clarin.webanno.support.wicket.ModelChangedVisitor;
import de.tudarmstadt.ukp.clarin.webanno.ui.core.login.LoginPage;
import de.tudarmstadt.ukp.clarin.webanno.ui.core.page.ApplicationPageBase;
import de.tudarmstadt.ukp.inception.pluginserver.core.plugindb.Plugin;
import de.tudarmstadt.ukp.inception.pluginserver.core.plugindb.PluginVersion;
import de.tudarmstadt.ukp.inception.pluginserver.core.plugindb.dao.PluginDao;
import de.tudarmstadt.ukp.inception.pluginserver.core.plugindb.dao.PluginVersionDao;

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

    private @SpringBean UserDao userRepo;
    protected @SpringBean PluginDao pluginRepo;
    private @SpringBean PluginVersionDao versionRepo;

    protected PluginPanel plugins;

    protected PluginDetailForm pluginDetails;

    private IModel<Plugin> selectedPlugin;

    private VersionPanel versions;

    private IModel<PluginVersion> selectedVersion;

    /**
     * This form displays a plugin version's metadata and allows a developer or administrator to
     * change it.
     */
    class PluginDetailForm
        extends Form<PluginVersion>
    {

        private static final long serialVersionUID = -4995450803719326124L;
        private boolean isCreatePlugin;
        private boolean isCreateVersion;

        private IModel<FileUpload> fileUpload;

        public PluginDetailForm(String id, IModel<PluginVersion> aModel)
        {
            super(id, new CompoundPropertyModel<>(aModel));

            setOutputMarkupId(true);
            setOutputMarkupPlaceholderTag(true);
            setMultiPart(true);

            StringValidator lengthValidator = StringValidator.maximumLength(255);
            UrlValidator urlValidator = new UrlValidator();

            add(new TextField<String>("name").setRequired(true).add(lengthValidator));

            add(new TextField<String>("versionNumber").setRequired(true).add(lengthValidator));

            add(new TextField<String>("author").setRequired(true).add(lengthValidator));

            add(new TextArea<String>("description"));

            add(new TextField<String>("license")
            {
                private static final long serialVersionUID = 4374974281338899190L;

                @Override
                public boolean isRequired()
                {
                    return isCreateVersion;
                }
            }.add(lengthValidator).add(enabledWhen(() -> isCreateVersion)));

            add(new UrlTextField("projectPage", PropertyModel.of(aModel, "projectPage"))
                    .add(lengthValidator).add(urlValidator));

            add(new UrlTextField("docPage", PropertyModel.of(aModel, "docPage"))
                    .add(lengthValidator).add(urlValidator));

            add(new Label("uploadTime"));

            add(new Label("updateTime"));

            FileUploadField uploadField = new FileUploadField("fileUpload", Model.of())
            {
                private static final long serialVersionUID = -9006880767490743569L;

                @Override
                public boolean isRequired()
                {
                    return aModel.getObject().getFile() == null;
                }
            };
            uploadField.add(this::validateJarUpload);
            add(uploadField);
            fileUpload = uploadField::getFileUpload;

            add(new TextField<String>("fileName").add(StringValidator.maximumLength(100)));

            add(new LambdaAjaxButton<>("withdraw", PluginManagerPage.this::actionWithdraw)
                    .add(visibleWhen(() -> !isCreateVersion && getModelObject().isEnabled())));

            add(new LambdaAjaxButton<>("publish", PluginManagerPage.this::actionPublish)
                    .add(visibleWhen(() -> !isCreateVersion && !getModelObject().isEnabled())));

            add(new LambdaAjaxButton<>("save", PluginManagerPage.this::actionSave));

            add(new LambdaAjaxLink("cancel", PluginManagerPage.this::actionCancel));
        }

        @Override
        protected void onConfigure()
        {
            super.onConfigure();

            setVisible(getModelObject() != null);
        }

        private void validateJarUpload(IValidatable<List<FileUpload>> x)
        {
            if (x.getValue().size() > 1) {
                x.error(new ValidationError("Only one archive file can be uploaded."));
            }
            else {
                FileUpload upload = x.getValue().get(0);
                if (upload.getSize() > PluginVersion.MAX_FILE_SIZE) {
                    x.error(new ValidationError("The uploaded file is too large - maximum size is "
                            + Bytes.bytes(PluginVersion.MAX_FILE_SIZE) + "."));
                }
                try {
                    InputStream stream = upload.getInputStream();

                    byte[] fileHeader = stream.readNBytes(4);
                    if (!Arrays.equals(fileHeader, new byte[] { 0x50, 0x4b, 0x03, 0x04 })) {
                        // this only checks if the file is a non-empty non-split ZIP file,
                        // so this is not a guarantee that the file contains a valid plugin
                        StringBuilder actualHeader = new StringBuilder();
                        for (byte b : fileHeader) {
                            actualHeader.append(' ').append(Integer.toString(b));
                        }
                        x.error(new ValidationError("The uploaded file is not a JAR file: header is"
                                + actualHeader.append('.')));
                    }
                }
                catch (IOException e) {
                    x.error(new ValidationError("Error reading the uploaded file."));
                    e.printStackTrace();
                }
            }

        }

    }

    public PluginManagerPage()
    {
        setStatelessHint(true);
        setVersioned(false);

        // In case we restore a saved session, make sure the user actually still exists in the DB.
        // redirect to login page (if no user is found, admin/admin will be created)
        User user = userRepo.getCurrentUser();
        if (user == null) {
            setResponsePage(LoginPage.class);
        }

        selectedPlugin = Model.of();
        selectedVersion = Model.of();
        plugins = makePluginPanel("plugins", selectedPlugin, applicablePlugins());
        versions = new VersionPanel("versions", selectedPlugin, selectedVersion);

        plugins.setCreateAction(_target -> {
            selectedPlugin.setObject(new Plugin());
            selectedVersion.setObject(null);
            _target.add(plugins, versions, pluginDetails);
            // Need to defer setting this field because otherwise setChangeAction below
            // sets it back to false.
            _target.registerRespondListener(__target -> pluginDetails.isCreatePlugin = true);
        });

        plugins.setChangeAction(_target -> {
            versions.visitChildren(new ModelChangedVisitor(selectedPlugin));
            pluginDetails.isCreatePlugin = false;
            pluginDetails.visitChildren(new ModelChangedVisitor(selectedVersion));
            _target.add(versions);
            _target.add(pluginDetails);
        });

        versions.setCreateAction(_target -> {
            selectedVersion.setObject(
                    new PluginVersion(selectedPlugin.getObject(), userRepo.getCurrentUser()));
            _target.add(versions, pluginDetails);
            // Need to defer setting this field because otherwise setChangeAction below
            // sets it back to false.
            _target.registerRespondListener(__target -> pluginDetails.isCreateVersion = true);
            pluginDetails.configure();
        });

        versions.setChangeAction(_target -> {
            pluginDetails.visitChildren(new ModelChangedVisitor(selectedVersion));
            pluginDetails.isCreateVersion = false;
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
     * @param iModel
     *            A model for the list of plugins to select from
     * @return The PluginPanel for this page
     */
    protected PluginPanel makePluginPanel(String id, IModel<Plugin> pluginModel,
            IModel<List<Plugin>> listModel)
    {
        return new PluginPanel(id, pluginModel, listModel);
    }

    /**
     * This method is called when the save button is clicked. It updates the DB with the values in
     * the form if all of these values are of the correct format (e.g. the URL fields must actually
     * contain URLs).
     * 
     * @param aTarget
     *            The request target
     * @param aForm
     *            ignored - this method always uses the PluginDetailForm on this page
     */
    private void actionSave(AjaxRequestTarget aTarget, Form<PluginVersion> aForm)
    {
        PluginVersion version = pluginDetails.getModelObject();
        Plugin plugin = selectedPlugin.getObject();

        FileUpload upload = pluginDetails.fileUpload.getObject();

        if (upload != null) {
            String clientFileName = upload.getClientFileName();
            if (!version.hasFileName() && clientFileName.length() >= 100
                    && clientFileName.length() != 0) {
                version.setFileName(clientFileName);
            }
            version.setFile(upload.getBytes());
        }

        if (pluginDetails.isCreatePlugin) {

            String id = plugin.makeId(version, pluginRepo);
            plugin.getVersions().add(version);
            version.setPlugin(plugin);

            pluginRepo.createPluginAndVersion(plugin, version);

            info("A new plugin with the ID " + id + " has been created.");
        }
        else if (pluginDetails.isCreateVersion) {
            versionRepo.create(version);
            selectedPlugin.setObject(pluginRepo.update(plugin));

            info("A new plugin version has been created.");
        }
        else {
            pluginDetails.setModelObject(versionRepo.update(version));

            info("Version details have been updated.");
        }

        aTarget.add(plugins, versions, pluginDetails);
        aTarget.addChildren(getPage(), IFeedback.class);
        pluginDetails.configure();
        plugins.configure();
        versions.configure();
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
     * This method is called when the publish or withdraw button is clicked. Clicking one of the
     * button does these things according to which button was clicked:
     * <ul>
     * <li>change the selected plugin version's visibility on the plugin download page</li>
     * <li>change the withdraw button's visibility</li>
     * <li>change the publish button's visibility</li>
     * </ul>
     * 
     * @param aTarget
     *            The request target
     * @param aForm
     *            The PluginDetailForm on this page
     */
    private void actionPublishWithdraw(boolean visible, AjaxRequestTarget aTarget,
            Form<PluginVersion> aForm)
    {
        PluginVersion version = aForm.getModelObject();

        version.setEnabled(visible);

        // The buttons should only be visible when the plugin version exists in the DB
        aForm.setModelObject(versionRepo.updateVisibility(version));

        info("Plugin version has been " + ((visible) ? "published" : "withdrawn") + ".");

        aTarget.add(pluginDetails);
        aTarget.addChildren(getPage(), IFeedback.class);
        pluginDetails.configure();
    }

    /**
     * This method is called when the publish button is clicked. Clicking one of the button does
     * these things:
     * <ul>
     * <li>make the selected plugin version visible on the plugin download page</li>
     * <li>show the withdraw button</li>
     * <li>hide the publish button</li>
     * </ul>
     * 
     * @param aTarget
     *            The request target
     * @param aForm
     *            The PluginDetailForm on this page
     */
    private void actionPublish(AjaxRequestTarget aTarget, Form<PluginVersion> aForm)
    {
        actionPublishWithdraw(true, aTarget, aForm);
    }

    /**
     * This method is called when the withdraw button is clicked. Clicking one of the button does
     * these things:
     * <ul>
     * <li>make the selected plugin version invisible on the plugin download page</li>
     * <li>hide the withdraw button</li>
     * <li>show the publish button</li>
     * </ul>
     * 
     * @param aTarget
     *            The request target
     * @param aForm
     *            The PluginDetailForm on this page
     */
    private void actionWithdraw(AjaxRequestTarget aTarget, Form<PluginVersion> aForm)
    {
        actionPublishWithdraw(false, aTarget, aForm);
    }

    /**
     * @return A List of all plugins that can be managed by this PluginManagerPage instance.
     */
    protected LoadableDetachableModel<List<Plugin>> applicablePlugins()
    {
        return LoadableDetachableModel
                .of(() -> pluginRepo.getMaintained(userRepo.getCurrentUser()));
    }
}
