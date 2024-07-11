package com.util.interfaces;

@FunctionalInterface
public interface InitFlowController {
    void initController(Runnable onExit);
}
