/***
 * Excerpted from "OpenGL ES for Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/kbogla for more book information.
***/
/*******************************************************************************
 * Copyright (C) 2012 Digipom Inc. All Rights Reserved. Unauthorized distribution is strictly prohibited.
 ******************************************************************************/
package com.nextstep.android;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import com.nextstep.android.util.TextureHelper.FilterMode;

public class ChooseFilterMode extends DialogFragment {
    public static interface ChooseFilterModeListener {
        void onChooseFilter(FilterMode filterMode);
    }
    
	private static final String TAG = "ChooseFilterMode";

	public static void show(FragmentManager fragmentManager) {
		final ChooseFilterMode fragment = new ChooseFilterMode();
		fragment.show(fragmentManager, TAG);
	}		

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {						
		final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.chooseTextureFilter);
		builder.setItems(R.array.filterChoices, new OnClickListener() {            
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final FilterMode filterMode;
                
                if (which == 0) {
                    filterMode = FilterMode.NEAREST_NEIGHBOUR;
                } else if (which == 1) {
                    filterMode = FilterMode.BILINEAR;
                } else if (which == 2) {
                    filterMode = FilterMode.BILINEAR_WITH_MIPMAPS;
                } else if (which == 3) {
                    filterMode = FilterMode.TRILINEAR;
                } else {
                    filterMode = FilterMode.ANISOTROPIC;
                }  
                
                ((ChooseFilterModeListener)getActivity()).onChooseFilter(filterMode);                
            }
        });
					
		return builder.create();
	}
}
