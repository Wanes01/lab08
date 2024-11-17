package it.unibo.mvc.view;

import it.unibo.mvc.api.DrawNumberController;
import it.unibo.mvc.api.DrawNumberView;
import it.unibo.mvc.api.DrawResult;

/**
 * This view must only write results to standard output
 */
public class DrawNumberCLIView implements DrawNumberView {

    @Override
    public void setController(DrawNumberController observer) {
    }

    @Override
    public void start() {
    }

    @Override
    public void result(DrawResult res) {
        System.out.println(res.getDescription());
    }

}
