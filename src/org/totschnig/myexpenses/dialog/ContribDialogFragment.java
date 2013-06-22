package org.totschnig.myexpenses.dialog;

import java.io.Serializable;

import org.totschnig.myexpenses.MyApplication;
import org.totschnig.myexpenses.R;
import org.totschnig.myexpenses.activity.ContribIFace;
import org.totschnig.myexpenses.dialog.MessageDialogFragment.MessageDialogListener;
import org.totschnig.myexpenses.model.ContribFeature.Feature;
import org.totschnig.myexpenses.util.Utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;

public class ContribDialogFragment extends DialogFragment implements DialogInterface.OnClickListener{
  Feature feature;
  int usagesLeft;

  public static final ContribDialogFragment newInstance(Feature feature) {
    ContribDialogFragment dialogFragment = new ContribDialogFragment();
    Bundle bundle = new Bundle();
    bundle.putSerializable("feature", feature);
    dialogFragment.setArguments(bundle);
    return dialogFragment;
  }
  @Override
  public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      final Bundle bundle = getArguments();
      feature = (Feature) bundle.getSerializable("feature");
      usagesLeft = feature.usagesLeft();
  }
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
      CharSequence message = Html.fromHtml(String.format(getString(
        R.string.dialog_contrib_reminder,
        getString(getResources().getIdentifier("contrib_feature_" + feature + "_label", "string", getActivity().getPackageName())),
        usagesLeft > 0 ? getString(R.string.dialog_contrib_usage_count,usagesLeft) : getString(R.string.dialog_contrib_no_usages_left))));
      AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
      alertDialogBuilder.setTitle(R.string.dialog_title_contrib_feature);
      alertDialogBuilder.setMessage(message);
      //null should be your on click listener
      alertDialogBuilder.setNegativeButton(R.string.dialog_contrib_no, this);
      alertDialogBuilder.setPositiveButton(R.string.dialog_contrib_yes, this);
      return alertDialogBuilder.create();
  }
  @Override
  public void onClick(DialogInterface dialog, int which) {
    Context ctx = getActivity();
    if (which == AlertDialog.BUTTON_POSITIVE) {
      Utils.viewContribApp(getActivity());
    } else {
      if (usagesLeft > 0) {
        ((ContribIFace)ctx).contribFeatureCalled(feature);
      } else {
        ((ContribIFace)ctx).contribFeatureNotCalled();
      }
    }
  }
  @Override
  public void onCancel (DialogInterface dialog) {
    ((ContribIFace)getActivity()).contribFeatureNotCalled();
  }
}