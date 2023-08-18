package com.x8192Bit.DIYEdit_Mobile.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.x8192Bit.DIYEdit_Mobile.Utils.CharUtils;
import com.x8192Bit.DIYEdit_Mobile.Utils.GraphicsUtils;
import com.xperia64.diyedit.FileByteOperations;
import com.xperia64.diyedit.metadata.Checksums;
import com.xperia64.diyedit.metadata.GameMetadata;
import com.xperia64.diyedit.metadata.MangaMetadata;
import com.xperia64.diyedit.metadata.Metadata;
import com.xperia64.diyedit.metadata.RecordMetadata;

import org.joda.time.DateTime;

import java.util.Locale;
import java.util.Objects;

import x8192Bit.DIYEdit_Mobile.R;

public class MetadataEditFragment extends Fragment {

    private static final String ARG_NAME = "name";
    private static final String ARG_MIOTYPE = "miotype";

    private boolean isCharLostMode = false;

    private String name;

    private int miotype;

    private TextView CharLostIndicator;

    AdapterView.OnItemSelectedListener SpinnerEvent = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            refreshIcon();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    public MetadataEditFragment() {
    }

    public static MetadataEditFragment newInstance(String name, int miotype) {
        MetadataEditFragment fragment = new MetadataEditFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        args.putInt(ARG_MIOTYPE, miotype);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString(ARG_NAME);
            miotype = getArguments().getInt(ARG_MIOTYPE);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_metadata, container, true);
    }

    void refreshIcon() {
        //region Init
        Spinner selfColor = requireView().findViewById(R.id.ColorSelfSpinner);
        Spinner selfStyle = requireView().findViewById(R.id.StyleSelfSpinner);
        Spinner iconColor = requireView().findViewById(R.id.ColorIconSpinner);
        Spinner iconStyle = requireView().findViewById(R.id.StyleIconSpinner);
        ImageView preview = requireView().findViewById(R.id.MetadataIconPreview);
        //endregion
        //region For GAME Refresh
        if (miotype == 0) {
            GraphicsUtils gt = new GraphicsUtils();
            GraphicsUtils.GameItem gi = gt.new GameItem();
            gi.setCartridgeColor(selfColor.getSelectedItemPosition());
            gi.setCartridgeShape(selfStyle.getSelectedItemPosition());
            gi.setIconColor(iconColor.getSelectedItemPosition());
            gi.setIconShape(iconStyle.getSelectedItemPosition());
            preview.setImageBitmap(gi.renderImage(getContext(), 26, 26));
        }
        //endregion
        //region For RECORD Refresh
        else if (miotype == 1) {
            GraphicsUtils gt = new GraphicsUtils();
            GraphicsUtils.RecordItem ri = gt.new RecordItem();
            ri.setRecordColor(selfColor.getSelectedItemPosition());
            ri.setRecordShape(selfStyle.getSelectedItemPosition());
            ri.setIconColor(iconColor.getSelectedItemPosition());
            ri.setIconShape(iconStyle.getSelectedItemPosition());
            preview.setImageBitmap(ri.renderImage(getContext(), 26, 26));
        }
        //endregion
        //region For MANGA Refresh
        else if (miotype == 2) {
            GraphicsUtils gt = new GraphicsUtils();
            GraphicsUtils.MangaItem mi = gt.new MangaItem();
            mi.setMangaColor(selfColor.getSelectedItemPosition());
            mi.setIconColor(iconColor.getSelectedItemPosition());
            mi.setIconShape(iconStyle.getSelectedItemPosition());
            preview.setImageBitmap(mi.renderImage(getContext(), 26, 26));
        }
        //endregion
    }

    void loadFromFile(View view) {
        //region Init
        Metadata m = new Metadata(name);
        TextInputEditText seriesInput = view.findViewById(R.id.SeriesNumberInput);
        TextInputEditText nameInput = view.findViewById(R.id.NameInput);
        TextInputEditText commentInput = view.findViewById(R.id.CommentInput);
        TextInputEditText authorInput = view.findViewById(R.id.AuthorInput);
        TextInputEditText companyInput = view.findViewById(R.id.CompanyInput);
        TextInputEditText instructInput = view.findViewById(R.id.GameInstructInput);
        SwitchMaterial lockSwitch = view.findViewById(R.id.LockSwitch);
        TextInputEditText dateInput = view.findViewById(R.id.DateInput);
        RadioButton shortButton = view.findViewById(R.id.ShortTimeButton);
        RadioButton longButton = view.findViewById(R.id.LongTimeButton);
        RadioButton bossButton = view.findViewById(R.id.BOSSTimeButton);
        Spinner selfColor = view.findViewById(R.id.ColorSelfSpinner);
        Spinner selfStyle = view.findViewById(R.id.StyleSelfSpinner);
        Spinner iconColor = view.findViewById(R.id.ColorIconSpinner);
        Spinner iconStyle = view.findViewById(R.id.StyleIconSpinner);
        selfStyle.setOnItemSelectedListener(SpinnerEvent);
        selfColor.setOnItemSelectedListener(SpinnerEvent);
        iconColor.setOnItemSelectedListener(SpinnerEvent);
        iconStyle.setOnItemSelectedListener(SpinnerEvent);
        //endregion
        //region For GAME Settings
        if (miotype == 0) {
            GameMetadata gm = new GameMetadata(name);
            instructInput.setText(gm.getCommand());
            switch (gm.getLength()) {
                case 0:
                    shortButton.toggle();
                    break;
                case 1:
                    longButton.toggle();
                    break;
                case 2:
                    bossButton.toggle();
                    break;
            }
            selfColor.setAdapter(new ArrayAdapter<>(getContext(), R.layout.spinner_item_layout, R.id.SpinnerItemTextView, getContext().getResources().getTextArray(R.array.diy_colors)));
            selfStyle.setAdapter(new ArrayAdapter<>(getContext(), R.layout.spinner_item_layout, R.id.SpinnerItemTextView, getContext().getResources().getTextArray(R.array.game_shapes)));
            iconColor.setAdapter(new ArrayAdapter<>(getContext(), R.layout.spinner_item_layout, R.id.SpinnerItemTextView, getContext().getResources().getTextArray(R.array.diy_colors)));
            iconStyle.setAdapter(new ArrayAdapter<>(getContext(), R.layout.spinner_item_layout, R.id.SpinnerItemTextView, getContext().getResources().getTextArray(R.array.game_icons)));
            selfColor.setSelection(gm.getCartColor());
            selfStyle.setSelection(gm.getCartType());
            iconColor.setSelection(gm.getLogoColor());
            iconStyle.setSelection(gm.getLogo());
        } else {
            instructInput.setEnabled(false);
            shortButton.setEnabled(false);
            longButton.setEnabled(false);
            bossButton.setEnabled(false);
        }
        //endregion
        //region For RECORD Settings
        if (miotype == 1) {

            selfColor.setAdapter(new ArrayAdapter<>(getContext(), R.layout.spinner_item_layout, R.id.SpinnerItemTextView, getContext().getResources().getTextArray(R.array.diy_colors)));
            selfStyle.setAdapter(new ArrayAdapter<>(getContext(), R.layout.spinner_item_layout, R.id.SpinnerItemTextView, getContext().getResources().getTextArray(R.array.record_shapes)));
            iconColor.setAdapter(new ArrayAdapter<>(getContext(), R.layout.spinner_item_layout, R.id.SpinnerItemTextView, getContext().getResources().getTextArray(R.array.diy_colors)));
            iconStyle.setAdapter(new ArrayAdapter<>(getContext(), R.layout.spinner_item_layout, R.id.SpinnerItemTextView, getContext().getResources().getTextArray(R.array.record_icons)));
            RecordMetadata rm = new RecordMetadata(name);
            selfColor.setSelection(rm.getRecordColor());
            selfStyle.setSelection(rm.getRecordType());
            iconColor.setSelection(rm.getLogoColor());
            iconStyle.setSelection(rm.getLogo());
        }
        //endregion
        //region For MANGA Settings
        if (miotype == 2) {
            MangaMetadata mm = new MangaMetadata(name);
            selfStyle.setEnabled(false);
            selfColor.setAdapter(new ArrayAdapter<>(getContext(), R.layout.spinner_item_layout, R.id.SpinnerItemTextView, getContext().getResources().getTextArray(R.array.diy_colors)));
            iconColor.setAdapter(new ArrayAdapter<>(getContext(), R.layout.spinner_item_layout, R.id.SpinnerItemTextView, getContext().getResources().getTextArray(R.array.diy_colors)));
            iconStyle.setAdapter(new ArrayAdapter<>(getContext(), R.layout.spinner_item_layout, R.id.SpinnerItemTextView, getContext().getResources().getTextArray(R.array.manga_icons)));
            selfColor.setSelection(mm.getMangaColor());
            iconColor.setSelection(mm.getLogoColor());
            iconStyle.setSelection(mm.getLogo());
        }
        //endregion
        //region Overall Settings
        seriesInput.setText(
                m.getSerial1()
                        + "-"
                        + String.format(Locale.getDefault(), "%0" + 4 + "d", m.getSerial2())
                        + "-"
                        + String.format(Locale.getDefault(), "%0" + 4 + "d", m.getSerial3())
        );
        nameInput.setText(m.getName());
        commentInput.setText(m.getDescription());
        authorInput.setText(m.getCreator());
        companyInput.setText(m.getBrand());
        DateTime date = new DateTime(2000, 1, 1, 0, 0, 0, 0);
        date = date.plusDays(m.getTimestamp());
        String name = String.format(Locale.getDefault(), "%04d-%02d-%02d", date.getYear(), date.getMonthOfYear(), date.getDayOfMonth());
        dateInput.setText(name);
        lockSwitch.setChecked(m.getLocked());
        //endregion
    }

    void writeToFile(View view) {

        //region Init
        byte[] fil = FileByteOperations.read(name);
        Metadata m = new Metadata(fil);
        TextInputEditText seriesInput = view.findViewById(R.id.SeriesNumberInput);
        TextInputEditText nameInput = view.findViewById(R.id.NameInput);
        TextInputEditText commentInput = view.findViewById(R.id.CommentInput);
        TextInputEditText authorInput = view.findViewById(R.id.AuthorInput);
        TextInputEditText companyInput = view.findViewById(R.id.CompanyInput);
        TextInputEditText instructInput = view.findViewById(R.id.GameInstructInput);
        SwitchMaterial lockSwitch = view.findViewById(R.id.LockSwitch);
        RadioButton shortButton = view.findViewById(R.id.ShortTimeButton);
        RadioButton longButton = view.findViewById(R.id.LongTimeButton);
        RadioButton bossButton = view.findViewById(R.id.BOSSTimeButton);
        Spinner selfColor = view.findViewById(R.id.ColorSelfSpinner);
        Spinner selfStyle = view.findViewById(R.id.StyleSelfSpinner);
        Spinner iconColor = view.findViewById(R.id.ColorIconSpinner);
        Spinner iconStyle = view.findViewById(R.id.StyleIconSpinner);
        m.setLocked(lockSwitch.isChecked());
        //endregion
        //region CharLostMode Check PRE part
        String pre_name = Objects.requireNonNull(nameInput.getText()).toString();
        String pre_comment = Objects.requireNonNull(commentInput.getText()).toString();
        String pre_author = Objects.requireNonNull(authorInput.getText()).toString();
        String pre_company = Objects.requireNonNull(companyInput.getText()).toString();
        String pre_command = null;
        //endregion
        //region Overall Settings
        try {
            String[] splited = Objects.requireNonNull(seriesInput.getText()).toString().split("-");
            if (splited.length != 3 || splited[0].length() > 4 || splited[1].length() > 4 || splited[2].length() > 4 || !CharUtils.isNumeric(splited[1]) || !CharUtils.isNumeric(splited[2])) {
                new AlertDialog.Builder(getContext())
                        .setMessage(R.string.serialNumberWrongKey)
                        .setCancelable(true)
                        .setNegativeButton(R.string.okKey, null)
                        .show();
            } else {
                m.setSerial(splited[0], Integer.parseInt(splited[1]), Integer.parseInt(splited[2]));
                m.setName(isCharLostMode ? CharUtils.doubleFirstChar(pre_name) : pre_name);
                m.setDescription(isCharLostMode ? CharUtils.doubleFirstChar(pre_comment) : pre_comment);
                m.setCreator(isCharLostMode ? CharUtils.doubleFirstChar(pre_author) : pre_author);
                m.setBrand(isCharLostMode ? CharUtils.doubleFirstChar(pre_company) : pre_company);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            new AlertDialog.Builder(getContext())
                    .setMessage(R.string.metadataEmptyKey)
                    .setCancelable(true)
                    .setNegativeButton(R.string.okKey, null)
                    .show();
        }
        fil = m.file;
        //endregion
        //region For GAME Settings
        if (miotype == 0) {
            GameMetadata gm = new GameMetadata(fil);

            pre_command = Objects.requireNonNull(instructInput.getText()).toString();
            gm.setCommand(isCharLostMode ? CharUtils.doubleFirstChar(pre_command) : pre_command);
            if (shortButton.isChecked()) {
                gm.setLength((byte) 0);
            } else if (longButton.isChecked()) {
                gm.setLength((byte) 1);
            } else if (bossButton.isChecked()) {
                gm.setLength((byte) 2);
            }
            gm.setCartColor((byte) selfColor.getSelectedItemPosition());
            gm.setCartType((byte) selfStyle.getSelectedItemPosition());
            gm.setLogoColor((byte) iconColor.getSelectedItemPosition());
            gm.setLogo((byte) iconStyle.getSelectedItemPosition());
            FileByteOperations.write(Checksums.writeChecksums(gm.file), name);
        }
        //endregion
        //region For RECORD Settings
        if (miotype == 1) {
            RecordMetadata rm = new RecordMetadata(fil);
            rm.setRecordColor((byte) selfColor.getSelectedItemPosition());
            rm.setRecordType((byte) selfStyle.getSelectedItemPosition());
            rm.setLogoColor((byte) iconColor.getSelectedItemPosition());
            rm.setLogo((byte) iconStyle.getSelectedItemPosition());
            FileByteOperations.write(Checksums.writeChecksums(rm.file), name);
        }
        //endregion
        //region For MANGA Settings
        if (miotype == 2) {
            MangaMetadata mm = new MangaMetadata(fil);
            mm.setMangaColor((byte) selfColor.getSelectedItemPosition());
            mm.setLogoColor((byte) iconColor.getSelectedItemPosition());
            mm.setLogo((byte) iconStyle.getSelectedItemPosition());
            FileByteOperations.write(Checksums.writeChecksums(mm.file), name);
        }
        //endregion
        //region CharLostMode Check POST part
        String post_name = m.getName();
        if (!isCharLostMode && !pre_name.equals(post_name) && pre_name.contains(post_name)) {
            isCharLostMode = true;
            CharLostIndicator.setText(R.string.charLostModeKey);
            m.setName(CharUtils.doubleFirstChar(pre_name));
            m.setDescription(CharUtils.doubleFirstChar(pre_comment));
            m.setCreator(CharUtils.doubleFirstChar(pre_author));
            m.setBrand(CharUtils.doubleFirstChar(pre_company));
            if (miotype == 0) {
                GameMetadata gm = new GameMetadata(fil);
                gm.setCommand(CharUtils.doubleFirstChar(pre_command));
                FileByteOperations.write(Checksums.writeChecksums(gm.file), name);
            }

        }
        //endregion
        FileByteOperations.write(Checksums.writeChecksums(m.file), name);
        loadFromFile(view);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadFromFile(view);
        Button save = view.findViewById(R.id.buttonSave);
        Button discard = view.findViewById(R.id.buttonDiscard);
        CharLostIndicator = view.findViewById(R.id.CharLostModeTextView);
        discard.setOnClickListener((vd) -> loadFromFile(requireView()));
        save.setOnClickListener((vs) -> {
            writeToFile(requireView());
            loadFromFile(requireView());
        });
    }
}