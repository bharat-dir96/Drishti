// Generated by view binder compiler. Do not edit!
package org.tensorflow.lite.examples.objectdetection.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentContainerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;
import org.tensorflow.lite.examples.objectdetection.R;

public final class FragmentFindBinding implements ViewBinding {
  @NonNull
  private final FrameLayout rootView;

  @NonNull
  public final LinearLayout bottleLayout;

  @NonNull
  public final View bottomBar;

  @NonNull
  public final ImageButton btnBottle;

  @NonNull
  public final ImageButton btnChair;

  @NonNull
  public final ImageButton btnDesktop;

  @NonNull
  public final ImageButton btnPerson;

  @NonNull
  public final ImageButton btnTable;

  @NonNull
  public final LinearLayout chairLayout;

  @NonNull
  public final LinearLayout desktopLayout;

  @NonNull
  public final FragmentContainerView fragmentContainer;

  @NonNull
  public final LinearLayout personLayout;

  @NonNull
  public final LinearLayout tableLayout;

  @NonNull
  public final TextView txtPerson;

  private FragmentFindBinding(@NonNull FrameLayout rootView, @NonNull LinearLayout bottleLayout,
      @NonNull View bottomBar, @NonNull ImageButton btnBottle, @NonNull ImageButton btnChair,
      @NonNull ImageButton btnDesktop, @NonNull ImageButton btnPerson,
      @NonNull ImageButton btnTable, @NonNull LinearLayout chairLayout,
      @NonNull LinearLayout desktopLayout, @NonNull FragmentContainerView fragmentContainer,
      @NonNull LinearLayout personLayout, @NonNull LinearLayout tableLayout,
      @NonNull TextView txtPerson) {
    this.rootView = rootView;
    this.bottleLayout = bottleLayout;
    this.bottomBar = bottomBar;
    this.btnBottle = btnBottle;
    this.btnChair = btnChair;
    this.btnDesktop = btnDesktop;
    this.btnPerson = btnPerson;
    this.btnTable = btnTable;
    this.chairLayout = chairLayout;
    this.desktopLayout = desktopLayout;
    this.fragmentContainer = fragmentContainer;
    this.personLayout = personLayout;
    this.tableLayout = tableLayout;
    this.txtPerson = txtPerson;
  }

  @Override
  @NonNull
  public FrameLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentFindBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentFindBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_find, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentFindBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.bottleLayout;
      LinearLayout bottleLayout = ViewBindings.findChildViewById(rootView, id);
      if (bottleLayout == null) {
        break missingId;
      }

      id = R.id.bottomBar;
      View bottomBar = ViewBindings.findChildViewById(rootView, id);
      if (bottomBar == null) {
        break missingId;
      }

      id = R.id.btnBottle;
      ImageButton btnBottle = ViewBindings.findChildViewById(rootView, id);
      if (btnBottle == null) {
        break missingId;
      }

      id = R.id.btnChair;
      ImageButton btnChair = ViewBindings.findChildViewById(rootView, id);
      if (btnChair == null) {
        break missingId;
      }

      id = R.id.btnDesktop;
      ImageButton btnDesktop = ViewBindings.findChildViewById(rootView, id);
      if (btnDesktop == null) {
        break missingId;
      }

      id = R.id.btnPerson;
      ImageButton btnPerson = ViewBindings.findChildViewById(rootView, id);
      if (btnPerson == null) {
        break missingId;
      }

      id = R.id.btnTable;
      ImageButton btnTable = ViewBindings.findChildViewById(rootView, id);
      if (btnTable == null) {
        break missingId;
      }

      id = R.id.chairLayout;
      LinearLayout chairLayout = ViewBindings.findChildViewById(rootView, id);
      if (chairLayout == null) {
        break missingId;
      }

      id = R.id.desktopLayout;
      LinearLayout desktopLayout = ViewBindings.findChildViewById(rootView, id);
      if (desktopLayout == null) {
        break missingId;
      }

      id = R.id.fragment_container;
      FragmentContainerView fragmentContainer = ViewBindings.findChildViewById(rootView, id);
      if (fragmentContainer == null) {
        break missingId;
      }

      id = R.id.personLayout;
      LinearLayout personLayout = ViewBindings.findChildViewById(rootView, id);
      if (personLayout == null) {
        break missingId;
      }

      id = R.id.tableLayout;
      LinearLayout tableLayout = ViewBindings.findChildViewById(rootView, id);
      if (tableLayout == null) {
        break missingId;
      }

      id = R.id.txtPerson;
      TextView txtPerson = ViewBindings.findChildViewById(rootView, id);
      if (txtPerson == null) {
        break missingId;
      }

      return new FragmentFindBinding((FrameLayout) rootView, bottleLayout, bottomBar, btnBottle,
          btnChair, btnDesktop, btnPerson, btnTable, chairLayout, desktopLayout, fragmentContainer,
          personLayout, tableLayout, txtPerson);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
