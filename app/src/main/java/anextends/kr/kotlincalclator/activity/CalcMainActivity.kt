package anextends.kr.kotlincalclator.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import anextends.kr.kotlincalclator.R
import kotlinx.android.synthetic.main.activity_calc_main.*
import java.util.regex.Pattern

class CalcMainActivity : AppCompatActivity(), View.OnClickListener {

    private val inputList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_main)
        // クリックリスナー登録
        // # build.gradleに"apply plugin: 'kotlin-android-extensions'"の記載があるため
        // # findViewByIdが不要
        calc_btn_1.setOnClickListener(this)
        calc_btn_2.setOnClickListener(this)
        calc_btn_3.setOnClickListener(this)
        calc_btn_4.setOnClickListener(this)
        calc_btn_5.setOnClickListener(this)
        calc_btn_6.setOnClickListener(this)
        calc_btn_7.setOnClickListener(this)
        calc_btn_8.setOnClickListener(this)
        calc_btn_9.setOnClickListener(this)
        calc_btn_ac.setOnClickListener(this)
        calc_btn_sign.setOnClickListener(this)
        calc_btn_percent.setOnClickListener(this)
        calc_btn_div.setOnClickListener(this)
        calc_btn_multi.setOnClickListener(this)
        calc_btn_sub.setOnClickListener(this)
        calc_btn_add.setOnClickListener(this)
        calc_btn_equal.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when(view.id) {
            R.id.calc_btn_0, R.id.calc_btn_1, R.id.calc_btn_2, R.id.calc_btn_3, R.id.calc_btn_4
          , R.id.calc_btn_5, R.id.calc_btn_6, R.id.calc_btn_7, R.id.calc_btn_8, R.id.calc_btn_9
                -> inputNum(view.id)
            R.id.calc_btn_ac
                -> inputAllClear()
            R.id.calc_btn_sign
                -> inputSign()
            R.id.calc_btn_add, R.id.calc_btn_sub, R.id.calc_btn_multi, R.id.calc_btn_div
                -> inputOperator(view.id)
            R.id.calc_btn_equal
                -> inputEqual()
        }
    }

    /**
     * 数字押下時
     */
    private fun inputNum(id: Int) {
        val now = txt_calc_input.text.toString()
        val input = findViewById<Button>(id).text.toString()
        if ("0" != now) {
            // "0"でないならそのまま追加
            txt_calc_input.text = txt_calc_input.text.toString() + input
        } else {
            // "0"なら"0"以外の場合更新
            txt_calc_input.text = if("0" != input) input
                                  else "0"
        }
    }

    /**
     * AC押下時
     */
    private fun inputAllClear() {
        txt_calc_input.text = getString(R.string.calc_btn_0)
        txt_calc_preview.text = ""
        inputList.clear()
    }

    /**
     * ±押下時
     */
    private fun inputSign() {
        val txt = txt_calc_input.text.toString()
        txt_calc_input.text =
                if(txt.startsWith("-")) txt.replace("-", "", true)
                else "-$txt"
    }

    /**
     * 演算子押下時
     */
    private fun inputOperator(id: Int) {
        if(inputList.size == 0) return
        if(!isHalfNumeric(inputList.get(inputList.size - 1))) {
            // 演算子が連続して入力された場合は入れ替え
            inputList.removeAt(inputList.size - 1)
            inputList.add(findViewById<Button>(id).text.toString())
        } else {
            inputList.add(txt_calc_input.text.toString())
        }
        updatePreview()
    }

    /**
     * 途中式更新
     */
    private fun updatePreview() {
        var formula  = ""
        for(s in inputList) {
            formula = "$s "
        }
        // 最後の空白除去
        formula = formula.substring(0, formula.length - 1)
        txt_calc_preview.text = formula
    }

    /**
     * =押下時
     */
    private fun inputEqual() {
        var result: Long? = null
        var second: Long? = null
        var ope: String? = null
        for(s in inputList) {
            if (isHalfNumeric(s)) {
                if(null == result) result = s.toLong()
                else              second = s.toLong()
            } else {
                ope = s
            }
            if(null != result && null != second && null != ope) {
                result = calc(result, second, ope)
            }
        }
        txt_calc_input.text = result.toString()
    }

    private fun isHalfNumeric(input: String): Boolean {
        val pattern = Pattern.compile("^[0-9]*$")
        return  pattern.matcher(input).matches()
    }

    private fun calc(a: Long, b: Long, ope: String): Long {
        return when(ope) {
            getString(R.string.calc_btn_add)   -> a + b
            getString(R.string.calc_btn_sub)   -> a - b
            getString(R.string.calc_btn_multi) -> a * b
            getString(R.string.calc_btn_div)   -> a / b
            else                               -> 0
        }
    }

}
