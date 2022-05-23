package com.example.swiftbay

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.fazalmapbox.utils.APIInterface
import com.example.fazalmapbox.utils.PrefsHelper
import com.example.swiftbay.Model.LoginResponse
import com.example.swiftbay.Model.ModelLogin
import com.example.swiftbay.helper.dismissDialoog
import com.example.swiftbay.helper.showDialoog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

public class LoginTabFragment : Fragment() {
    var mailEd: EditText? = null
    var passEd: EditText? = null
    var loginButton: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.login_tab_fragment, container, false) as ViewGroup

        mailEd = view.findViewById(R.id.mailEd)
        passEd = view.findViewById(R.id.passEd)
        loginButton = view.findViewById(R.id.button2)
        PrefsHelper.init(requireActivity())
        loginButton!!.setOnClickListener {

            if (mailEd!!.text.isNotEmpty()) {
                if (passEd!!.text.isNotEmpty()) {
                    callApi(mailEd!!.text.toString(), passEd!!.text.toString())
                }
            }

        }

        return view
    }

    fun callApi(mail: String, password: String) {
        showDialoog(requireActivity(),"")
        val apiInterface = APIInterface.retrofit.create(APIInterface::class.java)
        val modelLogin = ModelLogin(mail, password)
        val call: Call<LoginResponse> = apiInterface.loginUser(modelLogin)
        call.enqueue(object : Callback<LoginResponse?> {
            override fun onResponse(
                call: Call<LoginResponse?>,
                response: Response<LoginResponse?>
            ) {
                if (response.body() != null) {
                    PrefsHelper.write(PrefsHelper.TOKEN, response.body()!!.token.toString())
                    val token = PrefsHelper.read(PrefsHelper.TOKEN, "")
                    Toast.makeText(requireActivity(), "Success", Toast.LENGTH_LONG).show()
                    if(response.body()!!.user != null && response.body()!!.user!!.role.equals("customer")){
                        PrefsHelper.write(PrefsHelper.ROLE, "customer")
                        PrefsHelper.write(PrefsHelper.LOGIN, true)
                       // val i = Intent(context,MainActivity::class.java)
                        dismissDialoog()
                      //  startActivity(i)

                        val intent =  Intent()

                        requireActivity().setResult(AppCompatActivity.RESULT_OK, intent)
                        requireActivity().finish()
                    } else if(response.body()!!.user != null && response.body()!!.user!!.role.equals("seller")){
                        PrefsHelper.write(PrefsHelper.ROLE, "seller")
                        val i = Intent(context,restaurant_home_page::class.java)
                        dismissDialoog()
                        startActivity(i)
                    }else {
                        PrefsHelper.write(PrefsHelper.ROLE, "rider")
                        val i = Intent(context,RiderMainActivty::class.java)
                        dismissDialoog()
                        startActivity(i)
                    }


                } else {
                    dismissDialoog()
                    Toast.makeText(
                        requireActivity(),
                        "Failed to login: ${response.message()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                dismissDialoog()
                Toast.makeText(requireActivity(), "Fail", Toast.LENGTH_LONG).show()

            }
        })

    }
}




