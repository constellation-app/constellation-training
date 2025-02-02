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
package au.gov.asd.tac.constellation.training.exercises.chapter5;

import au.gov.asd.tac.constellation.graph.node.GraphNode;
import au.gov.asd.tac.constellation.graph.node.plugins.SimplePluginAction;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle;

/**
 * Percentage Afflicted Action.
 */
@ActionID(category = "Tools", id = "au.gov.asd.tac.constellation.training.exercises.chapter5.PercentageAfflictedAction")
@ActionRegistration(displayName = "#CTL_PercentageAfflictedAction", iconBase = "", surviveFocusChange = true)
@NbBundle.Messages("CTL_PercentageAfflictedAction=Percentrage Afflicted")
public class PercentageAfflictedAction extends SimplePluginAction {

    public PercentageAfflictedAction(final GraphNode context) {
        // Pass the appropriate plugin in here
        super(context, null, true);
    }

}
