// Generated code from Butter Knife. Do not modify!
package com.ismummy.biometricauth.ui.activities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.ismummy.biometricauth.R;
import com.rengwuxian.materialedittext.MaterialEditText;
import java.lang.CharSequence;
import java.lang.IllegalStateException;
import java.lang.Override;

public class EnrollmentActivity_ViewBinding implements Unbinder {
  private EnrollmentActivity target;

  private View view2131232016;

  private TextWatcher view2131232016TextWatcher;

  private View view2131232062;

  private View view2131231047;

  @UiThread
  public EnrollmentActivity_ViewBinding(EnrollmentActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public EnrollmentActivity_ViewBinding(final EnrollmentActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.medt_search_input, "field 'searchInput' and method 'AddressChanged'");
    target.searchInput = Utils.castView(view, R.id.medt_search_input, "field 'searchInput'", MaterialEditText.class);
    view2131232016 = view;
    view2131232016TextWatcher = new TextWatcher() {
      @Override
      public void onTextChanged(CharSequence p0, int p1, int p2, int p3) {
      }

      @Override
      public void beforeTextChanged(CharSequence p0, int p1, int p2, int p3) {
      }

      @Override
      public void afterTextChanged(Editable p0) {
        target.AddressChanged();
      }
    };
    ((TextView) view).addTextChangedListener(view2131232016TextWatcher);
    target.detailsLayout = Utils.findRequiredViewAsType(source, R.id.layout_details, "field 'detailsLayout'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.miv_search, "method 'searchClicked'");
    view2131232062 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.searchClicked();
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_enrollment_proceed, "method 'proceedClicked'");
    view2131231047 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.proceedClicked();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    EnrollmentActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.searchInput = null;
    target.detailsLayout = null;

    ((TextView) view2131232016).removeTextChangedListener(view2131232016TextWatcher);
    view2131232016TextWatcher = null;
    view2131232016 = null;
    view2131232062.setOnClickListener(null);
    view2131232062 = null;
    view2131231047.setOnClickListener(null);
    view2131231047 = null;
  }
}
