package org.monarchinitiative.vmvt.core;

import org.monarchinitiative.vmvt.core.svg.SvgComponent;
import org.monarchinitiative.vmvt.core.svg.SvgComponentWriter;

import java.util.ArrayList;
import java.util.List;


/**
 * Simple wrapper to make it easier to combine components of an SVG file.
 * Intended to be used with {@link SvgComponentWriter}.
 */
public class SvgBuilder {

    private List<SvgComponent> components;

    public SvgBuilder() {
        components = new ArrayList<>();
    }

    public SvgBuilder addComponent(SvgComponent component) {
        this.components.add(component);
        return this;
    }

    public List<SvgComponent> build() {
        return this.components;
    }

}
