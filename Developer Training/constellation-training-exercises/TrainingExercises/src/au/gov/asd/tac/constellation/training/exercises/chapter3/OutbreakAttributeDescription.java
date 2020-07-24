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
package au.gov.asd.tac.constellation.training.exercises.chapter3;

import au.gov.asd.tac.constellation.graph.attribute.AbstractObjectAttributeDescription;
import au.gov.asd.tac.constellation.graph.attribute.AttributeDescription;
import au.gov.asd.tac.constellation.training.exercises.Outbreak;
import org.openide.util.lookup.ServiceProvider;

/**
 * Outbreak Attribute Description.
 */
@ServiceProvider(service = AttributeDescription.class)
public class OutbreakAttributeDescription extends AbstractObjectAttributeDescription {

    public static final String ATTRIBUTE_NAME = "outbreak";
    public static final Class<?> NATIVE_CLASS = Outbreak.class;
    public static final Outbreak DEFAULT_VALUE = null;

    public OutbreakAttributeDescription() {
        super(ATTRIBUTE_NAME, NATIVE_CLASS, DEFAULT_VALUE);
    }

    @Override
    public void setString(final int id, final String value) {
        // Set the attribute value from a string
    }

    @Override
    public String getString(final int id) {
        // Return the attribute value as a string
        return null;
    }
}
