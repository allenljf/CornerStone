package com.trubuzz.cornerstone.util

import android.content.Context
import android.text.TextUtils
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.trubuzz.cornerstone.Constant
import com.trubuzz.cornerstone.R
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

object FormatUtils {
    val percentColor: List<String> = ArrayList(
        Arrays.asList(
            "numType", "float2", "roundDown", "percentage", "plusSign", "riseColor"
        )
    )

    val percentNormal: List<String> = ArrayList(
        Arrays.asList(
            "numType", "float2", "roundDown", "percentage", "plusSign"
        )
    )

    val percentNormalNoSign: List<String> = ArrayList(
        Arrays.asList(
            "numType", "float2", "roundDown", "percentage"
        )
    )

    val numberColor: List<String> = ArrayList(
        Arrays.asList(
            "numType", "float2", "roundDown", "kSeparator", "plusSign", "riseColor"
        )
    )

    val numberNormalNoSign: List<String> = ArrayList(
        Arrays.asList(
            "numType", "float2", "roundDown", "kSeparator"
        )
    )

    fun checkPhone(str: String, areaCode: String): Boolean {
        var isPhoneOk = false
        var regex = "0?\\d+"
        when (areaCode) {
            "+86" -> {
                /*
                大陆：开头1 3-8号码段，后面加9位数字  "[1][345678]\\d{9}"
                */
                regex = "[1]\\d{10}"//"[1]"代表第1位为数字1，"[345678]"代表第二位可以为3、4、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
            }
            "+852" -> {
                //香港：8位數------5、6、8、9開頭 +7位
                regex = "[5689]\\d{7}"
            }
            "+886" -> {
                //台灣：0(可有可無) + 9 + 8位數
                regex = "0?[9]\\d{8}"
            }
            "+853" -> {
                //澳門：8位數------6開頭 +7位
                regex = "[6]\\d{7}"
            }
        }
        if (Pattern.matches(regex, str)) {
            isPhoneOk = true
        }
        return isPhoneOk
    }

    fun checkEmail(str: String?): Boolean {
        //驗證電子郵件格式
        var isMailOk = false
        val patternEmail =
            Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
        if (str != null && patternEmail.matcher(str).find()) {
            isMailOk = true
        }
        return isMailOk
    }

    fun checkVeriCode(str: String?): Boolean {
        //驗證驗證碼格式
        var isCodeOk = false
        if (Pattern.matches("\\d{4}", str)) {
            isCodeOk = true
        }
        return isCodeOk
    }

    //帳號中間用*號
    fun getStarString(content: String, frontNum: Int, endNum: Int): String {

        if (frontNum >= content.length || frontNum < 0) {
            return content
        }
        if (endNum >= content.length || endNum < 0) {
            return content
        }
        if (frontNum + endNum >= content.length) {
            return content
        }
        var starStr = ""
        for (i in 0 until content.length - frontNum - endNum) {
            starStr = "$starStr*"
        }
        return (content.substring(0, frontNum) + starStr
                + content.substring(content.length - endNum, content.length))
    }

    fun isNumeric(str: String): Boolean {
        return if (TextUtils.isEmpty(str)) false else str.matches("-?\\d+(\\.\\d+)?".toRegex())
    }

    fun largeNumber(input_number: Double, decimalFormat: DecimalFormat): String {
        var input_number = input_number
        if (input_number == 0.0) {
            return "0.00"
        }

        var sign = ""
        if (input_number < 0) {
            sign = "-$sign"
        }
        input_number = Math.abs(input_number)
        if (input_number < 1000)
            return sign + decimalFormat.format(input_number)

        var exp = (Math.log(input_number) / Math.log(1000.0)).toInt()
        if (exp > 3) {
            exp = 3
        }
        return sign + decimalFormat.format(input_number / Math.pow(1000.0, exp.toDouble())) + "KMB"[exp - 1]
    }

    fun formatTextView(input: String?, formatList: List<*>, textView: TextView, context: Context) {
        if (input == null) {
            return
        }

        if (formatList.contains("text")) {
            textView.text = input
        }

        var input_number = 0.0
        var output = "0"
        var formatNumberString = ""
        val decimalFormat: DecimalFormat
        //        boolean isPrivate = SettingUtils.isPrivateModeOn();

        if (!isNumeric(input)) {
            if (!formatList.contains("numType")) {
                textView.text = input
                return
            }
            textView.text = Constant.NO_VALUE
            return
        } else {
            input_number = java.lang.Double.parseDouble(input)
        }

        //漲跌顏色
        if (formatList.contains("riseColor")) {
            if (input_number > 0) {
                textView.setTextColor(context.resources.getColor(R.color.colorRising))
            } else if (input_number < 0) {
                textView.setTextColor(context.resources.getColor(R.color.colorFalling))
            } else {
                textView.setTextColor(context.resources.getColor(R.color.gray_B4B4B4))
            }
        }

        //千分位
        if (formatList.contains("kSeparator")) {
            formatNumberString = "#,##0"
        } else {
            formatNumberString = "0"
        }

        //股價
        if (formatList.contains("stockPrice")) {
            if (Math.abs(input_number) > 0 && Math.abs(input_number) < 1) {
                formatNumberString += ".0000"
            } else {
                formatNumberString += ".00"
            }
        }

        //小數點後兩位
        if (formatList.contains("float2")) {
            formatNumberString += ".00"
        } else if (!formatList.contains("stock_price")) {
            formatNumberString += ".##"
        }
        decimalFormat = DecimalFormat(formatNumberString)

        //四捨五入 / 無條件捨去
        if (formatList.contains("rounding")) {
            decimalFormat.roundingMode = RoundingMode.HALF_UP
        } else if (formatList.contains("roundDown")) {
            decimalFormat.roundingMode = RoundingMode.DOWN
        }

        //K/M/B轉換
        if (formatList.contains("largeNumber")) {
            output = largeNumber(input_number, decimalFormat)
        }

        //百分比
        if (formatList.contains("percentage")) {
            output = decimalFormat.format(input_number) + "%"
        } else {
            output = decimalFormat.format(input_number)
        }

        //正負號
        if (formatList.contains("plusSign")) {
            if (input_number > 0)
                output = "+$output"
        }

        //資產
        if (formatList.contains("asset")) {
            output = "$$output"
        }

        textView.text = output
    }

    fun setTextRiseOrDownColor(context: Context, textView: TextView, number: String) {
        var str = number.replace(",", "").replace("%", "")
        try {
            var value = str.toFloat()
            when {
                value > 0 -> textView.setTextColor(ContextCompat.getColor(context, R.color.colorRising))
                value < 0F -> textView.setTextColor(ContextCompat.getColor(context, R.color.colorFalling))
                else -> textView.setTextColor(ContextCompat.getColor(context, R.color.white_FFFFFF))
            }
        } catch (e: Exception) {
            textView.setTextColor(ContextCompat.getColor(context, R.color.white_FFFFFF))
        }

        textView.text = number
    }

    fun getFormatPriceFromObject(source: String?): String {
        if (TextUtils.isEmpty(source)) {
            return Constant.NO_VALUE
        }
        try {
            val str: String? = source
            var df: DecimalFormat = DecimalFormat("#,##0.00")

            str!!.replace(",", "").replace("%", "").trim()
            var ret = str.toFloat()
            return df.format(ret)

        } catch (e: Exception) {
            return source.toString()
        }
    }

    fun getSgnAndPercentStringFromObject(source: String?, multiply100: Boolean): String {
        if (TextUtils.isEmpty(source)) {
            return Constant.NO_VALUE
        }
        val str: String? = source
        var df: DecimalFormat = DecimalFormat("#,##0.00")

        str!!.trim()
        var ret = str.replace(",", "").replace("%", "").toFloat()
        if (multiply100) ret *= 100
        when {
            ret > 0 -> return "+" + df.format(ret) + "%"
            ret < 0 -> return df.format(ret).toString() + "%"
            ret == 0f -> return "0.00%"
        }
        return Constant.NO_VALUE
    }

    fun getPercentStringFromObject(source: String?, multiply100: Boolean): String {
        return getSgnAndPercentStringFromObject(source, multiply100).replace("+", "")
    }

    //去掉千分位
    fun removeDotOfNumber(source: String): String {
        return source.replace(",", "")
    }

    //小數點四捨五入取整數
    fun getIntRoundStringFromObject(source: String?): String {
        if (TextUtils.isEmpty(source)) {
            return Constant.NO_VALUE
        }

        var str: String? = source
        str!!.trim()

        if (isNumeric(str)) {
            return Math.round(str?.toFloat()!!).toString()
        }

        return Constant.NO_VALUE
    }

    //數字轉千分位取整數
    fun getMuchMoneyString(source: Any?): String {
        if (source == null) return ""
        val df = DecimalFormat("#,##0")

        var str: String
        if (source is String) {
            str = source.toString()
        } else {
            str = df.format(source)
        }

        str = str.replace(",".toRegex(), "").replace("%".toRegex(), "").trim { it <= ' ' }


        if (!isNumeric(str)) {
            return str
        } else {
            val value = java.lang.Double.parseDouble(str)
            return df.format(value)
        }
    }


    fun initDecimalFormat(digits: Int): DecimalFormat {
        val b = StringBuffer()
        for (i in 0 until digits) {
            if (i == 0)
                b.append(".")
            b.append("0")
        }
        return DecimalFormat("###,###,###,##0" + b.toString())
    }

    //時間格式
    fun getDate(timestamp: Long): String {
        val formatter = SimpleDateFormat("HH:mm")
        return formatter.format(timestamp)
    }
}