/*
 * Copyright (C) 2014 Vlad Mihalachi
 *
 * This file is part of Turbo Editor.
 *
 * Turbo Editor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Turbo Editor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package sharedcode.turboeditor.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import sharedcode.turboeditor.R;
import sharedcode.turboeditor.preferences.PreferenceHelper;
import sharedcode.turboeditor.preferences.SettingsFragment;
import sharedcode.turboeditor.util.SaveFileTask;

import org.mozilla.universalchardet.Constants;

public class EncodingDialogFragment extends DialogFragment implements AdapterView.OnItemClickListener {

    private final String[] encodings = new String[]{
            Constants.CHARSET_BIG5,
            Constants.CHARSET_EUC_JP,
            Constants.CHARSET_EUC_KR,
            Constants.CHARSET_EUC_TW,
            Constants.CHARSET_GB18030,
            "GB2312",
            Constants.CHARSET_IBM855,
            Constants.CHARSET_IBM866,
            Constants.CHARSET_ISO_2022_CN,
            Constants.CHARSET_ISO_2022_JP,
            Constants.CHARSET_ISO_2022_KR,
            Constants.CHARSET_ISO_8859_5,
            Constants.CHARSET_ISO_8859_7,
            Constants.CHARSET_ISO_8859_8,
            Constants.CHARSET_KOI8_R,
            Constants.CHARSET_MACCYRILLIC,
            Constants.CHARSET_SHIFT_JIS,
            Constants.CHARSET_UTF_16BE,
            Constants.CHARSET_UTF_16LE,
            Constants.CHARSET_UTF_32BE,
            Constants.CHARSET_UTF_32LE,
            Constants.CHARSET_UTF_8,
            Constants.CHARSET_WINDOWS_1251,
            Constants.CHARSET_WINDOWS_1252,
            Constants.CHARSET_WINDOWS_1253,
            Constants.CHARSET_WINDOWS_1255
    };
    private ListView list;

    public static EncodingDialogFragment newInstance() {
        final EncodingDialogFragment f = new EncodingDialogFragment();
        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_encoding_list, null);
        list = (ListView) view.findViewById(android.R.id.list);
        Switch autoencoding = (Switch) view.findViewById(android.R.id.checkbox);
        autoencoding.setChecked(PreferenceHelper.getAutoEncoding(getActivity()));

        autoencoding.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferenceHelper.setAutoencoding(getActivity(), isChecked);
            }
        });

        list.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.item_single_choice, encodings));
        list.setOnItemClickListener(this);

        String currentEncoding = PreferenceHelper.getEncoding(getActivity());

        for (int i = 0; i < encodings.length; i++) {
            if (currentEncoding.equals(encodings[i])) {
                list.setItemChecked(i, true);
            }

        }

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DialogListener target = (DialogListener) getTargetFragment();
        if (target == null) {
            target = (DialogListener) getActivity();
        }
        target.onEncodingSelected(encodings[position]);
        this.dismiss();
    }

    public interface DialogListener {
        void onEncodingSelected(String result);
    }
}