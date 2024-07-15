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
package au.gov.asd.tac.constellation.training.exercises.chapter7;

import au.gov.asd.tac.constellation.graph.GraphReadMethods;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;

/**
 * Pandemic View Pane.
 */
public class PandemicViewPane extends BorderPane {

    public PandemicViewPane() {
        setPadding(new Insets(5));

        // TODO: design the gui of this pane.
    }

    public final void refresh(final GraphReadMethods graph) {
        // TODO: define the behaviour when this pane is refreshed.
    }
}
