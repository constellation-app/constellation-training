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

import au.gov.asd.tac.constellation.views.schemaview.providers.SchemaViewNodeProvider;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import org.openide.util.lookup.ServiceProvider;

/**
 * Pandemic Node Provider.
 *
 * @author cygnus_x-1
 */
@ServiceProvider(service = SchemaViewNodeProvider.class)
public class PandemicNodeProvider implements SchemaViewNodeProvider {

    private static final String INFO = "<Information about pandemics goes here>";

    @Override
    public void setContent(final Tab tab) {
        final VBox node = new VBox();
        final Label pandemicInfo = new Label(INFO);
        node.getChildren().add(pandemicInfo);

        Platform.runLater(() -> {
            tab.setContent(node);
        });
    }

    @Override
    public void discardNode() {
    }

    @Override
    public String getText() {
        return "Pandemic Info";
    }
}
