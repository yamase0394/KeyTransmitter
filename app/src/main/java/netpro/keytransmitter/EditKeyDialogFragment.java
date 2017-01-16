package netpro.keytransmitter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.view.View.GONE;

public class EditKeyDialogFragment extends android.support.v4.app.DialogFragment {

    private OnKeyUpdatedListener listener;
    private List<View> keyCodeViewList = new ArrayList<>();
    private List<View> rightKeyCodeViewList = new ArrayList<>();
    private List<View> leftKeyCodeViewList = new ArrayList<>();
    private List<View> flickUpKeyCodeViewList = new ArrayList<>();
    private List<View> flickDownKeyCodeViewList = new ArrayList<>();
    private List<View> flickRightKeyCodeViewList = new ArrayList<>();
    private List<View> flickLeftKeyCodeViewList = new ArrayList<>();

    public static EditKeyDialogFragment newInstance(int position, int emptySpace, Key key) {
        EditKeyDialogFragment fragment = new EditKeyDialogFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putInt("emptySpace", emptySpace + key.getColumnSpan() * key.getRowSpan());
        args.putSerializable("key", key);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final View view = View.inflate(getActivity(), R.layout.dialog_fragment_edit_key, null);
        Key key = (Key) getArguments().getSerializable("key");

        final TextInputLayout intervalLayout = (TextInputLayout) view.findViewById(R.id.intervalLayout);
        intervalLayout.setErrorEnabled(true);
        intervalLayout.setError("必須");
        if (key instanceof PressingKey) {
            intervalLayout.getEditText().setText(String.valueOf(((PressingKey) key).getInputInterval()));
            intervalLayout.setErrorEnabled(false);
        } else {
            intervalLayout.setVisibility(GONE);
        }
        intervalLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    Integer.parseInt(editable.toString());
                } catch (NumberFormatException e) {
                    intervalLayout.setErrorEnabled(true);
                    intervalLayout.setError("必須");
                    return;
                }
                intervalLayout.setErrorEnabled(false);
            }
        });

        final TextInputLayout adjustTextInputLayout = (TextInputLayout) view.findViewById(R.id.text_input_layout_adjust);
        adjustTextInputLayout.setErrorEnabled(true);
        adjustTextInputLayout.setError("必須");
        if (key instanceof FlickKey) {
            adjustTextInputLayout.getEditText().setText(String.valueOf(((FlickKey) key).getAdjust()));
            adjustTextInputLayout.setErrorEnabled(false);
        } else {
            adjustTextInputLayout.setVisibility(GONE);
        }
        adjustTextInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    Integer.parseInt(editable.toString());
                } catch (NumberFormatException e) {
                    adjustTextInputLayout.setErrorEnabled(true);
                    adjustTextInputLayout.setError("必須");
                    return;
                }
                adjustTextInputLayout.setErrorEnabled(false);
            }
        });

        final TextInputLayout descriptionTil = (TextInputLayout) view.findViewById(R.id.descriptionTextInputLayout);
        descriptionTil.getEditText().setText(key.getDescription());
        /*
        descriptionTil.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Spinner keyTypeSpinner = (Spinner) view.findViewById(R.id.keyTypeSpinner);
                String str = (String) keyTypeSpinner.getSelectedItem();
                Key.Type type = Key.Type.toType(str);
                if (type != Key.Type.EMPTY && editable.length() == 0) {
                    descriptionTil.setErrorEnabled(true);
                    descriptionTil.setError("必須");
                } else {
                    descriptionTil.setErrorEnabled(false);
                }
            }
        });
        */

        final Spinner columnCountSpinner = (Spinner) view.findViewById(R.id.columnCountSpinner);
        Integer[] columnCounts = {1, 2, 3, 4, 5};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, columnCounts);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        columnCountSpinner.setAdapter(adapter);
        columnCountSpinner.setSelection(key.getColumnSpan() - 1);

        final Spinner rowCountSpinner = (Spinner) view.findViewById(R.id.rowCountSpinner);
        Integer[] rowCounts = {1, 2, 3, 4};
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, rowCounts);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rowCountSpinner.setAdapter(adapter);
        rowCountSpinner.setSelection(key.getRowSpan() - 1);


        final LinearLayout addKeyLayout = (LinearLayout) view.findViewById(R.id.addKeyLayout);
        final LinearLayout addRightKeyLayout = (LinearLayout) view.findViewById(R.id.addRightKeyLayout);
        final LinearLayout addLeftKeyLayout = (LinearLayout) view.findViewById(R.id.addLeftKeyLayout);
        final LinearLayout addFlickUpKeyLayout = (LinearLayout) view.findViewById(R.id.addFlickUpKeyLayout);
        final LinearLayout addFlickDownKeyLayout = (LinearLayout) view.findViewById(R.id.addFlickDownKeyLayout);
        final LinearLayout addFlickRightKeyLayout = (LinearLayout) view.findViewById(R.id.addFlickRightKeyLayout);
        final LinearLayout addFlickLeftKeyLayout = (LinearLayout) view.findViewById(R.id.addFlickLeftKeyLayout);

        final Spinner keyTypeSpinner = (Spinner) view.findViewById(R.id.keyTypeSpinner);
        Key.Type[] keyTypes = Key.Type.values();
        String[] keyDescriptions = new String[keyTypes.length];
        for (int i = 0; i < keyDescriptions.length; i++) {
            keyDescriptions[i] = keyTypes[i].getDescription();
        }
        ArrayAdapter<String> keyAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, keyDescriptions);
        keyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        keyTypeSpinner.setAdapter(keyAdapter);
        keyTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Spinner spinner = (Spinner) adapterView;
                String description = (String) spinner.getSelectedItem();
                Key.Type type = Key.Type.toType(description);

                if (type == Key.Type.EMPTY) {
                    //名前と説明を必須にしない
                    //descriptionTil.setErrorEnabled(false);
                    //キーを追加できなくする
                    addKeyLayout.setVisibility(GONE);
                } else {
                    /*
                    if (descriptionTil.getEditText().getText().length() == 0) {
                        descriptionTil.setErrorEnabled(true);
                        descriptionTil.setError("必須");
                    }
                    */
                    addKeyLayout.setVisibility(View.VISIBLE);
                }

                if (type == Key.Type.PRESSING) {
                    //キー送信間隔入力ボックスを表示
                    intervalLayout.setVisibility(View.VISIBLE);
                } else {
                    intervalLayout.setVisibility(GONE);
                }

                if (type == Key.Type.KNOB) {
                    addRightKeyLayout.setVisibility(View.VISIBLE);
                    addLeftKeyLayout.setVisibility(View.VISIBLE);
                    addKeyLayout.setVisibility(GONE);
                } else {
                    addRightKeyLayout.setVisibility(View.GONE);
                    addLeftKeyLayout.setVisibility(View.GONE);
                }

                if (type == Key.Type.FLICK) {
                    addFlickUpKeyLayout.setVisibility(View.VISIBLE);
                    addFlickDownKeyLayout.setVisibility(View.VISIBLE);
                    addFlickRightKeyLayout.setVisibility(View.VISIBLE);
                    addFlickLeftKeyLayout.setVisibility(View.VISIBLE);
                    adjustTextInputLayout.setVisibility(View.VISIBLE);
                    addKeyLayout.setVisibility(GONE);
                } else {
                    addFlickUpKeyLayout.setVisibility(View.GONE);
                    addFlickDownKeyLayout.setVisibility(View.GONE);
                    addFlickRightKeyLayout.setVisibility(View.GONE);
                    addFlickLeftKeyLayout.setVisibility(View.GONE);
                    adjustTextInputLayout.setVisibility(GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        keyTypeSpinner.setSelection(key.getType().ordinal());

        final List<String> alphabetList = new ArrayList<>();
        final int ALPHABET_SIZE = 'Z' - 'A';
        char alphabet = 'A';
        for (int i = 0; i <= ALPHABET_SIZE; i++) {
            alphabetList.add(String.valueOf(alphabet++));
        }

        final List<String> numberList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            numberList.add(String.valueOf(i));
        }

        final List<String> controlKeyList = Arrays.asList("Backspace", "Enter", "Shift", "Ctrl", "Alt", "Pause", "Space", "Page Up", "Page Down", "End", "Home", "←", "↑", "→", "↓", "Print Screen", "Insert", "Delete", "Windows", "Num Lock", "Scroll Lock", "Esc", "Tab");

        final List<String> functionKeyList = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            functionKeyList.add("F" + String.valueOf(i));
        }

        final List<String> symbolList = Arrays.asList(":", ";", "+", ",", "-", "=", ".", "/", "@", "[", "\\", "]", "^", "_");

        final LinearLayout keyCodesLayout = (LinearLayout) view.findViewById(R.id.keyCodesLayout);
        for (String keyCode : key.getKeyCodeList()) {
            List<String> spinnerContents = new ArrayList<>();
            if (alphabetList.contains(keyCode)) {
                spinnerContents = alphabetList;
            } else if (numberList.contains(keyCode)) {
                spinnerContents = numberList;
            } else if (controlKeyList.contains(keyCode)) {
                spinnerContents = controlKeyList;
            } else if (functionKeyList.contains(keyCode)) {
                spinnerContents = functionKeyList;
            } else if (symbolList.contains(keyCode)) {
                spinnerContents = symbolList;
            }

            View addKeyCodeView = View.inflate(getActivity(), R.layout.layout_add_key_code, null);

            Spinner spinner = (Spinner) addKeyCodeView.findViewById(R.id.keyCodeSpinner);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, spinnerContents);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(arrayAdapter);
            spinner.setSelection(arrayAdapter.getPosition(keyCode));

            Button removeButton = (Button) addKeyCodeView.findViewById(R.id.removeButton);
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ViewGroup parent = (ViewGroup) view.getParent();
                    keyCodeViewList.remove(parent);
                    parent.removeAllViews();
                }
            });
            keyCodesLayout.addView(addKeyCodeView);
            keyCodeViewList.add(addKeyCodeView);
        }

        final LinearLayout rightKeyCodesLayout = (LinearLayout) view.findViewById(R.id.rotate_right_keyCodesLayout);
        if (key.type == Key.Type.KNOB) {
            for (String keyCode : ((ControlKnob) key).getRotateRightKeyCodeList()) {
                List<String> spinnerContents = new ArrayList<>();
                if (alphabetList.contains(keyCode)) {
                    spinnerContents = alphabetList;
                } else if (numberList.contains(keyCode)) {
                    spinnerContents = numberList;
                } else if (controlKeyList.contains(keyCode)) {
                    spinnerContents = controlKeyList;
                } else if (functionKeyList.contains(keyCode)) {
                    spinnerContents = functionKeyList;
                } else if (symbolList.contains(keyCode)) {
                    spinnerContents = symbolList;
                }

                View addKeyCodeView = View.inflate(getActivity(), R.layout.layout_add_key_code, null);

                Spinner spinner = (Spinner) addKeyCodeView.findViewById(R.id.keyCodeSpinner);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, spinnerContents);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(arrayAdapter);
                spinner.setSelection(arrayAdapter.getPosition(keyCode));

                Button removeButton = (Button) addKeyCodeView.findViewById(R.id.removeButton);
                removeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ViewGroup parent = (ViewGroup) view.getParent();
                        rightKeyCodeViewList.remove(parent);
                        parent.removeAllViews();
                    }
                });
                rightKeyCodesLayout.addView(addKeyCodeView);
                rightKeyCodeViewList.add(addKeyCodeView);
            }
        }

        final LinearLayout leftKeyCodesLayout = (LinearLayout) view.findViewById(R.id.rotate_left_keyCodesLayout);
        if (key.type == Key.Type.KNOB) {
            for (String keyCode : ((ControlKnob) key).getRotateLeftKeyCodeList()) {
                List<String> spinnerContents = new ArrayList<>();
                if (alphabetList.contains(keyCode)) {
                    spinnerContents = alphabetList;
                } else if (numberList.contains(keyCode)) {
                    spinnerContents = numberList;
                } else if (controlKeyList.contains(keyCode)) {
                    spinnerContents = controlKeyList;
                } else if (functionKeyList.contains(keyCode)) {
                    spinnerContents = functionKeyList;
                } else if (symbolList.contains(keyCode)) {
                    spinnerContents = symbolList;
                }

                View addKeyCodeView = View.inflate(getActivity(), R.layout.layout_add_key_code, null);

                Spinner spinner = (Spinner) addKeyCodeView.findViewById(R.id.keyCodeSpinner);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, spinnerContents);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(arrayAdapter);
                spinner.setSelection(arrayAdapter.getPosition(keyCode));

                Button removeButton = (Button) addKeyCodeView.findViewById(R.id.removeButton);
                removeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ViewGroup parent = (ViewGroup) view.getParent();
                        leftKeyCodeViewList.remove(parent);
                        parent.removeAllViews();
                    }
                });
                leftKeyCodesLayout.addView(addKeyCodeView);
                leftKeyCodeViewList.add(addKeyCodeView);
            }
        }

        final LinearLayout flickUpKeyCodesLayout = (LinearLayout) view.findViewById(R.id.flick_up_keyCodesLayout);
        if (key.type == Key.Type.FLICK) {
            for (String keyCode : ((FlickKey) key).getFlickUpKeyStrList()) {
                List<String> spinnerContents = new ArrayList<>();
                if (alphabetList.contains(keyCode)) {
                    spinnerContents = alphabetList;
                } else if (numberList.contains(keyCode)) {
                    spinnerContents = numberList;
                } else if (controlKeyList.contains(keyCode)) {
                    spinnerContents = controlKeyList;
                } else if (functionKeyList.contains(keyCode)) {
                    spinnerContents = functionKeyList;
                } else if (symbolList.contains(keyCode)) {
                    spinnerContents = symbolList;
                }

                View addKeyCodeView = View.inflate(getActivity(), R.layout.layout_add_key_code, null);

                Spinner spinner = (Spinner) addKeyCodeView.findViewById(R.id.keyCodeSpinner);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, spinnerContents);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(arrayAdapter);
                spinner.setSelection(arrayAdapter.getPosition(keyCode));

                Button removeButton = (Button) addKeyCodeView.findViewById(R.id.removeButton);
                removeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ViewGroup parent = (ViewGroup) view.getParent();
                        flickUpKeyCodeViewList.remove(parent);
                        parent.removeAllViews();
                    }
                });
                flickUpKeyCodesLayout.addView(addKeyCodeView);
                flickUpKeyCodeViewList.add(addKeyCodeView);
            }
        }

        final LinearLayout flickDownKeyCodesLayout = (LinearLayout) view.findViewById(R.id.flick_down_keyCodesLayout);
        if (key.type == Key.Type.FLICK) {
            for (String keyCode : ((FlickKey) key).getFlickDownKeyStrList()) {
                List<String> spinnerContents = new ArrayList<>();
                if (alphabetList.contains(keyCode)) {
                    spinnerContents = alphabetList;
                } else if (numberList.contains(keyCode)) {
                    spinnerContents = numberList;
                } else if (controlKeyList.contains(keyCode)) {
                    spinnerContents = controlKeyList;
                } else if (functionKeyList.contains(keyCode)) {
                    spinnerContents = functionKeyList;
                } else if (symbolList.contains(keyCode)) {
                    spinnerContents = symbolList;
                }

                View addKeyCodeView = View.inflate(getActivity(), R.layout.layout_add_key_code, null);

                Spinner spinner = (Spinner) addKeyCodeView.findViewById(R.id.keyCodeSpinner);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, spinnerContents);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(arrayAdapter);
                spinner.setSelection(arrayAdapter.getPosition(keyCode));

                Button removeButton = (Button) addKeyCodeView.findViewById(R.id.removeButton);
                removeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ViewGroup parent = (ViewGroup) view.getParent();
                        flickDownKeyCodeViewList.remove(parent);
                        parent.removeAllViews();
                    }
                });
                flickDownKeyCodesLayout.addView(addKeyCodeView);
                flickDownKeyCodeViewList.add(addKeyCodeView);
            }
        }

        final LinearLayout flickRightKeyCodesLayout = (LinearLayout) view.findViewById(R.id.flick_right_keyCodesLayout);
        if (key.type == Key.Type.FLICK) {
            for (String keyCode : ((FlickKey) key).getFlickRightKeyStrList()) {
                List<String> spinnerContents = new ArrayList<>();
                if (alphabetList.contains(keyCode)) {
                    spinnerContents = alphabetList;
                } else if (numberList.contains(keyCode)) {
                    spinnerContents = numberList;
                } else if (controlKeyList.contains(keyCode)) {
                    spinnerContents = controlKeyList;
                } else if (functionKeyList.contains(keyCode)) {
                    spinnerContents = functionKeyList;
                } else if (symbolList.contains(keyCode)) {
                    spinnerContents = symbolList;
                }

                View addKeyCodeView = View.inflate(getActivity(), R.layout.layout_add_key_code, null);

                Spinner spinner = (Spinner) addKeyCodeView.findViewById(R.id.keyCodeSpinner);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, spinnerContents);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(arrayAdapter);
                spinner.setSelection(arrayAdapter.getPosition(keyCode));

                Button removeButton = (Button) addKeyCodeView.findViewById(R.id.removeButton);
                removeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ViewGroup parent = (ViewGroup) view.getParent();
                        flickRightKeyCodeViewList.remove(parent);
                        parent.removeAllViews();
                    }
                });
                flickRightKeyCodesLayout.addView(addKeyCodeView);
                flickRightKeyCodeViewList.add(addKeyCodeView);
            }
        }

        final LinearLayout flickLeftKeyCodesLayout = (LinearLayout) view.findViewById(R.id.flick_left_keyCodesLayout);
        if (key.type == Key.Type.FLICK) {
            for (String keyCode : ((FlickKey) key).getFlickLeftKeyStrList()) {
                List<String> spinnerContents = new ArrayList<>();
                if (alphabetList.contains(keyCode)) {
                    spinnerContents = alphabetList;
                } else if (numberList.contains(keyCode)) {
                    spinnerContents = numberList;
                } else if (controlKeyList.contains(keyCode)) {
                    spinnerContents = controlKeyList;
                } else if (functionKeyList.contains(keyCode)) {
                    spinnerContents = functionKeyList;
                } else if (symbolList.contains(keyCode)) {
                    spinnerContents = symbolList;
                }

                View addKeyCodeView = View.inflate(getActivity(), R.layout.layout_add_key_code, null);

                Spinner spinner = (Spinner) addKeyCodeView.findViewById(R.id.keyCodeSpinner);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, spinnerContents);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(arrayAdapter);
                spinner.setSelection(arrayAdapter.getPosition(keyCode));

                Button removeButton = (Button) addKeyCodeView.findViewById(R.id.removeButton);
                removeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ViewGroup parent = (ViewGroup) view.getParent();
                        flickLeftKeyCodeViewList.remove(parent);
                        parent.removeAllViews();
                    }
                });
                flickLeftKeyCodesLayout.addView(addKeyCodeView);
                flickLeftKeyCodeViewList.add(addKeyCodeView);
            }
        }

        final Button addButton = (Button) view.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //Spinnerに入れるキーコードの種類を選択させるダイアログを生成
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                final String[] items = {"アルファベット", "数字", "制御キー", "ファンクションキー", "記号"};
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        View addKeyCodeView = View.inflate(getActivity(), R.layout.layout_add_key_code, null);

                        Spinner spinner = (Spinner) addKeyCodeView.findViewById(R.id.keyCodeSpinner);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
                        //選択結果に応じてSpinnerの選択肢を変える
                        switch (which) {
                            //アルファベット
                            case 0:
                                adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, alphabetList);
                                break;
                            //数字
                            case 1:
                                adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, numberList);
                                break;
                            //制御キー
                            case 2:
                                adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, controlKeyList);
                                break;
                            //ファンクションキー
                            case 3:
                                adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, functionKeyList);
                                break;
                            //記号
                            case 4:
                                adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, symbolList);
                                break;
                        }
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                        spinner.setSelection(0);

                        Button removeButton = (Button) addKeyCodeView.findViewById(R.id.removeButton);
                        removeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ViewGroup parent = (ViewGroup) view.getParent();
                                keyCodeViewList.remove(parent);
                                parent.removeAllViews();
                            }
                        });
                        keyCodesLayout.addView(addKeyCodeView);
                        keyCodeViewList.add(addKeyCodeView);
                    }
                });
                builder.show();
            }
        });

        final Button addRightButton = (Button) view.findViewById(R.id.button_add_rotate_right_key);
        addRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //Spinnerに入れるキーコードの種類を選択させるダイアログを生成
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                final String[] items = {"アルファベット", "数字", "制御キー", "ファンクションキー", "記号"};
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        View addKeyCodeView = View.inflate(getActivity(), R.layout.layout_add_key_code, null);

                        Spinner spinner = (Spinner) addKeyCodeView.findViewById(R.id.keyCodeSpinner);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
                        //選択結果に応じてSpinnerの選択肢を変える
                        switch (which) {
                            //アルファベット
                            case 0:
                                adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, alphabetList);
                                break;
                            //数字
                            case 1:
                                adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, numberList);
                                break;
                            //制御キー
                            case 2:
                                adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, controlKeyList);
                                break;
                            //ファンクションキー
                            case 3:
                                adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, functionKeyList);
                                break;
                            //記号
                            case 4:
                                adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, symbolList);
                                break;
                        }
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                        spinner.setSelection(0);

                        Button removeButton = (Button) addKeyCodeView.findViewById(R.id.removeButton);
                        removeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ViewGroup parent = (ViewGroup) view.getParent();
                                rightKeyCodeViewList.remove(parent);
                                parent.removeAllViews();
                            }
                        });
                        rightKeyCodesLayout.addView(addKeyCodeView);
                        rightKeyCodeViewList.add(addKeyCodeView);
                    }
                });
                builder.show();
            }
        });

        final Button addLeftButton = (Button) view.findViewById(R.id.button_add_rotate_left_key);
        addLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //Spinnerに入れるキーコードの種類を選択させるダイアログを生成
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                final String[] items = {"アルファベット", "数字", "制御キー", "ファンクションキー", "記号"};
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        View addKeyCodeView = View.inflate(getActivity(), R.layout.layout_add_key_code, null);

                        Spinner spinner = (Spinner) addKeyCodeView.findViewById(R.id.keyCodeSpinner);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
                        //選択結果に応じてSpinnerの選択肢を変える
                        switch (which) {
                            //アルファベット
                            case 0:
                                adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, alphabetList);
                                break;
                            //数字
                            case 1:
                                adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, numberList);
                                break;
                            //制御キー
                            case 2:
                                adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, controlKeyList);
                                break;
                            //ファンクションキー
                            case 3:
                                adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, functionKeyList);
                                break;
                            //記号
                            case 4:
                                adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, symbolList);
                                break;
                        }
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                        spinner.setSelection(0);

                        Button removeButton = (Button) addKeyCodeView.findViewById(R.id.removeButton);
                        removeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ViewGroup parent = (ViewGroup) view.getParent();
                                leftKeyCodeViewList.remove(parent);
                                parent.removeAllViews();
                            }
                        });
                        leftKeyCodesLayout.addView(addKeyCodeView);
                        leftKeyCodeViewList.add(addKeyCodeView);
                    }
                });
                builder.show();
            }
        });

        final Button addFlickUpButton = (Button) view.findViewById(R.id.button_add_flick_up_key);
        addFlickUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //Spinnerに入れるキーコードの種類を選択させるダイアログを生成
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                final String[] items = {"アルファベット", "数字", "制御キー", "ファンクションキー", "記号"};
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        View addKeyCodeView = View.inflate(getActivity(), R.layout.layout_add_key_code, null);

                        Spinner spinner = (Spinner) addKeyCodeView.findViewById(R.id.keyCodeSpinner);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
                        //選択結果に応じてSpinnerの選択肢を変える
                        switch (which) {
                            //アルファベット
                            case 0:
                                adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, alphabetList);
                                break;
                            //数字
                            case 1:
                                adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, numberList);
                                break;
                            //制御キー
                            case 2:
                                adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, controlKeyList);
                                break;
                            //ファンクションキー
                            case 3:
                                adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, functionKeyList);
                                break;
                            //記号
                            case 4:
                                adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, symbolList);
                                break;
                        }
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                        spinner.setSelection(0);

                        Button removeButton = (Button) addKeyCodeView.findViewById(R.id.removeButton);
                        removeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ViewGroup parent = (ViewGroup) view.getParent();
                                flickUpKeyCodeViewList.remove(parent);
                                parent.removeAllViews();
                            }
                        });
                        flickUpKeyCodesLayout.addView(addKeyCodeView);
                        flickUpKeyCodeViewList.add(addKeyCodeView);
                    }
                });
                builder.show();
            }
        });

        final Button addFlickDownButton = (Button) view.findViewById(R.id.button_add_flick_down_key);
        addFlickDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //Spinnerに入れるキーコードの種類を選択させるダイアログを生成
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                final String[] items = {"アルファベット", "数字", "制御キー", "ファンクションキー", "記号"};
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        View addKeyCodeView = View.inflate(getActivity(), R.layout.layout_add_key_code, null);

                        Spinner spinner = (Spinner) addKeyCodeView.findViewById(R.id.keyCodeSpinner);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
                        //選択結果に応じてSpinnerの選択肢を変える
                        switch (which) {
                            //アルファベット
                            case 0:
                                adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, alphabetList);
                                break;
                            //数字
                            case 1:
                                adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, numberList);
                                break;
                            //制御キー
                            case 2:
                                adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, controlKeyList);
                                break;
                            //ファンクションキー
                            case 3:
                                adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, functionKeyList);
                                break;
                            //記号
                            case 4:
                                adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, symbolList);
                                break;
                        }
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                        spinner.setSelection(0);

                        Button removeButton = (Button) addKeyCodeView.findViewById(R.id.removeButton);
                        removeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ViewGroup parent = (ViewGroup) view.getParent();
                                flickDownKeyCodeViewList.remove(parent);
                                parent.removeAllViews();
                            }
                        });
                        flickDownKeyCodesLayout.addView(addKeyCodeView);
                        flickDownKeyCodeViewList.add(addKeyCodeView);
                    }
                });
                builder.show();
            }
        });

        final Button addFlickRightButton = (Button) view.findViewById(R.id.button_add_flick_right_key);
        addFlickRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //Spinnerに入れるキーコードの種類を選択させるダイアログを生成
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                final String[] items = {"アルファベット", "数字", "制御キー", "ファンクションキー", "記号"};
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        View addKeyCodeView = View.inflate(getActivity(), R.layout.layout_add_key_code, null);

                        Spinner spinner = (Spinner) addKeyCodeView.findViewById(R.id.keyCodeSpinner);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
                        //選択結果に応じてSpinnerの選択肢を変える
                        switch (which) {
                            //アルファベット
                            case 0:
                                adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, alphabetList);
                                break;
                            //数字
                            case 1:
                                adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, numberList);
                                break;
                            //制御キー
                            case 2:
                                adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, controlKeyList);
                                break;
                            //ファンクションキー
                            case 3:
                                adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, functionKeyList);
                                break;
                            //記号
                            case 4:
                                adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, symbolList);
                                break;
                        }
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                        spinner.setSelection(0);

                        Button removeButton = (Button) addKeyCodeView.findViewById(R.id.removeButton);
                        removeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ViewGroup parent = (ViewGroup) view.getParent();
                                flickRightKeyCodeViewList.remove(parent);
                                parent.removeAllViews();
                            }
                        });
                        flickRightKeyCodesLayout.addView(addKeyCodeView);
                        flickRightKeyCodeViewList.add(addKeyCodeView);
                    }
                });
                builder.show();
            }
        });

        final Button addFlickLeftButton = (Button) view.findViewById(R.id.button_add_flick_left_key);
        addFlickLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //Spinnerに入れるキーコードの種類を選択させるダイアログを生成
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                final String[] items = {"アルファベット", "数字", "制御キー", "ファンクションキー", "記号"};
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        View addKeyCodeView = View.inflate(getActivity(), R.layout.layout_add_key_code, null);

                        Spinner spinner = (Spinner) addKeyCodeView.findViewById(R.id.keyCodeSpinner);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
                        //選択結果に応じてSpinnerの選択肢を変える
                        switch (which) {
                            //アルファベット
                            case 0:
                                adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, alphabetList);
                                break;
                            //数字
                            case 1:
                                adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, numberList);
                                break;
                            //制御キー
                            case 2:
                                adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, controlKeyList);
                                break;
                            //ファンクションキー
                            case 3:
                                adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, functionKeyList);
                                break;
                            //記号
                            case 4:
                                adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, symbolList);
                                break;
                        }
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                        spinner.setSelection(0);

                        Button removeButton = (Button) addKeyCodeView.findViewById(R.id.removeButton);
                        removeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ViewGroup parent = (ViewGroup) view.getParent();
                                flickLeftKeyCodeViewList.remove(parent);
                                parent.removeAllViews();
                            }
                        });
                        flickLeftKeyCodesLayout.addView(addKeyCodeView);
                        flickLeftKeyCodeViewList.add(addKeyCodeView);
                    }
                });
                builder.show();
            }
        });

        Button createButton = (Button) view.findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                EditText editDescription = (EditText) view.findViewById(R.id.description);
                String description = editDescription.getText().toString();

                int columnCount = (int) columnCountSpinner.getSelectedItem();
                int rowCount = (int) rowCountSpinner.getSelectedItem();

                Key.Type type = Key.Type.toType((String) keyTypeSpinner.getSelectedItem());
                Key key = new EmptyKey();
                switch (type) {
                    case RELEASED:
                        key = new NormalKey(columnCount, rowCount, description, type);
                        break;
                    case LONGPRESS:
                        key = new LongPressKey(columnCount, rowCount, description, type);
                        break;
                    case PRESSING:
                        EditText editInterval = (EditText) view.findViewById(R.id.interval);
                        int interval;
                        try {
                            interval = Integer.parseInt(editInterval.getText().toString());
                        } catch (NumberFormatException e) {
                            showErrorDialog("入力が不完全です");
                            return;
                        }
                        key = new PressingKey(columnCount, rowCount, description, type, interval);
                        break;
                    case KNOB:
                        key = new ControlKnob(columnCount, rowCount, description, type);
                        break;
                    case FLICK:
                        EditText adjustEditText = (EditText) view.findViewById(R.id.edit_text_adjust);
                        int adjust;
                        try {
                            adjust = Integer.parseInt(adjustEditText.getText().toString());
                        } catch (NumberFormatException e) {
                            showErrorDialog("フリック距離を入力してください");
                            return;
                        }
                        key = new FlickKey(columnCount, rowCount, description, type, adjust);
                        break;
                    case EMPTY:
                        key = new EmptyKey(columnCount, rowCount, description, type);
                        break;
                }

                if (!(key instanceof EmptyKey)) {
                    if (key instanceof ControlKnob) {
                        List<String> rightKeyStrList = new ArrayList<>();
                        for (View v : rightKeyCodeViewList) {
                            Spinner spinner = (Spinner) v.findViewById(R.id.keyCodeSpinner);
                            rightKeyStrList.add((String) spinner.getSelectedItem());
                        }
                        List<String> leftKeyStrList = new ArrayList<>();
                        for (View v : leftKeyCodeViewList) {
                            Spinner spinner = (Spinner) v.findViewById(R.id.keyCodeSpinner);
                            leftKeyStrList.add((String) spinner.getSelectedItem());
                        }
                        /*
                        if (rightKeyStrList.isEmpty() || leftKeyStrList.isEmpty()) {
                            showErrorDialog("少なくとも1つの入力キーが必要です");
                            return;
                        }
                        */
                        ((ControlKnob) key).setRotateRightKeyCodeList(rightKeyStrList);
                        ((ControlKnob) key).setRotateLeftKeyCodeList(leftKeyStrList);
                    } else if (key instanceof FlickKey) {
                        List<String> flickUpKeyStrList = new ArrayList<>();
                        for (View v : flickUpKeyCodeViewList) {
                            Spinner spinner = (Spinner) v.findViewById(R.id.keyCodeSpinner);
                            flickUpKeyStrList.add((String) spinner.getSelectedItem());
                        }
                        List<String> flickDownKeyStrList = new ArrayList<>();
                        for (View v : flickDownKeyCodeViewList) {
                            Spinner spinner = (Spinner) v.findViewById(R.id.keyCodeSpinner);
                            flickDownKeyStrList.add((String) spinner.getSelectedItem());
                        }
                        List<String> flickRightKeyStrList = new ArrayList<>();
                        for (View v : flickRightKeyCodeViewList) {
                            Spinner spinner = (Spinner) v.findViewById(R.id.keyCodeSpinner);
                            flickRightKeyStrList.add((String) spinner.getSelectedItem());
                        }
                        List<String> flickLeftKeyStrList = new ArrayList<>();
                        for (View v : flickLeftKeyCodeViewList) {
                            Spinner spinner = (Spinner) v.findViewById(R.id.keyCodeSpinner);
                            flickLeftKeyStrList.add((String) spinner.getSelectedItem());
                        }
                        /*
                        if (rightKeyStrList.isEmpty() || leftKeyStrList.isEmpty()) {
                            showErrorDialog("少なくとも1つの入力キーが必要です");
                            return;
                        }
                        */
                        ((FlickKey) key).setFlickUpKeyStrList(flickUpKeyStrList);
                        ((FlickKey) key).setFlickDownKeyStrList(flickDownKeyStrList);
                        ((FlickKey) key).setFlickRightKeyStrList(flickRightKeyStrList);
                        ((FlickKey) key).setFlickLeftKeyStrList(flickLeftKeyStrList);
                    } else {
                        /*
                        if (description.length() == 0) {
                            showErrorDialog("入力が不完全です");
                            return;
                        }

                        if (keyCodeViewList.size() <= 0) {
                            showErrorDialog("少なくとも1つの入力キーが必要です");
                            return;
                        }
                        */

                        for (View v : keyCodeViewList) {
                            Spinner spinner = (Spinner) v.findViewById(R.id.keyCodeSpinner);
                            key.addKeyCode((String) spinner.getSelectedItem());
                        }
                    }
                }


                if (columnCount * rowCount > getArguments().getInt("emptySpace")) {
                    int shortage = columnCount * rowCount - getArguments().getInt("emptySpace");
                    showErrorDialog("スペースが " + shortage + "足りません");
                    return;
                }

                listener.onKeyUpdated(getArguments().getInt("position"), key);
                dismiss();
            }
        });

        Button cacelButton = (Button) view.findViewById(R.id.cancelButton);
        cacelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("キー編集").setView(view);
        return builder.create();
    }

    private void showErrorDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("エラー").setMessage(message).setPositiveButton("確認", null).show();
    }

    @Override
    public void onResume() {
        super.onResume();

        Dialog dialog = getDialog();
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();

        //display metricsでdpのもと(?)を作る
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        //LayoutParamsにdpを計算して適用(今回は横幅300dp)(※metrics.scaledDensityの返り値はfloat)
        float dialogWidth = 300 * metrics.scaledDensity;
        layoutParams.width = (int) dialogWidth;

        //LayoutParamsをセットする
        dialog.getWindow().setAttributes(layoutParams);
    }


    @Override
    public void onPause() {
        super.onPause();
        dismiss();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnKeyUpdatedListener) {
            listener = (OnKeyUpdatedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnKeyUpdatedListener");
        }
    }

    public interface OnKeyUpdatedListener {
        void onKeyUpdated(int position, Key key);
    }

}
