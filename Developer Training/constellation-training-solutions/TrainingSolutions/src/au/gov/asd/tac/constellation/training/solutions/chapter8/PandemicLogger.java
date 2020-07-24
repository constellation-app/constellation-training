/*
 * Copyright 2010-2019 Australian Signals Directorate
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
package au.gov.asd.tac.constellation.training.solutions.chapter8;

import au.gov.asd.tac.constellation.graph.Graph;
import au.gov.asd.tac.constellation.plugins.Plugin;
import au.gov.asd.tac.constellation.plugins.logging.ConstellationLogger;
import au.gov.asd.tac.constellation.plugins.logging.DefaultConstellationLogger;
import au.gov.asd.tac.constellation.plugins.parameters.PluginParameters;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 * Pandemic Logger.
 */
@ServiceProvider(service = ConstellationLogger.class)
public class PandemicLogger extends DefaultConstellationLogger {

    private static final Logger LOGGER = Logger.getLogger(PandemicLogger.class.getName());

    @Override
    public void applicationStarted() {
        LOGGER.info("Application Started...");
    }

    @Override
    public void applicationStopped() {
        LOGGER.info("Application Stopped...");
    }

    @Override
    public void pluginStarted(final Plugin plugin, final PluginParameters pp, final Graph graph) {
        LOGGER.info("Plugin Started...");
    }

    @Override
    public void pluginStopped(Plugin plugin, PluginParameters pp) {
        LOGGER.info("Plugin Stopped...");
    }

    @Override
    public void pluginProperties(Plugin plugin, Properties properties) {
        LOGGER.log(Level.INFO, "Plugin {0} has properties {1}", new Object[]{plugin.getName(), properties});
    }
}
