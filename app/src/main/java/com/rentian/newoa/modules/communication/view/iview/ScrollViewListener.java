package com.rentian.newoa.modules.communication.view.iview;

import com.rentian.newoa.modules.communication.view.ObservableScrollView;

public interface ScrollViewListener {

    void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy);

}