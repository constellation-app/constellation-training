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
import au.gov.asd.tac.constellation.graph.GraphElementType;
import au.gov.asd.tac.constellation.graph.GraphWriteMethods;
import au.gov.asd.tac.constellation.graph.ReadableGraph;
import au.gov.asd.tac.constellation.graph.attribute.FloatAttributeDescription;
import au.gov.asd.tac.constellation.graph.schema.analytic.concept.AnalyticConcept;
import au.gov.asd.tac.constellation.graph.schema.visual.concept.VisualConcept;
import au.gov.asd.tac.constellation.plugins.Plugin;
import au.gov.asd.tac.constellation.plugins.PluginException;
import au.gov.asd.tac.constellation.plugins.PluginInfo;
import au.gov.asd.tac.constellation.plugins.PluginInteraction;
import au.gov.asd.tac.constellation.plugins.PluginType;
import au.gov.asd.tac.constellation.plugins.parameters.PluginParameter;
import au.gov.asd.tac.constellation.plugins.parameters.PluginParameters;
import au.gov.asd.tac.constellation.plugins.parameters.types.SingleChoiceParameterType;
import au.gov.asd.tac.constellation.plugins.parameters.types.SingleChoiceParameterType.SingleChoiceParameterValue;
import au.gov.asd.tac.constellation.plugins.templates.SimpleEditPlugin;
import au.gov.asd.tac.constellation.training.solutions.Outbreak;
import au.gov.asd.tac.constellation.training.solutions.chapter3.PandemicConcept;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 * Percentage Afflicted Plugin.
 */
@ServiceProvider(service = Plugin.class)
@NbBundle.Messages("PercentageAfflictedPlugin=Percentage Afflicted by Disease")
@PluginInfo(pluginType = PluginType.NONE, tags = {"OUTBREAK"})
public class PercentageAfflictedPlugin extends SimpleEditPlugin {

    public static final String DISEASE_PARAMETER_ID = PluginParameter.buildId(PercentageAfflictedPlugin.class, "disease");
    private static final String DISEASE_PARAMETER_LABEL = "Disease";

    @Override
    public PluginParameters createParameters() {
        final PluginParameters parameters = new PluginParameters();
        final PluginParameter<SingleChoiceParameterValue> diseaseParameter = SingleChoiceParameterType.build(DISEASE_PARAMETER_ID);
        diseaseParameter.setName(DISEASE_PARAMETER_LABEL);
        parameters.addParameter(diseaseParameter);
        return parameters;
    }

    @Override
    public void updateParameters(final Graph graph, final PluginParameters parameters) {
        final Set<String> diseases = new HashSet<>();
        final ReadableGraph readableGraph = graph.getReadableGraph();
        try {
            final int outbreakAttributeId = PandemicConcept.VertexAttribute.OUTBREAK.get(readableGraph);
            for (int vertexPosition = 0; vertexPosition < readableGraph.getVertexCount(); vertexPosition++) {
                final int vertexId = readableGraph.getVertex(vertexPosition);
                final Outbreak outbreak = readableGraph.getObjectValue(outbreakAttributeId, vertexId);
                if (outbreak != null) {
                    diseases.addAll(outbreak.getDiseases());
                }
            }
        } finally {
            readableGraph.release();
        }
        SingleChoiceParameterType.setOptions((PluginParameter<SingleChoiceParameterType.SingleChoiceParameterValue>) parameters.getParameters().get(DISEASE_PARAMETER_ID), new ArrayList<>(diseases));
    }

    @Override
    protected void edit(final GraphWriteMethods graph, final PluginInteraction interaction, final PluginParameters parameters) throws InterruptedException, PluginException {
        final String diseaseName = parameters.getStringValue(DISEASE_PARAMETER_ID);
        final int outbreakAttributeId = PandemicConcept.VertexAttribute.OUTBREAK.get(graph);
        final int typeAttributeId = AnalyticConcept.VertexAttribute.TYPE.get(graph);
        final int populationAttributeId = PandemicConcept.VertexAttribute.POPULATION.get(graph);
        final int nodeRadiusAttributeId = VisualConcept.VertexAttribute.NODE_RADIUS.get(graph);
        if (outbreakAttributeId != Graph.NOT_FOUND && typeAttributeId != Graph.NOT_FOUND && populationAttributeId != Graph.NOT_FOUND && nodeRadiusAttributeId != Graph.NOT_FOUND) {
            final String percentageAfflictedAttributeName = "Percentage Afflicted with " + diseaseName;
            final int percentageAfflictedAttributeId = graph.addAttribute(GraphElementType.VERTEX, FloatAttributeDescription.ATTRIBUTE_NAME, percentageAfflictedAttributeName, percentageAfflictedAttributeName, 0, null);
            for (int vertexPosition = 0; vertexPosition < graph.getVertexCount(); vertexPosition++) {
                final int vertexId = graph.getVertex(vertexPosition);
                if (graph.getObjectValue(typeAttributeId, vertexId).equals(PandemicConcept.VertexType.CITY)) {
                    final Outbreak outbreak = graph.getObjectValue(outbreakAttributeId, vertexId);
                    if (outbreak != null && outbreak.getDiseases().contains(diseaseName)) {
                        final int population = graph.getIntValue(populationAttributeId, vertexId);
                        final float percentageAfflicted = (100f * outbreak.getAffectedPopulation(diseaseName)) / population;
                        graph.setFloatValue(percentageAfflictedAttributeId, vertexId, percentageAfflicted);
                        graph.setFloatValue(nodeRadiusAttributeId, vertexId, Math.min(10, Math.max(1, percentageAfflicted / 10)));
                    }
                }
            }
        }
    }
}
