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

import au.gov.asd.tac.constellation.graph.GraphElementType;
import au.gov.asd.tac.constellation.graph.GraphWriteMethods;
import au.gov.asd.tac.constellation.graph.schema.attribute.SchemaAttribute;
import au.gov.asd.tac.constellation.graph.schema.visual.concept.VisualConcept;
import au.gov.asd.tac.constellation.plugins.Plugin;
import au.gov.asd.tac.constellation.plugins.PluginException;
import au.gov.asd.tac.constellation.plugins.PluginExecution;
import au.gov.asd.tac.constellation.plugins.PluginInfo;
import au.gov.asd.tac.constellation.plugins.PluginInteraction;
import au.gov.asd.tac.constellation.plugins.parameters.PluginParameters;
import au.gov.asd.tac.constellation.training.solutions.Outbreak;
import au.gov.asd.tac.constellation.training.solutions.PandemicPluginRegistry;
import au.gov.asd.tac.constellation.training.solutions.chapter3.PandemicConcept;
import au.gov.asd.tac.constellation.views.analyticview.analytics.AnalyticInfo;
import au.gov.asd.tac.constellation.views.analyticview.analytics.AnalyticPlugin;
import au.gov.asd.tac.constellation.views.analyticview.results.AnalyticResult;
import au.gov.asd.tac.constellation.views.analyticview.results.ScoreResult;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

/**
 * Outbreak Count Analytic.
 *
 * @author cygnus_x-1
 */
@ServiceProviders({
    @ServiceProvider(service = AnalyticPlugin.class)
    ,
    @ServiceProvider(service = Plugin.class)
})
@NbBundle.Messages("OutbreakCountAnalytic=Outbreak Count Analytic")
@PluginInfo(tags = {"ANALYTIC"})
@AnalyticInfo(analyticCategory = "Count")
public class OutbreakCountAnalytic extends AnalyticPlugin<ScoreResult> {

    protected ScoreResult result;

    @Override
    public Set<SchemaAttribute> getPrerequisiteAttributes() {
        final Set<SchemaAttribute> attributes = new HashSet<>();
        attributes.add(PandemicConcept.VertexAttribute.OUTBREAK);
        return attributes;
    }

    @Override
    protected final void edit(final GraphWriteMethods graph, final PluginInteraction interaction, final PluginParameters parameters) throws InterruptedException, PluginException {
        result = new ScoreResult();

        final int identifierAttributeId = VisualConcept.VertexAttribute.IDENTIFIER.get(graph);
        final int outbreakAttributeId = PandemicConcept.VertexAttribute.OUTBREAK.get(graph);
        final int outbreakCountAttributeId = PandemicConcept.VertexAttribute.OUTBREAK_COUNT.ensure(graph);

        PluginExecution.withPlugin(PandemicPluginRegistry.SPREAD_INFECTION).executeNow(graph);

        final int vertexCount = graph.getVertexCount();
        for (int vertexPosition = 0; vertexPosition < vertexCount; vertexPosition++) {
            final int vertexId = graph.getVertex(vertexPosition);
            final Outbreak vertexOutbreak = graph.getObjectValue(outbreakAttributeId, vertexId);
            if (vertexOutbreak != null) {
                final String identifier = graph.getStringValue(identifierAttributeId, vertexId);
                final float score = vertexOutbreak.getNumberOfDiseases();
                final Map<String, Float> scores = new HashMap<>();
                scores.put(identifier, score);
                final boolean isNull = score == (int) graph.getAttributeDefaultValue(outbreakCountAttributeId);
                result.add(new ScoreResult.ElementScore(GraphElementType.VERTEX, vertexId, identifier, isNull, scores));
            }
        }
    }

    @Override
    public Set getAnalyticAttributes(final PluginParameters parameters) {
        final Set<SchemaAttribute> attributes = new HashSet<>();
        attributes.add(PandemicConcept.VertexAttribute.OUTBREAK_COUNT);
        return attributes;
    }

    @Override
    public ScoreResult getResults() {
        return result;
    }

    @Override
    public Class<? extends AnalyticResult<?>> getResultType() {
        return ScoreResult.class;
    }
}
