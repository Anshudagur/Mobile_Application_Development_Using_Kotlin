package com.anshu.calculaterdesign

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.math.BigDecimal

class CalculatorActivity : AppCompatActivity() {

    private lateinit var tvExpression: TextView
    private lateinit var tvResult: TextView

    private var expression = ""
    private val operators = setOf('+', '-', '×', '÷')

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_calculator)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tvExpression = findViewById(R.id.tvExpression)
        tvResult = findViewById(R.id.tvResult)

        findViewById<View>(R.id.btnDelete).setOnClickListener {
            if (expression.isNotEmpty()) expression = expression.dropLast(1)
            updateDisplay()
        }

        findViewById<View>(R.id.btnAC).setOnClickListener {
            expression = ""
            tvResult.text = ""
            updateDisplay()
        }

        findViewById<View>(R.id.btnPercent).setOnClickListener {
            val num = currentNumber()
            if (num.isEmpty() || num == "-") return@setOnClickListener
            val value = num.toDoubleOrNull() ?: return@setOnClickListener
            expression = expression.dropLast(num.length) + format(value / 100)
            updateDisplay()
        }

        findViewById<View>(R.id.btnDivide).setOnClickListener {
            addOperator('÷')
        }

        findViewById<View>(R.id.btn7).setOnClickListener {
            expression += "7"
            updateDisplay()
        }

        findViewById<View>(R.id.btn8).setOnClickListener {
            expression += "8"
            updateDisplay()
        }

        findViewById<View>(R.id.btn9).setOnClickListener {
            expression += "9"
            updateDisplay()
        }

        findViewById<View>(R.id.btnMultiply).setOnClickListener {
            addOperator('×')
        }

        findViewById<View>(R.id.btn4).setOnClickListener {
            expression += "4"
            updateDisplay()
        }

        findViewById<View>(R.id.btn5).setOnClickListener {
            expression += "5"
            updateDisplay()
        }

        findViewById<View>(R.id.btn6).setOnClickListener {
            expression += "6"
            updateDisplay()
        }

        findViewById<View>(R.id.btnMinus).setOnClickListener {
            addOperator('-')
        }

        findViewById<View>(R.id.btn1).setOnClickListener {
            expression += "1"
            updateDisplay()
        }

        findViewById<View>(R.id.btn2).setOnClickListener {
            expression += "2"
            updateDisplay()
        }

        findViewById<View>(R.id.btn3).setOnClickListener {
            expression += "3"
            updateDisplay()
        }

        findViewById<View>(R.id.btnPlus).setOnClickListener {
            addOperator('+')
        }

        findViewById<View>(R.id.btnPlusMinus).setOnClickListener {
            toggleSign()
            updateDisplay()
        }

        findViewById<View>(R.id.btn0).setOnClickListener {
            expression += "0"
            updateDisplay()
        }

        findViewById<View>(R.id.btnDot).setOnClickListener {
            val lastNumber = currentNumber()
            if (lastNumber.contains('.')) return@setOnClickListener
            expression += if (lastNumber.isEmpty() || lastNumber == "-") "0." else "."
            updateDisplay()
        }

        findViewById<View>(R.id.btnEqual).setOnClickListener {
            val result = evaluate(expression)
            if (result == null) {
                tvResult.text = "Error"
            } else {
                expression = format(result)
                tvResult.text = ""
                updateDisplay()
            }
        }
    }


    private fun addOperator(op: Char) {
        if (expression.isEmpty()) {
            if (op == '-') expression = "-"
        } else if (expression.last() in operators) {
            expression = expression.dropLast(1) + op
        } else if (expression != "-") {
            expression += op
        }
        updateDisplay()
    }

    private fun updateDisplay() {
        tvExpression.text = if (expression.isEmpty()) "0" else expression

        if (expression.any { it in operators }) {
            val preview = evaluate(expression.trimEnd { it in operators })
            tvResult.text = preview?.let { format(it) } ?: ""
        } else {
            tvResult.text = ""
        }
    }

    private fun currentNumber(): String {
        var i = expression.length
        while (i > 0 && (expression[i - 1].isDigit() || expression[i - 1] == '.')) i--
        // include a unary minus (at start or right after an operator)
        if (i > 0 && expression[i - 1] == '-' &&
            (i - 1 == 0 || expression[i - 2] in operators)
        ) i--
        return expression.substring(i)
    }

    private fun toggleSign() {
        val num = currentNumber()
        if (num.isEmpty() || num == "-") return
        val start = expression.length - num.length
        expression = if (num.startsWith("-")) {
            expression.removeRange(start, start + 1)
        } else {
            expression.substring(0, start) + "-" + expression.substring(start)
        }
    }

    private fun format(value: Double): String =
        BigDecimal.valueOf(value).stripTrailingZeros().toPlainString()

    private fun evaluate(expr: String): Double? {
        if (expr.isEmpty()) return null
        val numbers = mutableListOf<Double>()
        val ops = mutableListOf<Char>()
        val sb = StringBuilder()

        for ((index, ch) in expr.withIndex()) {
            val isUnaryMinus = ch == '-' && (index == 0 || expr[index - 1] in operators)
            if (ch.isDigit() || ch == '.' || isUnaryMinus) {
                sb.append(ch)
            } else if (ch in operators) {
                numbers.add(sb.toString().toDoubleOrNull() ?: return null)
                sb.clear()
                ops.add(ch)
            }
        }
        numbers.add(sb.toString().toDoubleOrNull() ?: return null)

        var i = 0
        while (i < ops.size) {
            if (ops[i] == '×' || ops[i] == '÷') {
                val a = numbers[i]
                val b = numbers[i + 1]
                if (ops[i] == '÷' && b == 0.0) return null   // divide by zero
                numbers[i] = if (ops[i] == '×') a * b else a / b
                numbers.removeAt(i + 1)
                ops.removeAt(i)
            } else i++
        }

        var result = numbers[0]
        for (j in ops.indices) {
            result = if (ops[j] == '+') result + numbers[j + 1] else result - numbers[j + 1]
        }
        return result
    }
}