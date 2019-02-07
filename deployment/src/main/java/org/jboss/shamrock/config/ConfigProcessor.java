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
package org.jboss.shamrock.config;

import java.util.Arrays;
import java.util.List;

import org.jboss.shamrock.arc.deployment.AdditionalBeanBuildItem;
import org.jboss.shamrock.deployment.annotations.BuildStep;
import org.jboss.shamrock.deployment.builditem.FeatureBuildItem;
import org.jboss.shamrock.runtime.annotations.ConfigGroup;
import org.jboss.shamrock.runtime.annotations.ConfigItem;
import org.jboss.shamrock.undertow.ServletBuildItem;

/**
 * Provides a servlet which lists all configured properties.
 *
 * @author Harald Pehl
 */
class ConfigProcessor {

    Config config;

    @ConfigGroup
    static final class Config {
        /** The path of the config servlet. */
        @ConfigItem(defaultValue = "/config")
        String path;
    }

    @BuildStep
    ServletBuildItem produceServlet() {
        ServletBuildItem servletBuildItem = new ServletBuildItem("config", ConfigServlet.class.getName());
        servletBuildItem.getMappings().add(config.path);
        return servletBuildItem;
    }

    @BuildStep
    List<AdditionalBeanBuildItem> additionalBeans() {
        return Arrays.asList(
                new AdditionalBeanBuildItem(Config.class),
                new AdditionalBeanBuildItem(ConfigServlet.class));
    }

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem("config");
    }
}
