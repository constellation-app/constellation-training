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
package au.gov.asd.tac.constellation.training.exercises.chapter6;

import au.gov.asd.tac.constellation.graph.GraphReadMethods;
import au.gov.asd.tac.constellation.views.qualitycontrol.rules.QualityControlRule;
import org.openide.util.lookup.ServiceProvider;

/**
 * Outbreak Exceeds Population Rule.
 */
@ServiceProvider(service = QualityControlRule.class)
public class OutbreakExceedsPopulationRule extends QualityControlRule {

    public OutbreakExceedsPopulationRule() {
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public int getQuality(final int vertex) {
        return 70;
    }

    @Override
    protected boolean executeRule(final GraphReadMethods graph, final int vertex) {
        // Implement the rule logic to return true if the data integirty issue exists for the supplied vertex
        return false;
    }
}
