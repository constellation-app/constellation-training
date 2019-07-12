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
package au.gov.asd.tac.constellation.training.solutions.chapter9;

import au.gov.asd.tac.constellation.importexport.geospatial.AbstractGeoExportPlugin;
import au.gov.asd.tac.constellation.pluginframework.Plugin;
import au.gov.asd.tac.constellation.pluginframework.PluginInfo;
import au.gov.asd.tac.constellation.pluginframework.PluginType;
import au.gov.asd.tac.constellation.pluginframework.parameters.PluginParameters;
import au.gov.asd.tac.constellation.views.mapview.utilities.MapExporter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import javafx.stage.FileChooser;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

/**
 * Text Geo Exporter.
 *
 * @author cygnus_x-1
 */
@ServiceProviders({
    @ServiceProvider(service = MapExporter.class)
    ,
    @ServiceProvider(service = Plugin.class)
})
@NbBundle.Messages("TextGeoExporter=Export to Text")
@PluginInfo(pluginType = PluginType.EXPORT, tags = {"EXPORT"})
public class TextGeoExporter extends AbstractGeoExportPlugin {

    @Override
    protected FileChooser.ExtensionFilter getExportType() {
        return new FileChooser.ExtensionFilter("TEXT", "*.txt");
    }

    @Override
    protected void exportGeo(final PluginParameters parameters, final String uuid, final Map<String, String> shapes, final Map<String, Map<String, Object>> attributes, final File output) throws IOException {
        final StringBuilder builder = new StringBuilder();
        for (final String id : shapes.keySet()) {
            final String shape = shapes.get(id);
            builder.append(id).append("=").append(shape).append("\n");
        }
        try (final FileWriter writer = new FileWriter(output)) {
            writer.write(builder.toString());
        }
    }

    @Override
    public String getDisplayName() {
        return "Text";
    }

    @Override
    public String getPluginReference() {
        return this.getClass().getName();
    }
}
