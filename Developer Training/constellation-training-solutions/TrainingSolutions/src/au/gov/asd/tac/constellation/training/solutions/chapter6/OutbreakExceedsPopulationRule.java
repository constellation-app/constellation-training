/*
 * Copyright 2010-2024 Australian Signals Directorate
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
package au.gov.asd.tac.constellation.training.solutions.chapter6;

import au.gov.asd.tac.constellation.graph.Graph;
import au.gov.asd.tac.constellation.graph.GraphReadMethods;
import au.gov.asd.tac.constellation.graph.schema.analytic.concept.AnalyticConcept;
import au.gov.asd.tac.constellation.graph.schema.type.SchemaVertexType;
import au.gov.asd.tac.constellation.training.solutions.Outbreak;
import au.gov.asd.tac.constellation.training.solutions.chapter3.PandemicConcept;
import au.gov.asd.tac.constellation.views.qualitycontrol.rules.QualityControlRule;
import java.util.HashMap;
import java.util.Map;
import org.openide.util.lookup.ServiceProvider;

/**
 * Outbreak Exceeds Population Rule.
 */
@ServiceProvider(service = QualityControlRule.class)
public class OutbreakExceedsPopulationRule extends QualityControlRule {

    private static final String NAME = "Outbreak Exceeds Population";
    private static final String DESCRIPTION
            = "This rule flags city nodes where the number of people infected by a single disease exceeds their population";

    private final Map<Integer, Integer> risks = new HashMap<>();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public int getQuality(final int vertex) {
        return risks.get(vertex);
    }

    @Override
    protected boolean executeRule(final GraphReadMethods graph, final int vertex) {
        final int typeAttr = AnalyticConcept.VertexAttribute.TYPE.get(graph);
        final int outbreakAttr = PandemicConcept.VertexAttribute.OUTBREAK.get(graph);
        final int populationAttr = PandemicConcept.VertexAttribute.POPULATION.get(graph);
        if (typeAttr != Graph.NOT_FOUND && outbreakAttr != Graph.NOT_FOUND && populationAttr != Graph.NOT_FOUND) {
            final SchemaVertexType type = graph.getObjectValue(typeAttr, vertex);
            final Outbreak outbreak = graph.getObjectValue(outbreakAttr, vertex);
            final int population = graph.getIntValue(populationAttr, vertex);
            if (type != null && type.equals(PandemicConcept.VertexType.CITY) && outbreak != null) {
                for (final int numInfected : outbreak.getOutbreakData().values()) {
                    if (numInfected > population) {
                        risks.put(vertex, (int) Math.min(100, Math.max((50 * numInfected) / population, 1)));
                        return true;
                    }
                }
                return false;
            }
            return false;
        }
        return false;
    }
}
