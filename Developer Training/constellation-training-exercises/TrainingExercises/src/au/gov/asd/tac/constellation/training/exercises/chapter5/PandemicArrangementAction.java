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
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

/**
 * Pandemic Arrangement Action.
 */
@ActionID(category = "Arrange", id = "au.gov.asd.tac.constellation.training.exercises.chapter5.PandemicArrangementAction")
@ActionRegistration(displayName = "#CTL_PandemicArrangementAction", iconBase = "", surviveFocusChange = true)
@ActionReferences({
    @ActionReference(path = "Menu/Arrange", position = 0)
    ,
    @ActionReference(path = "Toolbars/Arrange", position = 0),})
@Messages("CTL_PandemicArrangementAction=Arrange by Geographic Coordinates")
public class PandemicArrangementAction extends AbstractAction {

    private final GraphNode context;

    public PandemicArrangementAction(final GraphNode context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        // Run the arrangement, followed by a rest view here
    }
}
