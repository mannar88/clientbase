package ru.burdin.clientbase.add;

import android.content.ContentValues;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Comparator;

import ru.burdin.clientbase.Bd;
import ru.burdin.clientbase.R;
import ru.burdin.clientbase.StaticClass;
import ru.burdin.clientbase.models.User;

public class AddClientActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextSurname;
    private EditText editTextPhone;
    private EditText editTextComment;
    private Bd bd;
    private User user;
    private int index = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);
        editTextName = findViewById(R.id.editTextName);
        editTextSurname = findViewById(R.id.editTextSurname);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextComment = findViewById(R.id.editTextComment);
        bd = Bd.load(getApplicationContext());
            index = getIntent().getIntExtra(Bd.TABLE, -1);
        getIntent().removeExtra(Bd.TABLE);
        if (index > -1) {
            user = bd.getUsers().get(index);
            editTextName.setText(user.getName());
            editTextName.setSelection(editTextName.getText().length());
            editTextSurname.setText(user.getSurname());
            editTextSurname.setSelection(editTextPhone.getText().length());
            editTextPhone.setText(user.getPhone());
            editTextPhone.setSelection(editTextPhone.getText().length());
            editTextComment.setText(user.getComment());
            editTextComment.setSelection(editTextComment.getText().length());
        }else {
            editTextPhone.setText(getIntent().getStringExtra(StaticClass.NUMBER_PHONE));
        }
        tooListen();
    }

    public void buttonSaveC(View view) {
        if (check()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Bd.COLUMN_NAME, editTextName.getText().toString());
            contentValues.put(Bd.COLUMN_SURNAME, editTextSurname.getText().toString());
            contentValues.put(Bd.COLUMN_PHONE, editTextPhone.getText().toString());
            contentValues.put(Bd.COLUMN_COMMENT, editTextComment.getText().toString());
            if (index == -1) {
                long id = bd.add(Bd.TABLE, contentValues);
                if (id > 0) {
                    if (bd.getUsers().add(new User(id, editTextName.getText().toString(), editTextSurname.getText().toString(), editTextPhone.getText().toString(), editTextComment.getText().toString()))) {
                        bd.getUsers().sort(Comparator.naturalOrder());
                    }
                    Toast.makeText(getApplicationContext(), "Клиент успешно добавлен", Toast.LENGTH_SHORT).show();
                }
                } else {
                if (bd.update(Bd.TABLE, contentValues, user.getId()) == 1) {
                    bd.getUsers().get(index).setName(editTextName.getText().toString());
                    bd.getUsers().get(index).setSurname(editTextSurname.getText().toString());
                    bd.getUsers().get(index).setPhone(editTextPhone.getText().toString());
                    bd.getUsers().get(index).setComment(editTextComment.getText().toString());
                }
            Toast.makeText(getApplicationContext(), "Карточка клиента успешна обновлена", Toast.LENGTH_SHORT).show();
            }
finish();
        }
    }

    private boolean check() {
        boolean result = false;
        if (editTextName.getText().length() > 0 || editTextSurname.getText().length() > 0 || editTextPhone.getText().length() > 0) {
            result = true;
        }
        return result;
    }

    private void tooListen() {
        editTextPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String result = "";
                for (int j = 0; j < charSequence.length(); j++) {
                    if ((charSequence.charAt(j) == '+' && j == 0) || Character.isDigit(charSequence.charAt(j))) {
                        if (j == 0 && charSequence.charAt(j) == '8') {
                            result = "+7";
                            continue;
                        }
                        result = result + charSequence.charAt(j);
                    }
                }
                if (!result.equals(charSequence.toString())) {
                    editTextPhone.setText(result);
                }
                editTextPhone.setSelection(result.length());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void onClickButtonAddClientExchange(View view) {
    String exchange = editTextSurname.getText().toString();
    editTextSurname.setText(editTextName.getText().toString());
    editTextName.setText(exchange);
    }
}
