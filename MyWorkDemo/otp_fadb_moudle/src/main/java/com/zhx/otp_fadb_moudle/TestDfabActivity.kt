package com.zhx.myworkdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.zhx.baselibrary.utils.ARouterConstants
import com.zhx.otp_fadb_moudle.R
import com.zhx.otp_fadb_moudle.view.dfab.ActionInterface
import com.zhx.otp_fadb_moudle.view.dfab.ActionItem
import com.zhx.otp_fadb_moudle.view.dfab.FabConfig
import com.zhx.otp_fadb_moudle.view.dfab.RefreshItem
import kotlinx.android.synthetic.main.otp_activity_test_dfab.*

@Route(path = ARouterConstants.GROUP_OTP_HOME)
open class TestDfabActivity : AppCompatActivity() {

    companion object {
        const val NAVIGATION_MODE_ACTION = "action"
        const val NAVIGATION_MODE_REFRESH = "refresh"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.otp_activity_test_dfab)
        initNavigationBall()
    }


    /**
     * 初始化导航球配置
     */
    private fun initNavigationBall() {
        val config = FabConfig.Builder()
                .setDragable(true)
                .setBottomMargin(40)
                .addItem(NAVIGATION_MODE_ACTION, ActionItem(), true)
                .addItem(NAVIGATION_MODE_REFRESH, RefreshItem())
                .setCallback { actionType, _ ->
                    when (actionType) {
                        ActionInterface.Action.BACK -> onNavBack()
                        ActionInterface.Action.HOME -> onNavHome()
                        ActionInterface.Action.REFRESH -> onNavRefresh()
                        else -> {

                        }
                    }
                }
                .build()
        navigationBall.initConfig(config)
    }

    /**
     * 导航球 -- 点击back
     */
    private fun onNavBack() {
        Toast.makeText(this, "back click !", Toast.LENGTH_SHORT).show()
    }

    /**
     * 导航球 -- 点击refresh
     */
    private fun onNavRefresh() {
        Toast.makeText(this, "refresh click !", Toast.LENGTH_SHORT).show()
    }


    /**
     * 导航球 -- 点击home
     */
    private fun onNavHome() {
        Toast.makeText(this, "home click !", Toast.LENGTH_SHORT).show()
    }

}
