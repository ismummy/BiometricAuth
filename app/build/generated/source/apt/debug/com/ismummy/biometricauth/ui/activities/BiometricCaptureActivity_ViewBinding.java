// Generated code from Butter Knife. Do not modify!
package com.ismummy.biometricauth.ui.activities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.ismummy.biometricauth.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class BiometricCaptureActivity_ViewBinding implements Unbinder {
  private BiometricCaptureActivity target;

  private View view2131231935;

  private View view2131231936;

  @UiThread
  public BiometricCaptureActivity_ViewBinding(BiometricCaptureActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public BiometricCaptureActivity_ViewBinding(final BiometricCaptureActivity target, View source) {
    this.target = target;

    View view;
    target.leftImage = Utils.findRequiredViewAsType(source, R.id.iv_left, "field 'leftImage'", ImageView.class);
    target.rightImage = Utils.findRequiredViewAsType(source, R.id.iv_right, "field 'rightImage'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.layout_left, "method 'leftClicked'");
    view2131231935 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.leftClicked();
      }
    });
    view = Utils.findRequiredView(source, R.id.layout_right, "method 'rightClicked'");
    view2131231936 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.rightClicked();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    BiometricCaptureActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.leftImage = null;
    target.rightImage = null;

    view2131231935.setOnClickListener(null);
    view2131231935 = null;
    view2131231936.setOnClickListener(null);
    view2131231936 = null;
  }
}
