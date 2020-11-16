package com.zhx.myworkdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.zhx.myworkdemo.view.dfab.ActionInterface
import com.zhx.myworkdemo.view.dfab.ActionItem
import com.zhx.myworkdemo.view.dfab.FabConfig
import com.zhx.myworkdemo.view.dfab.RefreshItem
import kotlinx.android.synthetic.main.activity_test_dfab.*

class TestDfabActivity : AppCompatActivity() {

    companion object {
        const val NAVIGATION_MODE_ACTION = "action"
        const val NAVIGATION_MODE_REFRESH = "refresh"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_dfab)
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
        Toast.makeText(MyApplication.getContext(), "back click !", Toast.LENGTH_SHORT).show()
    }

    /**
     * 导航球 -- 点击refresh
     */
    private fun onNavRefresh() {
        Toast.makeText(MyApplication.getContext(), "refresh click !", Toast.LENGTH_SHORT).show()
    }


    /**
     * 导航球 -- 点击home
     */
    private fun onNavHome() {
        Toast.makeText(MyApplication.getContext(), "home click !", Toast.LENGTH_SHORT).show()
    }

}
