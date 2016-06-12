package com.odm.gui.entities;

import java.awt.*;

/**
 * Created by mohamed on 6/10/16.
 */
public class FrameOptions {
    private boolean defaultCloseOperation;
    private Dimension dimension;
    private boolean isResizable;
    private boolean isVisible;
    private boolean isCentered;

    public boolean isDefaultCloseOperation() {
        return defaultCloseOperation;
    }

    public void setDefaultCloseOperation(boolean defaultCloseOperation) {
        this.defaultCloseOperation = defaultCloseOperation;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public boolean isResizable() {
        return isResizable;
    }

    public void setIsResizable(boolean isResizable) {
        this.isResizable = isResizable;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setIsVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public boolean isCentered() {
        return isCentered;
    }

    public void setIsCentered(boolean isCentered) {
        this.isCentered = isCentered;
    }

}
