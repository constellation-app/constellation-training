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

import au.gov.asd.tac.constellation.views.mapview.overlays.MapOverlay;
import org.openide.util.lookup.ServiceProvider;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

/**
 * Mouse Position Overlay.
 *
 * @author cygnus_x-1
 */
@ServiceProvider(service = MapOverlay.class)
public class MousePositionOverlay extends MapOverlay {

    private boolean mousePressed = false;

    @Override
    public String getName() {
        return "Mouse Position Overlay";
    }

    @Override
    public float getX() {
        return renderer.getComponent().getX() + renderer.getComponent().getWidth() - 10 - width;
    }

    @Override
    public float getY() {
        return renderer.getComponent().getY() + renderer.getComponent().getHeight() - 10 - height;
    }

    @Override
    public void overlay() {

        // draw mouse position overlay
        renderer.noStroke();
        renderer.fill(mousePressed ? HIGHLIGHT_COLOUR : BACKGROUND_COLOUR);
        renderer.rect(x, y, width, height);

        float yOffset = y + MARGIN;

        // draw location info
        final String mouseX = String.valueOf(renderer.mouseX) + "px";
        final String mouseY = String.valueOf(renderer.mouseY) + "px";
        drawLabel("Mouse", x + 60, yOffset);
        drawValue(mouseX, x + 60, yOffset, VALUE_BOX_MEDIUM_WIDTH, false, false);
        drawValue(mouseY, x + 60 + VALUE_BOX_MEDIUM_WIDTH + PADDING, yOffset, VALUE_BOX_MEDIUM_WIDTH, false, false);
    }

    @Override
    public void mouseMoved(final MouseEvent event) {
    }

    @Override
    public void mouseClicked(final MouseEvent event) {
    }

    @Override
    public void mousePressed(final MouseEvent event) {
        mousePressed = true;
    }

    @Override
    public void mouseDragged(final MouseEvent event) {
    }

    @Override
    public void mouseReleased(final MouseEvent event) {
        mousePressed = false;
    }

    @Override
    public void mouseWheel(final MouseEvent event) {
    }

    @Override
    public void keyPressed(final KeyEvent event) {
    }
}
