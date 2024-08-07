package de.industrieschule.vp.core.autodiscovery.templates;


/**
 * An abstract class representing the entry point for a plugin in the application.
 * Subclasses of this class should implement the {@link #onClassLoad()} method to define the behavior
 * to be executed after the discovery of all classes with the {@link de.industrieschule.vp.autodiscovery.annotations.AutoloadClass} annotation.
 *
 * @author Moritz Hofmann
 */
public abstract class AutoloadClassTemplate {

    /**
     * This method is called after the discovery of all classes with the {@link de.industrieschule.vp.autodiscovery.annotations.AutoloadClass} annotation.
     * Subclasses should implement this method to define the specific behavior of the plugin.
     */
    public abstract void onClassLoad();
}
