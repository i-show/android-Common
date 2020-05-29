package com.ishow.noah.modules.main.home

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.Observer
import com.ishow.common.extensions.toJSON
import com.ishow.common.utils.router.AppRouter
import com.ishow.common.utils.textwatcher.PhoneNumberTextWatcher
import com.ishow.common.widget.PrintView
import com.ishow.noah.R
import com.ishow.noah.databinding.FHomeBinding
import com.ishow.noah.modules.account.common.AccountModel
import com.ishow.noah.modules.base.mvvm.view.AppBindFragment
import kotlinx.android.synthetic.main.f_home.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Created by yuhaiyang on 2020-05-11.
 */
class HomeFragment : AppBindFragment<FHomeBinding, HomeViewModel>() {

    override fun getLayout(): Int = R.layout.f_home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        PrintView.init(printView)
        PrintView.reset()

        phone.addTextChangedListener(PhoneNumberTextWatcher())
        //phone.addTextChangedListener(PhoneNumberFormattingTextWatcher())
    }

    override fun initViewModel(vm: HomeViewModel) {
        super.initViewModel(vm)
        vm.test2.observe(this, Observer { PrintView.print(it.toString()) })
    }

    override fun onRightClick(v: View) {
        super.onRightClick(v)
        AppRouter.with(context)
            .action("com.yuhaiyang.androidcommon.Test")
            .start()
    }




    inner class PhoneNumberTextWatcher2 : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(str: CharSequence, arg1: Int, arg2: Int, arg3: Int) {
            var contents = str.toString()
            val length = contents.length
            if (length == 4) {
                if (contents.substring(3) == " ") { // -
                    contents = contents.substring(0, 3)
                    phone.setText(contents)
                    phone.setSelection(contents.length)
                } else { // +
                    contents = contents.substring(0, 3) + " " + contents.substring(3)
                    phone.setText(contents)
                    phone.setSelection(contents.length)
                }
            } else if (length == 9) {
                if (contents.substring(8) == " ") { // -
                    contents = contents.substring(0, 8)
                    phone.setText(contents)
                    phone.setSelection(contents.length)
                } else { // +
                    contents = contents.substring(0, 8) + " " + contents.substring(8)
                    phone.setText(contents)
                    phone.setSelection(contents.length)
                }
            }
        }
    }

    companion object {

        fun newInstance(): HomeFragment {

            val args = Bundle()

            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }
}