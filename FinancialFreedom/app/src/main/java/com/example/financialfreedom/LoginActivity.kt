package com.example.financialfreedom

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.financialfreedom.utils.BaseActivity
import kotlinx.android.synthetic.main.activity_login.*

/**
 *  登录界面的activity
 */
class LoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*
         * 加载登录界面布局
         */
        setContentView(R.layout.activity_login)

        /*
         * login按键响应事件
         */
        login.setOnClickListener {
            val account = accountEdit.text.toString()
            val password = passwordEdit.text.toString()
            /*
             * 如果账号是jiyi，密码是6，就认为登陆成功
             */
            if (account == "jiyi" && password == "6"){
                /*
                 * 使用intent去开启MainActivity
                 */
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent) //开启活动
                finish() //销毁此活动
            } else {
                Toast.makeText(this, "account or password is invalid",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }
}