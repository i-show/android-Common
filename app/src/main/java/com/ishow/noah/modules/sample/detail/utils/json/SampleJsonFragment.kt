package com.ishow.noah.modules.sample.detail.utils.json

import android.os.Bundle
import android.view.View
import com.ishow.common.extensions.parseJSON
import com.ishow.common.extensions.toJSON
import com.ishow.common.widget.PrintView
import com.ishow.noah.R
import com.ishow.noah.databinding.FSampleJsonBinding
import com.ishow.noah.modules.base.mvvm.view.AppBindFragment
import kotlinx.android.synthetic.main.f_sample_json.*

/**
 * Created by yuhaiyang on 2020-01-17.
 */
class SampleJsonFragment : AppBindFragment<FSampleJsonBinding, SampleJsonViewModel>() {

    override fun getLayout(): Int = R.layout.f_sample_json

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        PrintView.init(printView)

        val test = TestJSON("张三", 10)

        val testMap = HashMap<String, TestJSON>()
        testMap["aaa"] = test

        var json: String? = null
        toJson.setOnClickListener {
            PrintView.reset()
            json = testMap.toJSON()
            PrintView.print("json = $json")
        }

        fromJson.setOnClickListener {
            val result: HashMap<String, TestJSON>? = json?.parseJSON()
            PrintView.reset()
            PrintView.print(result?.toString())
        }
    }


    class TestJSON(
        var name: String? = null,
        var age: Int = 0
    ) {
        override fun toString(): String {
            return "TestJSON(name=$name, age=$age)"
        }
    }


}