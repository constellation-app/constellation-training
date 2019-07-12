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
package au.gov.asd.tac.constellation.training.solutions.chapter6;

import au.gov.asd.tac.constellation.graph.attribute.interaction.ValueValidator;
import au.gov.asd.tac.constellation.training.solutions.Outbreak;
import au.gov.asd.tac.constellation.training.solutions.chapter3.OutbreakAttributeDescription;
import au.gov.asd.tac.constellation.views.attributeeditor.editors.AbstractEditorFactory.AbstractEditor;
import au.gov.asd.tac.constellation.views.attributeeditor.editors.AttributeValueEditorFactory;
import au.gov.asd.tac.constellation.views.attributeeditor.editors.operations.DefaultGetter;
import au.gov.asd.tac.constellation.views.attributeeditor.editors.operations.EditOperation;
import au.gov.asd.tac.constellation.visual.icons.UserInterfaceIconProvider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.openide.util.lookup.ServiceProvider;

/**
 * Outbreak Editor Factory.
 */
@ServiceProvider(service = AttributeValueEditorFactory.class)
public class OutbreakEditorFactory extends AttributeValueEditorFactory<Outbreak> {

    @Override
    public AbstractEditor<Outbreak> createEditor(final EditOperation editOperation, final DefaultGetter<Outbreak> defaultGetter, final ValueValidator<Outbreak> validator, final String editedItemName, final Outbreak initialValue) {
        return new OutbreakEditor(editOperation, defaultGetter, validator, editedItemName, initialValue);
    }

    @Override
    public String getAttributeType() {
        return OutbreakAttributeDescription.ATTRIBUTE_NAME;
    }

    public class OutbreakEditor extends AbstractEditor<Outbreak> {

        private final List<DiseaseEntry> diseases = new ArrayList<>();
        private final VBox diseaseEntries = new VBox(5);
        private final CheckBox noValueCheckBox = new CheckBox(NO_VALUE_LABEL);

        protected OutbreakEditor(final EditOperation editOperation, final DefaultGetter defaultGetter, final ValueValidator<Outbreak> validator, final String editedItemName, final Outbreak initialValue) {
            super(editOperation, defaultGetter, validator, editedItemName, initialValue);
        }

        @Override
        protected Outbreak getValueFromControls() throws ControlsInvalidException {
            final Map<String, Integer> data = new HashMap<>();
            try {
                diseases.forEach(disease -> {
                    if (!disease.diseaseName.getText().isEmpty()) {
                        data.put(disease.diseaseName.getText(), Integer.parseInt(disease.numberInfected.getText()));
                    }
                });
            } catch (NumberFormatException ex) {
                throw new ControlsInvalidException("Non integer value entered for number infected");
            }
            return noValueCheckBox.isSelected() ? null : new Outbreak(data);
        }

        @Override
        protected void updateControlsWithValue(final Outbreak value) {
            diseases.forEach(disease -> {
                disease.remove();
            });
            if (value != null) {
                value.getOutbreakData().forEach((name, number) -> {
                    new DiseaseEntry(diseases, diseaseEntries, name, number);
                });
            }
            noValueCheckBox.setSelected(value == null);
        }

        @Override
        protected Node createEditorControls() {
            final HBox diseaseLabels = new HBox(85);
            diseaseLabels.getChildren().addAll(new Label("Disease Name"), new Label("Number Infected"));
            diseaseEntries.setPadding(new Insets(5));
            diseaseEntries.getChildren().add(diseaseLabels);

            final ScrollPane diseasesScrollPane = new ScrollPane();
            diseasesScrollPane.setPrefHeight(200);
            diseasesScrollPane.setPrefWidth(300);
            diseasesScrollPane.setContent(diseaseEntries);

            final Button addButton = new Button("", new ImageView(UserInterfaceIconProvider.ADD.buildImage(16)));
            addButton.setOnAction(event -> {
                new DiseaseEntry(diseases, diseaseEntries, "", 0);
            });
            final Label addButtonLabel = new Label("Add Disease");
            final FlowPane addPane = new FlowPane();
            addPane.setHgap(10);
            addPane.setAlignment(Pos.CENTER_RIGHT);
            addPane.getChildren().addAll(addButtonLabel, addButton);

            noValueCheckBox.selectedProperty().addListener((o, n, v) -> {
                addButton.setDisable(noValueCheckBox.isSelected());
                diseaseEntries.setDisable(noValueCheckBox.isSelected());
                update();
            });

            final VBox controls = new VBox(10);
            controls.setPrefWidth(300);
            controls.getChildren().addAll(diseasesScrollPane, addPane, noValueCheckBox);

            return controls;
        }

        private class DiseaseEntry {

            final TextField diseaseName;
            final TextField numberInfected;
            final HBox entry;
            final List<DiseaseEntry> host;
            final Pane visualHost;

            DiseaseEntry(final List<DiseaseEntry> host, final Pane visualHost, final String disease, final int numInfected) {
                diseaseName = new TextField(disease);
                diseaseName.setPrefWidth(150);
                diseaseName.textProperty().addListener((o, n, v) -> {
                    update();
                });

                numberInfected = new TextField(String.valueOf(numInfected));
                numberInfected.setPrefWidth(100);
                numberInfected.textProperty().addListener((o, n, v) -> {
                    update();
                });

                final Button removeButton = new Button("", new ImageView(UserInterfaceIconProvider.CROSS.buildImage(8)));
                removeButton.setOnAction(e -> {
                    remove();
                    update();
                });

                entry = new HBox(10);
                entry.getChildren().addAll(diseaseName, numberInfected, removeButton);

                this.host = host;
                this.host.add(this);
                this.visualHost = visualHost;
                this.visualHost.getChildren().add(entry);
            }

            private void remove() {
                host.remove(this);
                visualHost.getChildren().remove(entry);
            }
        }
    }
}
