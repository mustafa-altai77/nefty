package org.sudadev.nefty.ui.custom;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import org.sudadev.nefty.R;
import org.sudadev.nefty.common.PinchImageView;

public class ImageViewerFragment extends DialogFragment {

    private static final String IMAGE_PATH = "IMAGE_PATH";
    private String mPhotoItem;

    public static ImageViewerFragment newInstance(String photoItem) {
        Bundle bundle = new Bundle();
        bundle.putString(IMAGE_PATH, photoItem);

        ImageViewerFragment imageViewerFragment = new ImageViewerFragment();
        imageViewerFragment.setArguments(bundle);
        return imageViewerFragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mPhotoItem = getArguments().getString(IMAGE_PATH);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_photos_viewer, container, false);

        if (mPhotoItem != null && !mPhotoItem.isEmpty()) {
            PinchImageView imageViewPhoto = (PinchImageView) view.findViewById(R.id.image_view_photo);
            Picasso.with(getActivity()).load(mPhotoItem).into(imageViewPhoto);
        }

        ImageView closeImage = (ImageView) view.findViewById(R.id.image_view_close_dialog);
        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageViewerFragment.this.dismiss();
            }
        });

        return view;
    }
}