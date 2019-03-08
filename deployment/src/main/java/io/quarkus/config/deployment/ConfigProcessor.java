/*
 * Copyright 2019 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.quarkus.config.deployment;

import java.util.logging.Logger;

import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.config.runtime.ConfigServlet;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.LaunchModeBuildItem;
import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.quarkus.undertow.deployment.ServletBuildItem;

/**
 * Provides a servlet which lists all configured properties.
 *
 * @author Harald Pehl
 */
public class ConfigProcessor {

    private static final Logger log = Logger.getLogger("io.quarkus.config");

    /**
     * The configuration for config extension.
     */
    ConfigConfig config;

    @ConfigRoot(name = "config")
    static final class ConfigConfig {

        /**
         * The path of the config servlet.
         */
        @ConfigItem(defaultValue = "/config")
        String path;

        /**
         * Whether the servlet is configured in dev mode only.
         */
        @ConfigItem(defaultValue = "true")
        boolean devModeOnly;
    }

    @BuildStep
    public void build(LaunchModeBuildItem launchMode,
            BuildProducer<AdditionalBeanBuildItem> beans,
            BuildProducer<ServletBuildItem> servlets,
            BuildProducer<FeatureBuildItem> feature) {

        boolean useExtension;
        if (launchMode.getLaunchMode().isDevOrTest()) {
            useExtension = true;
        } else {
            useExtension = !config.devModeOnly;
            if (useExtension) {
                log.warning("Config extension was enabled in normal mode! " +
                        "All configuration will be visible at the /" + config.path + " endpoint. " +
                        "If that's not what you want, remove 'quarkus.config.dev-mode-only = false' " +
                        "from your configuration.");
            }
        }

        if (useExtension) {
            beans.produce(new AdditionalBeanBuildItem(ConfigServlet.class));
            servlets.produce(ServletBuildItem.builder("config", ConfigServlet.class.getName())
                    .addMapping(config.path)
                    .build());
            feature.produce(new FeatureBuildItem("config"));
        }
    }
}
