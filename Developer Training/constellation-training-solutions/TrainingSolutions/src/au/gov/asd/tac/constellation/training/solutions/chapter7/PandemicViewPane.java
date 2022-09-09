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
package au.gov.asd.tac.constellation.training.solutions.chapter7;

import au.gov.asd.tac.constellation.graph.GraphReadMethods;
import au.gov.asd.tac.constellation.graph.attribute.interaction.AbstractAttributeInteraction;
import au.gov.asd.tac.constellation.graph.manager.GraphManager;
import au.gov.asd.tac.constellation.graph.schema.visual.concept.VisualConcept;
import au.gov.asd.tac.constellation.plugins.PluginExecution;
import au.gov.asd.tac.constellation.training.solutions.Outbreak;
import au.gov.asd.tac.constellation.training.solutions.PandemicPluginRegistry;
import au.gov.asd.tac.constellation.training.solutions.chapter3.OutbreakAttributeDescription;
import au.gov.asd.tac.constellation.training.solutions.chapter3.PandemicConcept;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

/**
 * Pandemic View Pane.
 */
public class PandemicViewPane extends BorderPane {

    private final AbstractAttributeInteraction interaction = AbstractAttributeInteraction.getInteraction(OutbreakAttributeDescription.ATTRIBUTE_NAME);

    private final TextArea summary;
    private final FlowPane options;

    public PandemicViewPane() {
        setPadding(new Insets(5));

        summary = new TextArea();
        setCenter(summary);

        final Button infectButton = new Button("Spread Infection");
        infectButton.setOnAction(event -> {
            PluginExecution
                    .withPlugin(PandemicPluginRegistry.SPREAD_INFECTION)
                    .interactively(true)
                    .executeLater(GraphManager.getDefault().getActiveGraph());
        });

        options = new FlowPane();
        options.setAlignment(Pos.CENTER_RIGHT);
        options.getChildren().add(infectButton);
        setBottom(options);
    }

    public final void refresh(final GraphReadMethods graph) {
        Platform.runLater(() -> {
            final ProgressIndicator progress = new ProgressIndicator();
            progress.setMaxSize(50, 50);
            setCenter(progress);

            final Map<Outbreak, String> outbreaks = new TreeMap<>(Comparator.reverseOrder());
            if (graph != null) {
                final int outbreakAttributeId = PandemicConcept.VertexAttribute.OUTBREAK.get(graph);
                final int identifierAttributeId = VisualConcept.VertexAttribute.IDENTIFIER.get(graph);
                final int vertexCount = graph.getVertexCount();
                for (int vertexPosition = 0; vertexPosition < vertexCount; vertexPosition++) {
                    final int vertexId = graph.getVertex(vertexPosition);
                    final String identifier = graph.getStringValue(identifierAttributeId, vertexId);
                    final Outbreak outbreak = graph.getObjectValue(outbreakAttributeId, vertexId);
                    if (outbreak != null && !outbreak.getOutbreakData().isEmpty()) {
                        outbreaks.put(outbreak, identifier);
                    }
                }
            }

            final StringBuilder summaryText = new StringBuilder("Outbreak Summary:\n\n");

            for (final Map.Entry<Outbreak, String> outbreak : outbreaks.entrySet()) {
                summaryText.append(outbreak.getValue()).append(": ").append(interaction.getDisplayText(outbreak.getKey())).append("\n\n");
            }
            summary.setText(summaryText.toString());
            setCenter(summary);
        });
    }
}
