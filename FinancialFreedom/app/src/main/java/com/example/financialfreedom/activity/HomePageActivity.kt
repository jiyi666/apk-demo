package com.example.financialfreedom.activity

import android.content.Intent
import android.os.Bundle
import com.example.financialfreedom.R
import com.example.financialfreedom.utils.BaseActivity
import kotlinx.android.synthetic.main.activity_home_page.*

class HomePageActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        /* 进入我的自选界面 */
        myOptional.setOnClickListener {
            val intent = Intent(this, MyOptionalActivity::class.java)
            this.startActivity(intent)
        }

        /* 进入潜力精选界面 */
        potential.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
        }

        /* 进入我的关注界面 */
        interests.setOnClickListener {
            val intent = Intent(this, MyAttentionActivity::class.java)
            this.startActivity(intent)
        }
    }
}
