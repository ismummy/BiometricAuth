// Generated code from Butter Knife. Do not modify!
package com.ismummy.biometricauth.ui.activities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.ismummy.biometricauth.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ScanningActivity_ViewBinding implements Unbinder {
  private ScanningActivity target;

  private View view2131231050;

  @UiThread
  public ScanningActivity_ViewBinding(ScanningActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ScanningActivity_ViewBinding(final ScanningActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.btn_scanner_continue, "field 'btnCont' and method 'continueClicked'");
    target.btnCont = Utils.castView(view, R.id.btn_scanner_continue, "field 'btnCont'", Button.class);
    view2131231050 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.continueClicked();
      }
    });
    target.tvMessage = Utils.findRequiredViewAsType(source, R.id.tv_error, "field 'tvMessage'", TextView.class);
    target.viewFinger = Utils.findRequiredViewAsType(source, R.id.iv_finger, "field 'viewFinger'", ImageView.class);
    target.progressBar = Utils.findRequiredViewAsType(source, R.id.progressbar, "field 'progressBar'", ProgressBar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ScanningActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.btnCont = null;
    target.tvMessage = null;
    target.viewFinger = null;
    target.progressBar = null;

    view2131231050.setOnClickListener(null);
    view2131231050 = null;
  }
}
