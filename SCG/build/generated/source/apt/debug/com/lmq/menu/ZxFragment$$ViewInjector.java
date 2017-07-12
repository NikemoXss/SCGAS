// Generated code from Butter Knife. Do not modify!
package com.lmq.menu;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class ZxFragment$$ViewInjector<T extends com.lmq.menu.ZxFragment> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427710, "field 'videoList'");
    target.videoList = finder.castView(view, 2131427710, "field 'videoList'");
  }

  @Override public void reset(T target) {
    target.videoList = null;
  }
}
