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

import au.gov.asd.tac.constellation.graph.GraphConstants;
import au.gov.asd.tac.constellation.graph.GraphReadMethods;
import au.gov.asd.tac.constellation.graph.schema.attribute.SchemaAttribute;
import au.gov.asd.tac.constellation.graph.schema.visual.concept.VisualConcept;
import au.gov.asd.tac.constellation.training.solutions.chapter3.PandemicConcept;
import au.gov.asd.tac.constellation.utilities.icon.ConstellationIcon;
import au.gov.asd.tac.constellation.utilities.tooltip.TooltipPane;
import au.gov.asd.tac.constellation.views.conversationview.ConversationContribution;
import au.gov.asd.tac.constellation.views.conversationview.ConversationContributionProvider;
import au.gov.asd.tac.constellation.views.conversationview.ConversationMessage;
import au.gov.asd.tac.constellation.views.conversationview.SelectableLabel;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.openide.util.lookup.ServiceProvider;

/**
 * Icon Contribution Provider.
 *
 * @author cygnus_x-1
 */
@ServiceProvider(service = ConversationContributionProvider.class)
public class IconContributionProvider extends ConversationContributionProvider {

    private final SchemaAttribute iconAttribute = VisualConcept.VertexAttribute.FOREGROUND_ICON;
    private final SchemaAttribute flightAttribute = PandemicConcept.TransactionAttribute.AIRLINE;

    public IconContributionProvider() {
        super("Icons", Integer.MAX_VALUE);
    }

    @Override
    public boolean isCompatibleWithGraph(final GraphReadMethods graph) {
        return iconAttribute.get(graph) != GraphConstants.NOT_FOUND
                && flightAttribute.get(graph) != GraphConstants.NOT_FOUND;
    }

    @Override
    public ConversationContribution createContribution(final GraphReadMethods graph, final ConversationMessage message) {
        final int iconAttributeId = iconAttribute.get(graph);
        final int senderId = message.getSender();
        final ConstellationIcon senderIcon = graph.getObjectValue(iconAttributeId, senderId);

        final int flightAttributeId = flightAttribute.get(graph);
        final int transactionId = message.getTransaction();
        final String flight = graph.getStringValue(flightAttributeId, transactionId);

        return new IconContribution(message, senderIcon, flight);
    }

    private class IconContribution extends ConversationContribution {

        private final ConstellationIcon icon;
        private final String flight;

        public IconContribution(final ConversationMessage message, final ConstellationIcon icon, final String flight) {
            super(IconContributionProvider.this, message);
            this.icon = icon;
            this.flight = flight;
        }

        @Override
        protected Region createContent(final TooltipPane tips) {
            final VBox content = new VBox();
            final ImageView iconImage = new ImageView(icon.buildImage());
            final SelectableLabel iconLabel = new SelectableLabel(flight, true, null, tips, null);
            content.getChildren().addAll(iconImage, iconLabel);
            return content;
        }

        @Override
        protected String getText() {
            return "Icon Contribution";
        }
        
        @Override
        public String toString() {
            return getText();
        }
    }
}
