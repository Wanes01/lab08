package it.unibo.mvc;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import it.unibo.mvc.api.DrawNumberController;
import it.unibo.mvc.api.DrawNumberView;
import it.unibo.mvc.controller.DrawNumberControllerImpl;
import it.unibo.mvc.model.DrawNumberImpl;

/**
 * Application entry-point.
 */
public final class LaunchApp {

    private static final int VIEWS_PER_IMPL = 3;

    private LaunchApp() {
    }

    /**
     * Runs the application.
     *
     * @param args ignored
     * @throws ClassNotFoundException    if the fetches class does not exist
     * @throws NoSuchMethodException     if the 0-ary constructor do not exist
     * @throws InvocationTargetException if the constructor throws exceptions
     * @throws InstantiationException    if the constructor throws exceptions
     * @throws IllegalAccessException    in case of reflection issues
     * @throws IllegalArgumentException  in case of reflection issues
     */
    public static void main(final String... args)
            throws ClassNotFoundException,
            NoSuchMethodException,
            InstantiationException,
            IllegalAccessException,
            InvocationTargetException {
        final var model = new DrawNumberImpl();
        final DrawNumberController app = new DrawNumberControllerImpl(model);

        for (final var impl : Set.of("Swing", "CLI")) {
            final var currClass = Class.forName("it.unibo.mvc.view.DrawNumber" + impl + "View");
            for (int i = 0; i < VIEWS_PER_IMPL; i++) {
                final var view = currClass.getConstructor().newInstance();
                if (view instanceof DrawNumberView) {
                    app.addView((DrawNumberView) view);
                }
            }
        }
    }
}
