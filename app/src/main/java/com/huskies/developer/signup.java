package com.huskies.developer;



        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Context;
        import android.content.Intent;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.os.Bundle;
        import android.text.TextUtils;
        import android.view.View;
        import android.view.WindowManager;
        import android.widget.Button;
        import android.widget.Toast;

        import com.google.android.material.textfield.TextInputEditText;

        import java.util.Objects;

public class signup extends AppCompatActivity {
    Button Register, login_back;
    TextInputEditText Email, Password, Name, Username, PhoneNo ;
    String NameHolder, EmailHolder, PasswordHolder, UsernameHolder, PhoneNoHolder;
    Boolean EditTextEmptyHolder;
    SQLiteDatabase sqLiteDatabaseObj;
    String SQLiteDataBaseQueryHolder ;
    SQLiteHelper sqLiteHelper;
    Cursor cursor;
    String F_Result = "Not_Found";
    String EmailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String MobilePattern = "[1-9][0-9]{9}";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);

        Register = findViewById(R.id.sign_btn);
        login_back = findViewById(R.id.login_back);
        Email =  findViewById(R.id.edit_email);
        Password = findViewById(R.id.edit_password);
        Name = findViewById(R.id.edit_name);
        Username = findViewById(R.id.edit_username);
        PhoneNo = findViewById(R.id.edit_phoneNo);

        sqLiteHelper = new SQLiteHelper(this);

        // Adding click listener to register button.
        Register.setOnClickListener(view -> {

            // Creating SQLite database if dose n't exists
            SQLiteDataBaseBuild();

            // Creating SQLite table if dose n't exists.
            SQLiteTableBuild();

            // Checking EditText is empty or Not.
            CheckEditTextStatus();

            // Method to check Email is already exists or not.
            CheckingEmailAlreadyExistsOrNot();

            // Empty EditText After done inserting process.
            EmptyEditTextAfterDataInsert();

        });

        login_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginpage();
            }
        });

    }

    // SQLite database build method.
    public void SQLiteDataBaseBuild(){

        sqLiteDatabaseObj = openOrCreateDatabase(SQLiteHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);

    }

    // SQLite table build method.
    public void SQLiteTableBuild() {

        sqLiteDatabaseObj.execSQL("CREATE TABLE IF NOT EXISTS " + SQLiteHelper.TABLE_NAME + "(" +
                SQLiteHelper.Table_Column_ID + " PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                SQLiteHelper.Table_Column_1_Name + " VARCHAR, " + SQLiteHelper.Table_Column_2_Username + " VARCHAR, " +
                SQLiteHelper.Table_Column_3_Email + " VARCHAR," + SQLiteHelper.Table_Column_4_PhoneNo + "VARCHAR,"
                + SQLiteHelper.Table_Column_5_Password + "VARCHAR);");

    }

    // Insert data into SQLite database method.
    public void InsertDataIntoSQLiteDatabase(){

        // If editText is not empty then this block will executed.
        if(EditTextEmptyHolder)
        {

            // SQLite query to insert data into table.
            SQLiteDataBaseQueryHolder = "INSERT INTO "+SQLiteHelper.TABLE_NAME+" (name,username,email,phoneNo,password)" +
                    " VALUES('"+NameHolder+"', '"+UsernameHolder+"', '"+EmailHolder+"', '"+PhoneNoHolder+"','"+PasswordHolder+"');";

            // Executing query.
            sqLiteDatabaseObj.execSQL(SQLiteDataBaseQueryHolder);

            // Closing SQLite database object.
            sqLiteDatabaseObj.close();

            // Printing toast message after done inserting.
            Toast.makeText(signup.this,"User Registered Successfully!", Toast.LENGTH_LONG).show();

        }
        // This block will execute if any of the registration EditText is empty.
        else {

            // Printing toast message if any of EditText is empty.
            Toast.makeText(signup.this,"Please Fill All The Required Fields.", Toast.LENGTH_LONG).show();

        }

    }

    // Empty edittext after done inserting process method.
    public void EmptyEditTextAfterDataInsert(){

        Objects.requireNonNull(Name.getText()).clear();
        Objects.requireNonNull(Email.getText()).clear();
        Objects.requireNonNull(Password.getText()).clear();
        Objects.requireNonNull(Username.getText()).clear();
        Objects.requireNonNull(PhoneNo.getText()).clear();

    }

    // Method to check EditText is empty or Not.
    public void CheckEditTextStatus(){

        // Getting value from All EditText and storing into String Variables.
        NameHolder = Objects.requireNonNull(Name.getText()).toString() ;
        EmailHolder = Objects.requireNonNull(Email.getText()).toString();
        PasswordHolder = Objects.requireNonNull(Password.getText()).toString();
        UsernameHolder = Objects.requireNonNull(Username.getText()).toString();
        PhoneNoHolder = Objects.requireNonNull(PhoneNo.getText()).toString();

        if(TextUtils.isEmpty(NameHolder) || TextUtils.isEmpty(EmailHolder) ||
                TextUtils.isEmpty(PasswordHolder) || TextUtils.isEmpty(UsernameHolder)
                || TextUtils.isEmpty(PhoneNoHolder)){
            EditTextEmptyHolder = false;
        }
        else if(!EmailHolder.matches(EmailPattern)){
            Toast.makeText(signup.this, "Please Enter Valid Email Address", Toast.LENGTH_SHORT).show();
            EditTextEmptyHolder = false;
        }
        else if(PhoneNo.length()!=10){

            Toast.makeText(signup.this, "Please enter a valid 10 digit phone number.", Toast.LENGTH_SHORT).show();
            EditTextEmptyHolder = false;
        }
        else if(!PhoneNoHolder.matches(MobilePattern)){
            Toast.makeText(signup.this, "Please enter a valid 10 digit phone number.", Toast.LENGTH_SHORT).show();
            EditTextEmptyHolder = false;
        }

        else if(Username.length() < 5){
            Toast.makeText(signup.this, "Please enter a username with more than 5 characters.", Toast.LENGTH_SHORT).show();
            EditTextEmptyHolder = false;
        }
        else if(Password.length() < 8){
            Toast.makeText(signup.this, "Please enter password of more than 8 characters.", Toast.LENGTH_SHORT).show();
            EditTextEmptyHolder = false;
        }
        else{
            EditTextEmptyHolder = true;
        }

    }

    // Checking Email is already exists or not.
    public void CheckingEmailAlreadyExistsOrNot(){

        // Opening SQLite database write permission.
        sqLiteDatabaseObj = sqLiteHelper.getWritableDatabase();

        // Adding search email query to cursor.
        cursor = sqLiteDatabaseObj.query(SQLiteHelper.TABLE_NAME, null, " " +
                        SQLiteHelper.Table_Column_3_Email + "=?", new String[]{EmailHolder}, null, null,
                null);

        while (cursor.moveToNext()) {

            if (cursor.isFirst()) {

                cursor.moveToFirst();

                // If Email is already exists then Result variable value set as Email Found.
                F_Result = "Email Found";

                // Closing cursor.
                cursor.close();
            }
        }

        // Calling method to check final result and insert data into SQLite database.
        CheckFinalResult();

    }


    // Checking result
    public void CheckFinalResult(){

        // Checking whether email is already exists or not.
        if(F_Result.equalsIgnoreCase("Email Found"))
        {

            // If email is exists then toast msg will display.
            Toast.makeText(signup.this,"Email Already Exists",Toast.LENGTH_LONG).show();

        }
        else {

            // If email already dose n't exists then user registration details will entered to SQLite database.
            InsertDataIntoSQLiteDatabase();

        }

        F_Result = "Not_Found" ;

    }

    public void loginpage() {
        Intent intent = new Intent(this, login.class);
        startActivity(intent);
    }
}
