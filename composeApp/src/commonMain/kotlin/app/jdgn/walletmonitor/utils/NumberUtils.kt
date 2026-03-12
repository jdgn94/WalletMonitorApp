package app.jdgn.walletmonitor.utils

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.DecimalMode
import com.ionspin.kotlin.bignum.decimal.RoundingMode

object NumberUtils {
    private val calculationMode = DecimalMode(20, RoundingMode.ROUND_HALF_AWAY_FROM_ZERO)

    /**
     * Formatea un número con decimales fijos y separador de miles.
     */
    fun formatNumber(number: Double, decimals: Int = 2): String {
        val bd = BigDecimal.fromDouble(number)
        // En ionspin bignum 0.3.10, scale solo recibe el valor de la escala.
        // Utiliza ROUND_HALF_AWAY_FROM_ZERO por defecto si no hay un modo definido.
        val scaled = bd.scale(decimals.toLong())
        
        // Usamos toPlainString() para evitar notación científica.
        val parts = scaled.toPlainString().split(".")
        val integerPart = parts[0]
        val fractionalPart = if (parts.size > 1) parts[1] else ""
        
        val paddedFractional = fractionalPart.padEnd(decimals, '0').take(decimals)
        
        val isNegative = integerPart.startsWith("-")
        val cleanInt = if (isNegative) integerPart.substring(1) else integerPart
        
        val reversedInteger = cleanInt.reversed()
        val withCommas = StringBuilder()
        for (i in reversedInteger.indices) {
            if (i > 0 && i % 3 == 0) {
                withCommas.append(",")
            }
            withCommas.append(reversedInteger[i])
        }
        
        val formattedInt = (if (isNegative) "-" else "") + withCommas.reverse().toString()
        
        return if (decimals > 0) {
            "$formattedInt.$paddedFractional"
        } else {
            formattedInt
        }
    }

    /**
     * Formatea como moneda agregando un símbolo.
     */
    fun formatCurrency(number: Double, symbol: String = "$"): String {
        return "$symbol ${formatNumber(number)}"
    }

    /**
     * Formatea la expresión matemática para que los números tengan separadores de miles
     * y los operadores tengan espacios.
     */
    fun formatExpression(expr: String): String {
        if (expr.isEmpty()) return "0"
        val operators = listOf('+', '-', '*', '/')
        val formatted = StringBuilder()
        var currentNumber = ""
        
        for (i in expr.indices) {
            val char = expr[i]
            val isSign = char == '-' && (i == 0 || expr[i-1] in operators)
            
            if (char in operators && !isSign) {
                if (currentNumber.isNotEmpty()) {
                    formatted.append(formatNumberPartForExpression(currentNumber))
                    currentNumber = ""
                }
                val opSymbol = when(char) {
                    '*' -> " × "
                    '/' -> " ÷ "
                    else -> " $char "
                }
                formatted.append(opSymbol)
            } else {
                currentNumber += char
            }
        }
        
        if (currentNumber.isNotEmpty()) {
            formatted.append(formatNumberPartForExpression(currentNumber))
        }
        
        return formatted.toString()
    }

    private fun formatNumberPartForExpression(numberStr: String): String {
        if (numberStr.isEmpty()) return ""
        if (numberStr == "-") return "-"
        val parts = numberStr.split(".")
        val integerPart = parts[0]
        
        val isNegative = integerPart.startsWith("-")
        val cleanInt = if (isNegative) integerPart.substring(1) else integerPart
        
        val reversedInteger = cleanInt.reversed()
        val withCommas = StringBuilder()
        for (i in reversedInteger.indices) {
            if (i > 0 && i % 3 == 0) {
                withCommas.append(",")
            }
            withCommas.append(reversedInteger[i])
        }
        val formattedInt = (if (isNegative) "-" else "") + withCommas.reverse().toString()
        
        return if (parts.size > 1) {
            "$formattedInt.${parts[1]}"
        } else {
            formattedInt
        }
    }

    /**
     * Evalúa una expresión matemática en formato String con alta precisión.
     */
    fun evaluateExpression(expr: String): Double {
        if (expr.isEmpty()) return 0.0
        try {
            var cleanExpr = expr
            while (cleanExpr.isNotEmpty() && cleanExpr.last() in listOf('+', '-', '*', '/')) {
                cleanExpr = cleanExpr.dropLast(1)
            }
            if (cleanExpr.isEmpty()) return 0.0

            val tokens = mutableListOf<String>()
            var currentToken = ""
            val operators = listOf('+', '-', '*', '/')
            
            for (i in cleanExpr.indices) {
                val char = cleanExpr[i]
                val isSign = char == '-' && (i == 0 || cleanExpr[i-1] in operators)
                
                if (char in operators && !isSign) {
                    if (currentToken.isNotEmpty()) tokens.add(currentToken)
                    tokens.add(char.toString())
                    currentToken = ""
                } else {
                    currentToken += char
                }
            }
            if (currentToken.isNotEmpty()) tokens.add(currentToken)

            if (tokens.isEmpty()) return 0.0

            // Multiplicaciones y divisiones
            val afterMd = mutableListOf<Any>()
            var i = 0
            while (i < tokens.size) {
                val token = tokens[i]
                if (token == "*" || token == "/") {
                    val left = if (afterMd.last() is BigDecimal) afterMd.removeAt(afterMd.size - 1) as BigDecimal 
                               else BigDecimal.parseString(afterMd.removeAt(afterMd.size - 1).toString())
                    val right = BigDecimal.parseString(tokens.getOrNull(i + 1) ?: "1")
                    val res = if (token == "*") left.multiply(right, calculationMode) 
                              else left.divide(right, calculationMode)
                    afterMd.add(res)
                    i += 2
                } else {
                    afterMd.add(token)
                    i++
                }
            }

            // Sumas y restas
            var result = if (afterMd[0] is BigDecimal) afterMd[0] as BigDecimal 
                         else BigDecimal.parseString(afterMd[0].toString())
            
            var j = 1
            while (j < afterMd.size) {
                val op = afterMd[j] as String
                val nextVal = if (afterMd[j+1] is BigDecimal) afterMd[j+1] as BigDecimal 
                              else BigDecimal.parseString(afterMd[j+1].toString())
                
                result = if (op == "+") result.add(nextVal) else result.subtract(nextVal)
                j += 2
            }

            // En ionspin bignum 0.3.10 se usa doubleValue(exactRequired: Boolean)
            return result.doubleValue(false)
        } catch (e: Exception) {
            return 0.0
        }
    }
}
