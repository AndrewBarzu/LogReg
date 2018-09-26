package com.appist.xghos.Wrenchy.interfaces;

public interface ToolbarInterface {
    void showToolbar();
    void hideToolbar();
    void setToolbarTitle(int resource);
    void setToolbarTitle(String resource);
    void showBackButton();
    void hideBackButton();
    void cancel();
    void add();
    void remove_add();
    void show_add();
}
