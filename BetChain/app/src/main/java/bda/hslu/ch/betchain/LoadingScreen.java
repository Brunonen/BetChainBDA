package bda.hslu.ch.betchain;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class LoadingScreen extends DialogFragment {
    private String loadingScreenTitle;
    private View dialogeView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setCancelable(false);
        this.dialogeView = inflater.inflate(R.layout.fragment_loading_screen, null);
        builder.setView(dialogeView);
        TextView loadingScreenTitle = (TextView) dialogeView.findViewById(R.id.loadingScreenTitle);
        loadingScreenTitle.setText(this.loadingScreenTitle);
        //Glide.with(context).load(recorder.getPictureFile()).asGif().into(gifImageView);

        // Create the AlertDialog object and return it
        return builder.create();
    }

    public void setTitle(String title){
        loadingScreenTitle = title.toUpperCase();
    }

}