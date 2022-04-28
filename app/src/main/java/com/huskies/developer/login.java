package com.huskies.developer;

        import androidx.appcompat.app.AppCompatActivity;

        import android.app.ActivityOptions;
        import android.content.Intent;
        import android.os.Bundle;
        import android.util.Pair;
        import android.view.View;
        import android.view.WindowManager;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.annotation.SuppressLint;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.text.TextUtils;
        import android.widget.Toast;

        import com.google.android.material.textfield.TextInputEditText;

        import java.util.Objects;

public class login extends AppCompatActivity {
    Button callSignUp, LogInButton;
    ImageView image;
    TextView logoText, slogantext;
    TextInputEditText Username, Password ;
    String UsernameHolder, PasswordHolder;
    Boolean EditTextEmptyHolder;
    SQLiteDatabase sqLiteDatabaseObj;
    SQLiteHelper sqLiteHelper;
    Cursor cursor;
    String TempPassword = "NOT_FOUND" ;
    public static final String UserEmail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        LogInButton = findViewById(R.id.login_btn);
        Username = findViewById(R.id.edit_username);
        Password = findViewById(R.id.edit_password);
        callSignUp = findViewById(R.id.sign_btn);
        image = findViewById(R.id.logo_image);
        logoText = findViewById(R.id.logo_name);
        slogantext = findViewById(R.id.slogan_name);

        sqLiteHelper = new SQLiteHelper(this);

        //Adding click listener to log in button.
        LogInButton.setOnClickListener(view -> {

            // Calling EditText is empty or no method.
            CheckEditTextStatus();

            // Calling login method.
            LoginFunction();

        });

        callSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(login.this, signup.class);
//            startActivity(intent);

            Pair[] pairs = new Pair[7];

            pairs[0] = new Pair<View, String>(image, "logo_image");
            pairs[1] = new Pair<View, String>(logoText, "logo_text");
            pairs[2] = new Pair<View, String>(slogantext, "logo_desc");
            pairs[3] = new Pair<View, String>(Username, "user_trans");
            pairs[4] = new Pair<View, String>(Password, "password_trans");
            pairs[5] = new Pair<View, String>(LogInButton, "button_trans");
            pairs[6] = new Pair<View, String>(callSignUp, "login_trans");

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(login.this, pairs);
            startActivity(intent, options.toBundle());
        });
    }

    // Login function starts from here.
    @SuppressLint("Range")
    public void LoginFunction(){

        if(EditTextEmptyHolder) {

            // Opening SQLite database write permission.
            sqLiteDatabaseObj = sqLiteHelper.getWritableDatabase();

            // Adding search email query to cursor.
            cursor = sqLiteDatabaseObj.query(SQLiteHelper.TABLE_NAME, null, " " + SQLiteHelper.Table_Column_2_Username + "=?", new String[]{UsernameHolder}, null, null, null);

            while (cursor.moveToNext()) {

                if (cursor.isFirst()) {

                    cursor.moveToFirst();

                    // Storing Password associated with entered email.
                    TempPassword = cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_5_Password));

                    // Closing cursor.
                    cursor.close();
                }
            }

            // Calling method to check final result ..
            CheckFinalResult();

        }
        else {

            //If any of login EditText empty then this block will be executed.
            Toast.makeText(login.this,"Please Enter UserName or Password.",Toast.LENGTH_LONG).show();

        }

    }

    // Checking EditText is empty or not.
    public void CheckEditTextStatus(){

        // Getting value from All EditText and storing into String Variables.
        UsernameHolder = Objects.requireNonNull(Username.getText()).toString();
        PasswordHolder = Objects.requireNonNull(Password.getText()).toString();

        // Checking EditText is empty or no using TextUtils.
        EditTextEmptyHolder = !TextUtils.isEmpty(UsernameHolder) && !TextUtils.isEmpty(PasswordHolder);
    }

    // Checking entered password from SQLite database email associated password.
    public void CheckFinalResult(){

        if(TempPassword.equalsIgnoreCase(PasswordHolder))
        {

            Toast.makeText(login.this,"Login Successful!",Toast.LENGTH_LONG).show();

            // Going to Dashboard activity after login success message.
            Intent intent = new Intent(login.this, homePage.class);

            // Sending Email to Dashboard Activity using intent.
//            intent.putExtra(UserEmail, UsernameHolder);

            startActivity(intent);


        }
        else {

            Toast.makeText(login.this,"UserName or Password is Wrong, Please Try Again.",Toast.LENGTH_LONG).show();

        }
        TempPassword = "NOT_FOUND" ;

    }
    public void openhomePage() {
        Intent intent = new Intent(this, homePage.class);
        startActivity(intent);
    }
}

