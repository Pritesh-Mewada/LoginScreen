package graphysis.loginscreen

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast

class SignUpActivity : AppCompatActivity() {
    lateinit var email:TextInputEditText;
    lateinit var password:TextInputEditText;
    lateinit var rpassword:TextInputEditText;
    lateinit var signup:Button;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        signup =    findViewById(R.id.signup);
        password =  findViewById(R.id.password);
        rpassword = findViewById(R.id.rpassword);
        email = findViewById(R.id.email_id);

        signup.setOnClickListener(View.OnClickListener {

            var validity : Boolean = password.text.toString().isEmpty();

            var match :Boolean = password.text.toString().equals(rpassword.text.toString());
            if(verifyEmail(email.text.toString()) && !validity && match){
                Toast.makeText(applicationContext,"Ready to signup",Toast.LENGTH_LONG).show();
            }


        })

    }
    fun verifyEmail(email:String):Boolean{
        if (getIndex(email,".")>2 && getIndex(email,"@")>5) return true else return false
    }
    fun getIndex(email:String,str:String):Int{
        return (email.length - email.lastIndexOf(str));
    }
}
