/*
 * Copyright 2024 efekos
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the “Software”), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package dev.efekos.pg.text;

import dev.efekos.pg.data.color.Color;
import dev.efekos.pg.resource.IconResource;

public class BasicText implements Text{
    private String text;
    private IconResource iconResource;
    private Color color;

    public BasicText(String text) {
        this.text = text;
    }

    @Override
    public String getString() {
        return text;
    }

    @Override
    public String getElement() {
        return "<span>"+text+"</span>";
    }

    @Override
    public Text withIcon(IconResource icon) {
        this.iconResource = icon;
        return this;
    }

    @Override
    public Text withColor(Color color) {
        this.color = color;
        return this;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public IconResource getIconResource() {
        return iconResource;
    }

    public void setIconResource(IconResource iconResource) {
        this.iconResource = iconResource;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}