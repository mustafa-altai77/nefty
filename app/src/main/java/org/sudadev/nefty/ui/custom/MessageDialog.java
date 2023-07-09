package org.sudadev.nefty.ui.custom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import org.sudadev.nefty.R;

public class MessageDialog extends DialogFragment {

    public static void showMessageDialog(BaseActivity activity,
                                         String title, String message) {
        FragmentManager fm = activity.getSupportFragmentManager();
        MessageDialog editNameDialogFragment = MessageDialog.newInstance(title, message);
        editNameDialogFragment.show(fm, "MessageDialog");
    }

    public static MessageDialog newInstance(String title, String message) {
        MessageDialog fragment = new MessageDialog();
        Bundle args = new Bundle();
        args.putString("Message", message);
        args.putString("Title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_message, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String title = getArguments().getString("Title");
        String message = getArguments().getString("Message");

        Button ok_button =  view.findViewById(R.id.ok_button);
        ImageView close_button = view.findViewById(R.id.close_button);
        TextView title_text_view = view.findViewById(R.id.title_text_view);
        TextView message_text_view = view.findViewById(R.id.message_text_view);

        title_text_view.setText(title);
        message_text_view.setText(message);

        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}