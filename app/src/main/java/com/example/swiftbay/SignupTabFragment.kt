package com.example.swiftbay

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.fazalmapbox.utils.APIInterface
import com.example.fazalmapbox.utils.PrefsHelper
import com.example.swiftbay.Model.LoginResponse
import com.example.swiftbay.Model.ModelSignup
import com.example.swiftbay.Model.SignupResponse
import com.example.swiftbay.helper.dismissDialoog
import com.example.swiftbay.helper.showDialoog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupTabFragment : Fragment() {
    var nameEd: EditText? = null
    var mailEd: EditText? = null
    var passEd: EditText? = null
    var confpassEd: EditText? = null
    var restaurantRb: RadioButton? = null
    var adminRb: RadioButton? = null
    var customerRb: RadioButton? = null
    var signupButton: Button? = null
    var role: String? = "role"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.signup_tab_fragment, container, false) as ViewGroup

        nameEd = view.findViewById(R.id.nameEd)
        mailEd = view.findViewById(R.id.mailEd)
        passEd = view.findViewById(R.id.passEd)
        confpassEd = view.findViewById(R.id.confpassEd)
        restaurantRb = view.findViewById(R.id.restaurantRb)
        adminRb = view.findViewById(R.id.adminRb)
        customerRb = view.findViewById(R.id.customerRb)
        signupButton = view.findViewById(R.id.button)
        PrefsHelper.init(requireActivity())
        signupButton!!.setOnClickListener {

            if (nameEd!!.text.isNotEmpty()) {
                if (mailEd!!.text.isNotEmpty()) {
                    if (passEd!!.text.isNotEmpty()) {
                        if (confpassEd!!.text.isNotEmpty() && confpassEd!!.text.toString()
                                .equals(passEd!!.text.toString())
                        ) {
                            if (restaurantRb!!.isChecked) {
                                role = restaurantRb!!.text.toString()
                            }
                            if (adminRb!!.isChecked) {
                                role = adminRb!!.text.toString()
                            }
                            if (customerRb!!.isChecked) {
                                role = customerRb!!.text.toString()
                            }
                            callApi(
                                nameEd!!.text.toString(),
                                mailEd!!.text.toString(),
                                passEd!!.text.toString(),
                                role!!
                            )
                        }
                    }
                }
            }

        }

        return view
    }


    fun callApi(name: String, email: String, password: String, role: String) {
        showDialoog(requireActivity(),"")
        val apiInterface = APIInterface.retrofit.create(APIInterface::class.java)
        val modelSignup = ModelSignup(name, email, password, role)
        val call: Call<SignupResponse> = apiInterface.signupUser(modelSignup)
        call.enqueue(object : Callback<SignupResponse?> {
            override fun onResponse(
                call: Call<SignupResponse?>,
                response: Response<SignupResponse?>
            ) {
                assert(response.body() != null)
                Toast.makeText(requireActivity(), "Signup Successfully", Toast.LENGTH_LONG).show()
                dismissDialoog()
            }

            override fun onFailure(call: Call<SignupResponse?>, t: Throwable) {
                dismissDialoog()
                Toast.makeText(requireActivity(), "Signup Failed", Toast.LENGTH_LONG).show()
            }
        })

    }


}

