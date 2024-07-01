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
package au.gov.asd.tac.constellation.training.exercises.chapter3;

import au.gov.asd.tac.constellation.graph.schema.Schema;
import au.gov.asd.tac.constellation.graph.schema.analytic.AnalyticSchemaFactory;
import au.gov.asd.tac.constellation.graph.schema.concept.SchemaConcept;
import java.util.HashSet;
import java.util.Set;

/**
 * Pandemic Schema Factory.
 *
 * TODO: register this class as a SchemaFactory class.
 *
 * TODO: add an icon to the schema by overriding the getIcon method.
 *
 * TODO: give this schema factory a position which will result in it being the
 * default schema factory.
 */
public class PandemicSchemaFactory extends AnalyticSchemaFactory {

    @Override
    public String getName() {
        // TODO: give this schema factory a unique name.
        return null;
    }

    @Override
    public String getLabel() {
        // TODO: give this schema factory a displayable label.
        return null;
    }

    @Override
    public String getDescription() {
        // TODO: give this schema factory a displayable description.
        return null;
    }

    @Override
    public Set<Class<? extends SchemaConcept>> getRegisteredConcepts() {
        final Set<Class<? extends SchemaConcept>> registeredConcepts = new HashSet<>();
        return registeredConcepts;
    }

    @Override
    public Schema createSchema() {
        // TODO: specify which schema this schema factory will create;
        return null;
    }
}
