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
package au.gov.asd.tac.constellation.training.solutions.chapter5;

import au.gov.asd.tac.constellation.graph.Graph;
import au.gov.asd.tac.constellation.graph.GraphConstants;
import au.gov.asd.tac.constellation.graph.GraphWriteMethods;
import au.gov.asd.tac.constellation.graph.schema.analytic.concept.SpatialConcept;
import au.gov.asd.tac.constellation.graph.schema.visual.concept.VisualConcept;
import au.gov.asd.tac.constellation.plugins.Plugin;
import au.gov.asd.tac.constellation.plugins.PluginException;
import au.gov.asd.tac.constellation.plugins.PluginInfo;
import au.gov.asd.tac.constellation.plugins.PluginInteraction;
import au.gov.asd.tac.constellation.plugins.PluginNotificationLevel;
import au.gov.asd.tac.constellation.plugins.PluginType;
import au.gov.asd.tac.constellation.plugins.parameters.PluginParameters;
import au.gov.asd.tac.constellation.plugins.templates.SimpleEditPlugin;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 * Pandemic Arrangement Plugin.
 */
@ServiceProvider(service = Plugin.class)
@NbBundle.Messages("PandemicArrangementPlugin=Arrange by Geographic Coordinates")
@PluginInfo(pluginType = PluginType.DISPLAY, tags = {"ARRANGEMENT"})
public class PandemicArrangementPlugin extends SimpleEditPlugin {

    @Override
    protected void edit(final GraphWriteMethods writableGraph, final PluginInteraction interaction, final PluginParameters parameters) throws InterruptedException, PluginException {
        interaction.setProgress(0, 0, "Initialising Arrangement...", true);

        final int latitudeAttributeId = SpatialConcept.VertexAttribute.LATITUDE.get(writableGraph);
        final int longitudeAttributeId = SpatialConcept.VertexAttribute.LONGITUDE.get(writableGraph);
        final int yAttributeId = VisualConcept.VertexAttribute.Y.get(writableGraph);
        final int xAttributeId = VisualConcept.VertexAttribute.X.get(writableGraph);

        if (latitudeAttributeId == Graph.NOT_FOUND || longitudeAttributeId == GraphConstants.NOT_FOUND) {
            throw new PluginException(PluginNotificationLevel.ERROR, "Required attributes 'Geo.Latitude' and 'Geo.Longitude' do not exist on this graph!");
        }

        final int vertexCount = writableGraph.getVertexCount();
        for (int vertexPosition = 0; vertexPosition < vertexCount; vertexPosition++) {
            interaction.setProgress(vertexPosition, vertexCount, "Arranging by Geographic Coordinates...", true);

            final int vertexId = writableGraph.getVertex(vertexPosition);

            final float vertexLatitude = writableGraph.getFloatValue(latitudeAttributeId, vertexId);
            final float vertexLongitude = writableGraph.getFloatValue(longitudeAttributeId, vertexId);

            writableGraph.setFloatValue(yAttributeId, vertexId, vertexLatitude);
            writableGraph.setFloatValue(xAttributeId, vertexId, vertexLongitude);
        }

        interaction.setProgress(0, 0, "Finished", true);
    }
}
